package fr.wcs.wildcommunitysocks;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.data;
import static fr.wcs.wildcommunitysocks.Constants.DATABASE_PATH_CATEGORY_1INT;
import static fr.wcs.wildcommunitysocks.Constants.DATABASE_PATH_CATEGORY_2INT;
import static fr.wcs.wildcommunitysocks.Constants.DATABASE_PATH_CATEGORY_3INT;
import static fr.wcs.wildcommunitysocks.Constants.DATABASE_PATH_NO_CATEGORY;

public class RankingResult extends AppCompatActivity {

    private RecyclerView rView;

    private GridLayoutManager lLayout;

    //adapter object
    MyAdapter rcAdapter;

    //database reference
    private DatabaseReference mDatabase;


    //progress dialog
    private ProgressDialog progressDialog;

    //list to hold all the uploaded images
    private List<Chaussette> rowListItem;

    private Intent intent;
    private Query query;
    private ImageView imageViewTongs;
    private ImageView imageViewSlipers;
    private ImageView imageViewSocks;
    private ImageView imageViewGeneral;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_result);

        final List<Chaussette> rowListItem = getAllItemList();
        lLayout = new GridLayoutManager(RankingResult.this, 3);

        rView = (RecyclerView) findViewById(R.id.recyclerViewClassement);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(lLayout);

        progressDialog = new ProgressDialog(RankingResult.this);

        rcAdapter = new MyAdapter(RankingResult.this, rowListItem);
        rView.setAdapter(rcAdapter);

        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        imageViewSlipers = (ImageView) findViewById(R.id.imageViewSlipers);
        imageViewSocks = (ImageView) findViewById(R.id.imageViewSocks);
        imageViewTongs = (ImageView) findViewById(R.id.imageViewTongs);
        imageViewGeneral = (ImageView) findViewById(R.id.imageViewGeneral);

        imageViewSlipers.setVisibility(View.INVISIBLE);
        imageViewSocks.setVisibility(View.INVISIBLE);
        imageViewTongs.setVisibility(View.INVISIBLE);
        imageViewGeneral.setVisibility(View.INVISIBLE);


        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_SOCKS);

        intent = getIntent();
        String flag = intent.getStringExtra("I_CAME_FROM");

        if(flag.equals("a0")){
            query = mDatabase.child(Constants.DATABASE_PATH_ALL_UPLOADS).orderByChild("mSubNote");
            imageViewGeneral.setVisibility(View.VISIBLE);

        }
        if(flag.equals("a1")){
            query = mDatabase.child(Constants.DATABASE_PATH_CATEGORY).child(Constants.DATABASE_PATH_CATEGORY_1).orderByChild("mSubNote");
            imageViewSocks.setVisibility(View.VISIBLE);
        }
        if(flag.equals("a2")){
            query = mDatabase.child(Constants.DATABASE_PATH_CATEGORY).child(Constants.DATABASE_PATH_CATEGORY_2).orderByChild("mSubNote");
            imageViewSlipers.setVisibility(View.VISIBLE);
        }
        if(flag.equals("a3")){
            query = mDatabase.child(Constants.DATABASE_PATH_CATEGORY).child(Constants.DATABASE_PATH_CATEGORY_3).orderByChild("mSubNote");
            imageViewTongs.setVisibility(View.VISIBLE);
        }



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
                new RecyclerItemClickListener(RankingResult.this, rView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                        int itemPosition = rView.getChildLayoutPosition(view);
                        Chaussette item = rowListItem.get(itemPosition);
                        Intent intent = new Intent(RankingResult.this, SocksActivity.class);

                        intent.putExtra("sock",item);
                        intent.putExtra("position",itemPosition);


                        startActivity(intent);



                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );



    }


    private List<Chaussette> getAllItemList(){

        List<Chaussette> allItems = new ArrayList<>();


        return allItems;
    }
}
