package fr.wcs.wildcommunitysocks;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class ReportsActivity extends AppCompatActivity {

    private RecyclerView rView;
    private GridLayoutManager lLayout;
    public AdapterFlux rcAdapter;
    private DatabaseReference mDatabase;
    private ProgressDialog progressDialog;
    private List<Chaussette> rowListItem;
    private FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        final List<Chaussette> rowListItem = getAllItemList();
        lLayout = new GridLayoutManager(this, 3);

        rView = (RecyclerView) findViewById(R.id.recyclerViewMyProfile);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(lLayout);


        rcAdapter = new AdapterFlux(this, rowListItem);
        rView.setAdapter(rcAdapter);


        firebaseAuth =FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_REPORTS);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //progressDialog.dismiss();
                rowListItem.clear();

                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    Chaussette sock = postSnapshot.getValue(Chaussette.class);

                    rowListItem.add(sock);
                    rcAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        rView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, rView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        int itemPosition = rView.getChildLayoutPosition(view);
                        Chaussette item = rowListItem.get(itemPosition);
                        Intent intent = new Intent(ReportsActivity.this, SocksActivity.class);
                        intent.putExtra("sock", item);
                        intent.putExtra("position", itemPosition);
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        int itemPosition = rView.getChildLayoutPosition(view);
                        final Chaussette item = rowListItem.get(itemPosition);

                        new SweetAlertDialog(ReportsActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText(getString(R.string.moderatorAlertTitle))
                                .setContentText(getString(R.string.moderatorAlertRemove))
                                .setCancelText(getString(R.string.alertDialogueCancel))
                                .setConfirmText(getString(R.string.alertDialogConfirm))
                                .showCancelButton(true)
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        // reuse previous dialog instance, keep widget user state, reset them if you need
                                        sDialog.setTitleText(getString(R.string.moderatorAlertNoRemove))
                                                .setContentText("!!!")
                                                .showCancelButton(false)
                                                .setCancelClickListener(null)
                                                .setConfirmClickListener(null)
                                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                    }
                                })
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        removeButton(item);
                                        sDialog.setTitleText("Supprimer !")
                                                .setContentText("...")
                                                .showCancelButton(false)
                                                .setCancelClickListener(null)
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                        finish();
                                                        //startActivity(new Intent(MySocksActivity.this,Navigation.class));
                                                    }
                                                })
                                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                    }
                                })
                                .show();
                        return;
                    }
                })



        );

    }
    private List<Chaussette> getAllItemList(){

        List<Chaussette> allItems = new ArrayList<>();


        return allItems;
    }

    public void removeButton(Chaussette sock) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        mDatabase= FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_SOCKS);
        String category =sock.getmCategory();

        mDatabase.child(user.getUid()).child(Constants.DATABASE_PATH_UPLOADS).child(sock.getmIdChaussette()).removeValue();
        mDatabase.child(Constants.DATABASE_PATH_ALL_UPLOADS).child(sock.getmIdChaussette()).removeValue();

        if(sock.ismReported()){
            mDatabase= FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_REPORTS);
            mDatabase.child(sock.getmIdChaussette()).removeValue();
        }
        if(category!=""){
            mDatabase= FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_SOCKS);
            mDatabase.child(Constants.DATABASE_PATH_CATEGORY).child(category).child(sock.getmIdChaussette()).removeValue();
        }
        StorageReference mStorageRef= FirebaseStorage.getInstance().getReference();
        StorageReference desertRef = mStorageRef.child(Constants.STORAGE_PATH_UPLOADS).child(sock.getmIdChaussette());
        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
            }
        });
    }

}

