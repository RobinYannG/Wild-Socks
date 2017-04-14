package fr.wcs.wildcommunitysocks;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static fr.wcs.wildcommunitysocks.Constants.DATABASE_PATH_CATEGORY_1INT;
import static fr.wcs.wildcommunitysocks.Constants.DATABASE_PATH_CATEGORY_2INT;
import static fr.wcs.wildcommunitysocks.Constants.DATABASE_PATH_CATEGORY_3INT;
import static fr.wcs.wildcommunitysocks.Constants.DATABASE_PATH_NO_CATEGORY;

public class Classement extends Fragment implements View.OnClickListener {
    public static Classement newInstance() {
        Classement fragment = new Classement();
        return fragment;
    }

    private ImageView imageViewAllRanking;
    private ImageView imageViewCategory1;
    private ImageView imageViewCategory2;
    private ImageView imageViewCategory3;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(android.view.LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_classement, container, false);

        imageViewAllRanking = (ImageView) view.findViewById(R.id.imageViewAllRanking);
        imageViewCategory1 = (ImageView) view.findViewById(R.id.imageViewCategory1);
        imageViewCategory2 = (ImageView) view.findViewById(R.id.imageViewCategory2);
        imageViewCategory3 = (ImageView) view.findViewById(R.id.imageViewCategory3);


        imageViewAllRanking.setOnClickListener(this);
        imageViewCategory1.setOnClickListener(this);
        imageViewCategory2.setOnClickListener(this);
        imageViewCategory3.setOnClickListener(this);

        return view;
    }

    public void onClick(View v) {
        if (v == imageViewAllRanking){
            Intent intent = new Intent(getActivity(), RankingResult.class);
            intent.putExtra("I_CAME_FROM", "a0");
            startActivity(intent);
        }
        if (v == imageViewCategory1){
            Intent intent = new Intent(getActivity(), RankingResult.class);
            intent.putExtra("I_CAME_FROM", "a1");
            startActivity(intent);
        }
        if (v == imageViewCategory2){
            Intent intent = new Intent(getActivity(), RankingResult.class);
            intent.putExtra("I_CAME_FROM", "a2");
            startActivity(intent);
        }
        if (v == imageViewCategory3){
            Intent intent = new Intent(getActivity(), RankingResult.class);
            intent.putExtra("I_CAME_FROM", "a3");
            startActivity(intent);
        }
    }




}