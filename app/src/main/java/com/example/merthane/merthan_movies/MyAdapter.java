package com.example.merthane.merthan_movies;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by MerthanE on 16.02.2018.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    String[] data=new String[0];
    LayoutInflater inflater;
    CardClickListener cardClickListener =null;
    Context context;
    Movie[] movieArray;

    MyAdapter(Context context,String[] data){
        inflater=LayoutInflater.from(context);
        this.context=context;
        this.data=data;
    }
    public void setMovieArray(Movie[] movieArray){
        this.movieArray=movieArray;
    }

    public void setData(String[] data) {
        this.data = data;
    }

    @Override public int getItemCount() {return data.length;}

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder( inflater.inflate(R.layout.r_view_item,parent,false) );
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        try{
            Picasso.with(context).load(data[position]).into(holder.card);
        }catch (Exception E){
            holder.card.setImageDrawable(new ColorDrawable(Color.TRANSPARENT));//Error view
        }


    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView card;

        MyViewHolder(View itemView) {
            super(itemView);
            card= itemView.findViewById(R.id.card);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(cardClickListener!=null)
                cardClickListener.onItemClick(view,getAdapterPosition(),movieArray);
        }
    }

    public void setCardClickListener(CardClickListener cardClickListener){
        this.cardClickListener=cardClickListener;
    }

    public interface CardClickListener{
        void onItemClick(View view,int position,Movie[] movieArray);
    }

}
