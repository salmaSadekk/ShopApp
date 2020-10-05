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

public  class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {
  ArrayList <product> exampleList ;
  String TAG = "Adapter" ;
  public static class ExampleViewHolder extends RecyclerView.ViewHolder {
    ImageView ImageView ;
    TextView TextView1 ;
    TextView TextView2 ;


    public ExampleViewHolder(View itemView) {
      super(itemView) ;
      ImageView = itemView.findViewById(R.id.images) ;
      TextView1 = itemView.findViewById(R.id.T1) ;
      TextView2 = itemView.findViewById(R.id.T2) ;

    }
  }
  public ExampleAdapter(ArrayList<product> list){
  exampleList = list ;
  }

  @NonNull
  @Override
  public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_item , parent,false);
    ExampleViewHolder evh = new ExampleViewHolder(v) ;
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
