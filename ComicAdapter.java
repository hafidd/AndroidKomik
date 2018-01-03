package com.gmail.hafid.projekuas;

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

/**
 * Created by PRET-5 on 30/12/2017.
 */

class ComicAdapter extends RecyclerView.Adapter<ComicAdapter.MyViewHolder> {
    // struktur data
    private ArrayList<ComicModel> comicModels;

    public ComicAdapter(ArrayList<ComicModel> comicModels) {
        this.comicModels = comicModels;
    }

    @Override
    public ComicAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.comic_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ComicAdapter.MyViewHolder holder, int position) {
        // update view
        holder.title.setText(comicModels.get(position).getTitle());
        holder.author.setText(comicModels.get(position).getAuthor());

        final Context context = holder.img.getContext();
        // gambar
        Picasso.with(context)
                .load(comicModels.get(position).getImg())
                .error(R.drawable.default_img)
                .into(holder.img);
        // click
        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                //Toast.makeText(context, "#" + position + " - " + position, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(context, DetailActivity.class);
                i.putExtra("c_id", comicModels.get(position).getId());
                i.putExtra("title", comicModels.get(position).getTitle());
                i.putExtra("author", comicModels.get(position).getAuthor());
                i.putExtra("img", comicModels.get(position).getImg());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return comicModels.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // vars
        private TextView title;
        private ImageView img;
        private TextView author;
        ItemClickListener clickListener;

        MyViewHolder(View view) {
            super(view);

            title = (TextView) view.findViewById(R.id.text_comic_title);
            img = (ImageView) view.findViewById(R.id.img_comic);
            author = (TextView) view.findViewById(R.id.text_comic_author);
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
