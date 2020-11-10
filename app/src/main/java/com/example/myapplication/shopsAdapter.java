package com.example.myapplication;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Models.product;
import com.example.myapplication.Models.shopItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class shopsAdapter extends RecyclerView.Adapter<shopsAdapter.shopsAdapterHolder>{
  ArrayList<shopItem> arr ;

  OnShopItemClicked onShopItemClicked ;
  public interface OnShopItemClicked{
   void OnShopItemClick( int position) ;
  }
  public shopsAdapter ( ArrayList<shopItem> arr , OnShopItemClicked clickListener  ){
    this.arr = arr ;
    this.onShopItemClicked = clickListener ;
  }
  public static class shopsAdapterHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    TextView TextView1 ;
    TextView TextView2 ;
    TextView TextView3 ;
    OnShopItemClicked onShopItemClicked ;

    public shopsAdapterHolder(View itemView , OnShopItemClicked onShopItemClicked) {
    super(itemView) ;
      TextView1 = itemView.findViewById(R.id.T1) ;
      TextView2 = itemView.findViewById(R.id.T2) ;
      TextView3 = itemView.findViewById(R.id.T3) ;
      this.onShopItemClicked = onShopItemClicked ;
      itemView.setOnClickListener(this) ;
    }


    @Override
    public void onClick(View v) {
      onShopItemClicked.OnShopItemClick(getAdapterPosition());

    }
  }

  @NonNull
  @Override
  public shopsAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopitem , parent,false);
    shopsAdapterHolder evh = new shopsAdapterHolder(v , onShopItemClicked) ;
    return evh ;
  }
  @Override
  public void  onBindViewHolder(@NonNull shopsAdapterHolder holder, int position) {
    shopItem currentItem = arr.get(position) ;
    holder.TextView1.setText(currentItem.getName());
    holder.TextView2.setText(String.valueOf(currentItem.getPrice()));
    holder.TextView3.setText(String.valueOf(currentItem.getDistance()));

  }

  @Override
  public int getItemCount() {
    return arr.size();
  }
}
