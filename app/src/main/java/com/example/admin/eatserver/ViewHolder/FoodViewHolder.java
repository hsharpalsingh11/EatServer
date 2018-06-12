package com.example.admin.eatserver.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.eatserver.Common.Common;
import com.example.admin.eatserver.Interface.ItemClickListener;
import com.example.admin.eatserver.R;

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener
{

    public TextView txtFoodName;
    public ImageView FoodimageView;

    private ItemClickListener itemClickListener;

    public FoodViewHolder(View itemView)
    {
        super(itemView);

        txtFoodName =(TextView)itemView.findViewById( R.id.food_name);
        FoodimageView =(ImageView) itemView.findViewById(R.id.food_image);
        itemView.setOnCreateContextMenuListener( this );
        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v)
    {
        itemClickListener.onClick(v,getAdapterPosition(),false);

    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.setHeaderTitle( "Select the Action" );
        contextMenu.add( 0,0,getAdapterPosition(), Common.UPDATE);
        contextMenu.add( 0,1,getAdapterPosition(), Common.DELETE);
    }
}

