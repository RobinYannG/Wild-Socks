package fr.wcs.wildcommunitysocks;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Flux extends Fragment {
    public static fr.wcs.wildcommunitysocks.Flux newInstance() {
        fr.wcs.wildcommunitysocks.Flux fragment = new fr.wcs.wildcommunitysocks.Flux();
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(android.view.LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_flux, container, false);


            /* to delete?
            setContentView(R.layout.activity_navigation);
            setTitle(null);  */


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



        /** Delete ?
         @Override
         public boolean onCreateOptionsMenu(Menu menu) {
         // Inflate the menu; this adds items to the action bar if it is present.
         getMenuInflater().inflate(R.menu.menu_main, menu);
         return true;
         }
         @Override
         public boolean onOptionsItemSelected(MenuItem item) {
         // Handle action bar item clicks here. The action bar will
         // automatically handle clicks on the Home/Up button, so long
         // as you specify a parent activity in AndroidManifest.xml.
         int id = item.getItemId();
         //noinspection SimplifiableIfStatement
         if (id == R.id.action_settings) {
         return true;
         }
         if(id == R.id.action_refresh){
         Toast.makeText(Flux2.this, "Refresh App", Toast.LENGTH_LONG).show();
         }
         if(id == R.id.action_new){
         Toast.makeText(Flux2.this, "Create Text", Toast.LENGTH_LONG).show();
         }
         return super.onOptionsItemSelected(item);  */


        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);
        Query query = mDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
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






        return view;
    }

    private List<Chaussette> getAllItemList(){

        List<Chaussette> allItems = new ArrayList<>();


        return allItems;
    }
}