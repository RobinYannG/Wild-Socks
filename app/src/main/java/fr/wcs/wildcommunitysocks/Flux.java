package fr.wcs.wildcommunitysocks;

import android.app.ProgressDialog;
import android.os.Bundle;

import android.support.v4.app.Fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


    public class Flux extends Fragment {
        public static fr.wcs.wildcommunitysocks.Flux newInstance() {
            fr.wcs.wildcommunitysocks.Flux fragment = new fr.wcs.wildcommunitysocks.Flux();
            return fragment;
        }
        //recyclerview object
        private RecyclerView recyclerView;

        private GridLayoutManager lLayout;

        //adapter object
        private RecyclerView.Adapter adapter;

        //database reference
        private DatabaseReference mDatabase;

        //progress dialog
        private ProgressDialog progressDialog;

        //list to hold all the uploaded images
        private List<Chaussette> uploads;

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


            List<Chaussette> rowListItem = getAllItemList();
            lLayout = new GridLayoutManager(getActivity(), 3);

            RecyclerView rView = (RecyclerView) view.findViewById(R.id.recyclerView2);
            rView.setHasFixedSize(true);
            rView.setLayoutManager(lLayout);

            progressDialog = new ProgressDialog(getActivity());

            AdapterFlux rcAdapter = new AdapterFlux(getActivity(), rowListItem);
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

        /*

        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();

                for(DataSnapshot postSnapshot: )

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        */





            return view;
        }

        private List<Chaussette> getAllItemList(){

            List<Chaussette> allItems = new ArrayList<Chaussette>();
            allItems.add(new Chaussette("bla", "Blabla", "Blablabla"));
            allItems.add(new Chaussette("bla", "Blabla", "Blablabla"));
            allItems.add(new Chaussette("bla", "Bluuuuu", "Blablabla"));
            allItems.add(new Chaussette("bla", "Blabla", "Blablabla"));
            allItems.add(new Chaussette("bla", "Blabla", "Blablabla"));
            allItems.add(new Chaussette("bla", "Blabla", "Blablabla"));

            return allItems;
        }

}