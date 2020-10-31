package com.example.myapplication;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Models.shopItem;

import java.util.ArrayList;

public class shopsAdapter extends RecyclerView.Adapter{
  ArrayList<shopItem> arr ;
  public interface OnShopItemClicked{

  }
  public shopsAdapter ( ArrayList<shopItem> arr ){
    this.arr = arr ;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return null;
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

  }

  @Override
  public int getItemCount() {
    return 0;
  }
}
