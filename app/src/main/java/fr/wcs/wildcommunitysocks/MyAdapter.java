package fr.wcs.wildcommunitysocks;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by wilder on 29/03/17.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private Context context;
    private List<Chaussette> chaussettes;

    public MyAdapter(Context context, List<Chaussette> uploads) {
        this.chaussettes = uploads;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sock_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Chaussette sock = chaussettes.get(position);

        holder.textViewName.setText(sock.getmLegende());

        Glide.with(context).load(sock.getmImgChaussette()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return chaussettes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewName;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewName = (TextView) itemView.findViewById(R.id.sockLegend);
            imageView = (ImageView) itemView.findViewById(R.id.showSock);
        }
    }

}
