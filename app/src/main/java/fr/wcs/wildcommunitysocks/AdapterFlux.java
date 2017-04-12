package fr.wcs.wildcommunitysocks;

/**
 * Created by robingoudy on 30/03/2017.
 */
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;


public class AdapterFlux extends RecyclerView.Adapter<AdapterFlux.ViewHolder> {
    private Context context;
    private List<Chaussette> chaussettes;

    public AdapterFlux(Context context, List<Chaussette> uploads) {
        this.chaussettes = uploads;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sock_item, null);
        ViewHolder rcv = new ViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Chaussette sock = chaussettes.get(position);

//        holder.textViewName.setText(sock.getmLegende());

        Glide.with(context).load(sock.getmImgChaussette()).placeholder(R.mipmap.placeholdersocks).into(holder.imageView);
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
            imageView = (ImageView) itemView.findViewById(R.id.sockImage);
        }
    }

}