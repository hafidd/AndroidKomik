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

class ReadAdapter extends RecyclerView.Adapter<ReadAdapter.MyViewHolder> {

    private ArrayList<ReadModel> readModel; // this data structure carries our title and description

    public ReadAdapter(ArrayList<ReadModel> readModel) {
        this.readModel = readModel;
    }

    @Override
    public ReadAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate your custom row layout here
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.read_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ReadAdapter.MyViewHolder holder, int position) {
        // update your data here
        holder.page.setText(readModel.get(position).getPage());
        // idk
        final Context context = holder.pic.getContext();
        // gambar
        Picasso.with(context)
                .load(readModel.get(position).getImg())
                .error(R.drawable.default_img)
                .into(holder.pic);
    }

    @Override
    public int getItemCount() {
        return readModel.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // view this our custom row layout, so intialize your variables here
        private TextView page;
        private ImageView pic;
        ItemClickListener clickListener;

        MyViewHolder(View view) {
            super(view);

            pic = (ImageView) view.findViewById(R.id.pic_ch);
            page = (TextView) view.findViewById(R.id.text_pages);
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
