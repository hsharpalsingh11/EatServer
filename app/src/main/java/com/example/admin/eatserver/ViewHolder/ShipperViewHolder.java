package com.example.admin.eatserver.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.admin.eatserver.Interface.ItemClickListener;
import com.example.admin.eatserver.R;

import org.w3c.dom.Text;

public class ShipperViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView shipper_name, shipper_phone;
    public Button btn_edit, btn_remove;
    private ItemClickListener itemClickListener;


    public ShipperViewHolder(View itemView)
    {
        super(itemView);

        shipper_name = (TextView)itemView.findViewById(R.id.shipper_name);
        shipper_phone = (TextView)itemView.findViewById(R.id.shipper_pone);
        btn_edit = (Button) itemView.findViewById(R.id.btnEdit);
        btn_remove = (Button) itemView.findViewById(R.id.btnRemove);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v)
    {

        itemClickListener.onClick(v,getAdapterPosition(),false);
    }
}
