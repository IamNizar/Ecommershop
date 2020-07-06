package com.example.bourimanshop.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bourimanshop.Interface.ItemClickListner;
import com.example.ebm.R;

public class ProductViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener
 {
    public TextView txtProductName, txtProductDescription, txtProductPrice;
    public ImageView imageView;
    public ItemClickListner listner;


    public ProductViewHolder(View itemView)
    {
        super(itemView);


        imageView =  itemView.findViewById(R.id.product_image);
        txtProductName =  itemView.findViewById(R.id.product_name);
        txtProductDescription =  itemView.findViewById(R.id.product_description);
        txtProductPrice = itemView.findViewById(R.id.product_price);
    }

    public void setItemClickListner(ItemClickListner listner)
    {
        this.listner = listner;
    }

    @Override
    public void onClick(View view)
    {
        listner.onClick(view, getAdapterPosition(), false);
    }
}
