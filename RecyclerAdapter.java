package com.gmail.hafid.projekuas;

/**
 * Created by PRET-5 on 21/12/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private ArrayList<RecyclerModel> recyclerModels; // this data structure carries our title and description

    public RecyclerAdapter(ArrayList<RecyclerModel> recyclerModels) {
        this.recyclerModels = recyclerModels;
    }

    @Override
    public RecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate your custom row layout here
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.MyViewHolder holder, int position) {
        // update your data here

        holder.title.setText(recyclerModels.get(position).getTitle());
        holder.description.setText(recyclerModels.get(position).getDescription());
        // idk
        final Context context = holder.pic.getContext();
        // gambar
        Picasso.with(context)
                .load(recyclerModels.get(position).getImg())
                .error(R.drawable.default_img)
                .into(holder.pic);
        // click
        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                // Toast.makeText(context, "#" + position + " - " + position , Toast.LENGTH_SHORT).show();
                Intent i = new Intent(context, ReadActivity.class);
                i.putExtra("c_id", recyclerModels.get(position).getId());
                i.putExtra("komik_id", recyclerModels.get(position).getKomikId());
                i.putExtra("chapter", recyclerModels.get(position).getChapter());
                i.putExtra("title", recyclerModels.get(position).getTitle());
                i.putExtra("pages", recyclerModels.get(position).getPages());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recyclerModels.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // view this our custom row layout, so intialize your variables here
        private TextView title;
        private TextView description;
        private ImageView pic;
        ItemClickListener clickListener;

        MyViewHolder(View view) {
            super(view);

            title = (TextView) view.findViewById(R.id.title);
            description = (TextView) view.findViewById(R.id.description);
            pic = (ImageView) view.findViewById(R.id.pic);
            view.setTag(view);
            view.setOnClickListener(this);
        }

        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }

        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
        }

    }


}
