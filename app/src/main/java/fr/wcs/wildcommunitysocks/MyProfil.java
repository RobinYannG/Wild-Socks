package fr.wcs.wildcommunitysocks;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MyProfil extends Fragment {
    public static MyProfil newInstance() {
        MyProfil fragment = new MyProfil();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_flux, container, false);
/**
        GridView gridview = (GridView) view.findViewById(R.id.GridViewFlux);
        gridview.setAdapter(new ImageAdapter(getActivity()));
**/

      /*  gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), #####.class);
                startActivity(intent);


            }
        });  */



        return view;


    }
}