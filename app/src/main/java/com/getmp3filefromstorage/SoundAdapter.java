package com.getmp3filefromstorage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SoundAdapter extends RecyclerView.Adapter<SoundAdapter.Holder> {
    public List<SoundModel> objList = null;
    private Context context = null;

    public SoundAdapter(Context context) {
        this.context = context;
        objList = new ArrayList<>();

    }

    @Override
    public Holder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);

        Holder viewHolder = new Holder(v);
        return viewHolder;
    }

    public void addData(ArrayList<SoundModel> mobjList) {
        objList = new ArrayList<>();
        if (mobjList != null && mobjList.size() > 0)
            objList.addAll(mobjList);

        this.notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position)
    {

        holder.txtTitle.setText(objList.get(position).title);
        holder.txtArtist.setText(objList.get(position).artist);
        holder.txtLocation.setText(objList.get(position).location);

        holder.btnPlay.setImageResource(objList.get(position).isPlaying ? R.mipmap.pause : R.mipmap.play);
        holder.btnPlay.setTag(R.string.app_name, position);
        holder.btnPlay.setOnClickListener((View.OnClickListener) context);



    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return objList != null ? objList.size() : 0;
    }

    static class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.txtTitle)
        TextView txtTitle;
        @BindView(R.id.txtArtist)
        TextView txtArtist;
        @BindView(R.id.txtLocation)
        TextView txtLocation;
        @BindView(R.id.btnPlay)
        ImageView btnPlay;

        Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
