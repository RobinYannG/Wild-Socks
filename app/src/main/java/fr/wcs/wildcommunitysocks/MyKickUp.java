package fr.wcs.wildcommunitysocks;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MyKickUp extends Fragment {
    public static MyKickUp newInstance() {
        MyKickUp fragment = new MyKickUp();
        return fragment;
    }
    //recyclerview object
    private RecyclerView rView;

    private GridLayoutManager lLayout;

    //adapter object
    AdapterFlux rcAdapter;


    //Firebase references
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;


    //progress dialog
    private ProgressDialog progressDialog;

    //list to hold all the uploaded images
    private List<Chaussette> rowListItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_flux, container, false);

        mAuth=FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();

        final List<Chaussette> rowListItem = getAllItemList();
        lLayout = new GridLayoutManager(getActivity(), 3);

        rView = (RecyclerView) view.findViewById(R.id.recyclerView2);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(lLayout);

        progressDialog = new ProgressDialog(getActivity());



        rcAdapter = new AdapterFlux(getActivity(), rowListItem);
        rView.setAdapter(rcAdapter);

        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_SOCKS);
        Query query = mDatabase.child(user.getUid()).child(Constants.DATABASE_PATH_MYKICKS);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                rowListItem.clear();

                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    Chaussette sock = postSnapshot.getValue(Chaussette.class);

                    rowListItem.add(sock);
                    rcAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });

        rView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), rView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        int itemPosition = rView.getChildLayoutPosition(view);
                        Chaussette item = rowListItem.get(itemPosition);
                        Intent intent = new Intent(getActivity(), SocksActivity.class);

                        intent.putExtra("sock",item);
                        intent.putExtra("position",itemPosition);

                        startActivity(intent);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        int itemPosition = rView.getChildLayoutPosition(view);
                        final Chaussette item = rowListItem.get(itemPosition);

                        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Es-tu sûre ?")
                                .setContentText("Tu veux supprimer ton Kick Up ?")
                                .setCancelText("Non")
                                .setConfirmText("Yes")
                                .showCancelButton(true)
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        // reuse previous dialog instance, keep widget user state, reset them if you need
                                        sDialog.setTitleText("Super choix !")
                                                .setContentText("Cette photo mérite vraiment ton Kick Up.")
                                                .setConfirmText("Continuer")
                                                .showCancelButton(false)
                                                .setCancelClickListener(null)
                                                .setConfirmClickListener(null)
                                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                    }
                                })
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {

                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.setTitleText("Suppression !")
                                                .setContentText("Tu as supprimer ce Kick Up.")
                                                .setConfirmText("Continuer")
                                                .showCancelButton(false)
                                                .setCancelClickListener(null)
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                                                        mDatabase.child(mAuth.getCurrentUser().getUid()).child(Constants.DATABASE_PATH_MYKICKS).child(item.getmIdChaussette()).removeValue();

                                                    }
                                                })
                                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                                    }

                                })
                                .show();
                    }
                })
        );

        return view;
    }

    private List<Chaussette> getAllItemList(){

        List<Chaussette> allItems = new ArrayList<>();


        return allItems;
    }
}