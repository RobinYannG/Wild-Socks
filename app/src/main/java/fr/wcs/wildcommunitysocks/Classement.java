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

    private Button buttonAllRanking;
    private Button buttonCategory1;
    private Button buttonCategory2;
    private Button buttonCategory3;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(android.view.LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_classement, container, false);

        buttonAllRanking = (Button) view.findViewById(R.id.buttonAllRanking);
        buttonCategory1 = (Button) view.findViewById(R.id.buttonCategory1);
        buttonCategory2 = (Button) view.findViewById(R.id.buttonCategory2);
        buttonCategory3 = (Button) view.findViewById(R.id.buttonCategory3);

        buttonAllRanking.setOnClickListener(this);
        buttonCategory1.setOnClickListener(this);
        buttonCategory2.setOnClickListener(this);
        buttonCategory3.setOnClickListener(this);

        return view;
    }

    public void onClick(View v) {
        if (v == buttonAllRanking){
            Intent intent = new Intent(getActivity(), RankingResult.class);
            intent.putExtra("I_CAME_FROM", "a0");
            startActivity(intent);
        }
        if (v == buttonCategory1){
            Intent intent = new Intent(getActivity(), RankingResult.class);
            intent.putExtra("I_CAME_FROM", "a1");
            startActivity(intent);
        }
        if (v == buttonCategory2){
            Intent intent = new Intent(getActivity(), RankingResult.class);
            intent.putExtra("I_CAME_FROM", "a2");
            startActivity(intent);
        }
        if (v == buttonCategory3){
            Intent intent = new Intent(getActivity(), RankingResult.class);
            intent.putExtra("I_CAME_FROM", "a3");
            startActivity(intent);
        }
    }




}