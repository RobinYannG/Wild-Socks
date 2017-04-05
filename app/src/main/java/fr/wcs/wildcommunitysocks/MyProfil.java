package fr.wcs.wildcommunitysocks;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class MyProfil extends Fragment implements View.OnClickListener {

    public static MyProfil newInstance() {
        MyProfil fragment = new MyProfil();
        return fragment;
    }

    //recyclerview object
    private RecyclerView rView;

    private GridLayoutManager lLayout;

    //adapter object
    AdapterFlux rcAdapter;

    //database reference
    private DatabaseReference mDatabase;

    //progress dialog
    private ProgressDialog progressDialog;

    //list to hold all the uploaded images
    private List<Chaussette> rowListItem;

    private TextView textViewDisplayName;
    private Button buttonModifyProfil;

    private FirebaseAuth firebaseAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_profil, container, false);


        textViewDisplayName = (TextView) view.findViewById(R.id.textViewDisplayName);
        buttonModifyProfil = (Button) view.findViewById(R.id.buttonModifyProfil);
        buttonModifyProfil.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        // Display name User
        textViewDisplayName.setText(user.getDisplayName());

        final List<Chaussette> rowListItem = getAllItemList();
        lLayout = new GridLayoutManager(getActivity(), 3);

        rView = (RecyclerView) view.findViewById(R.id.recyclerViewMyProfile);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(lLayout);

        //progressDialog = new ProgressDialog(getActivity());

        rcAdapter = new AdapterFlux(getActivity(), rowListItem);
        rView.setAdapter(rcAdapter);

        //progressDialog.setMessage("Please wait...");
        //progressDialog.show();


        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_SOCKS);
        Query query = mDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //progressDialog.dismiss();
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    Chaussette sock = postSnapshot.getValue(Chaussette.class);
                    rowListItem.add(sock);
                    rcAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //progressDialog.dismiss();
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    Chaussette sock = postSnapshot.getValue(Chaussette.class);
                    rowListItem.add(sock);
                    rcAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    Chaussette sock = postSnapshot.getValue(Chaussette.class);
                    rowListItem.remove(sock);
                    rcAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
        /**
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
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
**/


        return view;


    }
    private List<Chaussette> getAllItemList(){

        List<Chaussette> allItems = new ArrayList<>();


        return allItems;
    }

    @Override
    public void onClick(View v) {

        if(v == buttonModifyProfil) {
            startActivity(new Intent(getActivity(), ModifyProfil.class));

        }

    }
}