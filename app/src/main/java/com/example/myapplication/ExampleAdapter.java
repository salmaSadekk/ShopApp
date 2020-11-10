package com.example.myapplication;


import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Models.product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public  class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder>  {
  ArrayList <product> exampleList ;
  OnItemClicked mListener ;
  String TAG = "Adapter" ;

  public interface OnItemClicked {
    void onItemClick(int position) ;
  }
  public ExampleAdapter(ArrayList<product> list , OnItemClicked listener){
    exampleList = list ;
    this.mListener =listener ;
  }


  public static class ExampleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    ImageView ImageView ;
    TextView TextView1 ;
    TextView TextView2 ;
    OnItemClicked onItemClicked ;

    public ExampleViewHolder(View itemView , OnItemClicked onItemClicked) {
      super(itemView) ;
      ImageView = itemView.findViewById(R.id.images) ;
      TextView1 = itemView.findViewById(R.id.T1) ;
      TextView2 = itemView.findViewById(R.id.T2) ;
      this.onItemClicked = onItemClicked ;
      itemView.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
      if(onItemClicked ==null) {
        return;
      }
      onItemClicked.onItemClick(getAdapterPosition());
    }


  }


  @NonNull
  @Override
  public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_item , parent,false);


    ExampleViewHolder evh = new ExampleViewHolder(v , mListener) ;
    return evh ;
  }

  @Override
  public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
    product currentItem = exampleList.get(position) ;
    //Picasso.get().load(Uri.parse(((product)currentItem).getImage_url())).into(holder.ImageView);
    Log.d(TAG ,"getName output" + currentItem.getImage_url()) ;
    Picasso.get().load(currentItem.getImage_url()).into(holder.ImageView);
    Log.d(TAG ,"getName output" + currentItem.getName()) ;
    holder.TextView1.setText(currentItem.getName());
    Log.d(TAG , "getDesc output" +currentItem.getDesc()) ;
    holder.TextView2.setText(currentItem.getDesc());

   // holder.ImageView.setImageResource();
  }

  @Override
  public int getItemCount() {
    return exampleList.size();
  }
}
