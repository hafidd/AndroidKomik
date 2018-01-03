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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.MyViewHolder> {

    private ArrayList<RecyclerModel> recyclerModels; // this data structure carries our title and description

    public ChapterAdapter(ArrayList<RecyclerModel> recyclerModels) {
        this.recyclerModels = recyclerModels;
    }

    @Override
    public ChapterAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate your custom row layout here
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chapter_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ChapterAdapter.MyViewHolder holder, int position) {
        // update your data here
        holder.description.setText(recyclerModels.get(position).getDescription());
        final Context context = holder.description.getContext();
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
        private TextView description;
        ItemClickListener clickListener;

        MyViewHolder(View view) {
            super(view);
            description = (TextView) view.findViewById(R.id.chapter);
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
