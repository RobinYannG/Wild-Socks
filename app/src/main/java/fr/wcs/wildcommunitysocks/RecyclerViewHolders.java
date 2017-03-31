package fr.wcs.wildcommunitysocks;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by robingoudy on 30/03/2017.
 */

public class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView textViewName;
    public ImageView imageView;

    public RecyclerViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        textViewName = (TextView) itemView.findViewById(R.id.sockLegend);
        imageView = (ImageView)itemView.findViewById(R.id.sockImage);
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(v.getContext(), "Clicked sock Position = " + getPosition(), Toast.LENGTH_SHORT);
    }

    /*
    @Override
    public void onClick(View view) {
        ow();
    }  */
}