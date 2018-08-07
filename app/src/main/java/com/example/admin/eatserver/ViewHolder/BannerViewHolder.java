package com.example.admin.eatserver.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.eatserver.Common.Common;
import com.example.admin.eatserver.Interface.ItemClickListener;
import com.example.admin.eatserver.R;

public class BannerViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener
{

    public TextView txtbannerName;
    public ImageView bannerimageView;


    public BannerViewHolder(View itemView)
    {
        super(itemView);

        txtbannerName =(TextView)itemView.findViewById( R.id.banner_name);
        bannerimageView =(ImageView) itemView.findViewById(R.id.banner_image);
        itemView.setOnCreateContextMenuListener( this );
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.setHeaderTitle( "Select the Action" );
        contextMenu.add( 0,0,getAdapterPosition(), Common.UPDATE);
        contextMenu.add( 0,1,getAdapterPosition(), Common.DELETE);
    }
}
