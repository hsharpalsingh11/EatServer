package com.example.admin.eatserver.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.admin.eatserver.Interface.ItemClickListener;
import com.example.admin.eatserver.R;

import java.io.BufferedReader;

public class OrderViewHolder extends RecyclerView.ViewHolder

{
    public TextView txtOrderId, txtOrderStatus,txtOrderPhone,txtOrderAddress,txtDate;
    public Button btnEdit,btnRemove,btnTrack,btnDetail;

    public OrderViewHolder(View itemView) {
        super(itemView);
        txtOrderAddress =(TextView)itemView.findViewById(R.id.order_address);
        txtOrderId =(TextView)itemView.findViewById(R.id.order_id);
        txtOrderStatus =(TextView)itemView.findViewById(R.id.order_status);
        txtOrderPhone =(TextView)itemView.findViewById(R.id.order_phone);
        txtDate = (TextView)itemView.findViewById( R.id.order_date );
        btnEdit = (Button)itemView.findViewById( R.id.btnEdit );
        btnRemove = (Button)itemView.findViewById( R.id.btnRemove );
        btnDetail = (Button)itemView.findViewById( R.id.btnDetail );
        btnTrack = (Button)itemView.findViewById( R.id.btnTrack );
    }
}
