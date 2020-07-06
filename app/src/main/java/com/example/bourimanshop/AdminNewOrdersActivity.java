package com.example.bourimanshop;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.bourimanshop.Model.AdminOrders;
import com.example.ebm.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminNewOrdersActivity extends AppCompatActivity {
private RecyclerView ordersList;
private DatabaseReference ordersRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        ordersList= findViewById(R.id.orders_list);
        ordersList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<AdminOrders> options = new FirebaseRecyclerOptions.Builder<AdminOrders>()
                .setQuery(ordersRef,AdminOrders.class).build();
        FirebaseRecyclerAdapter<AdminOrders,AdminOrdersViewHolder> adapter = new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminOrdersViewHolder holder, final int position, @NonNull final AdminOrders model) {
                holder.userName.setText("Client : " + model.getFullName());
                holder.userDateTime.setText("Commandé à : " + model.getDate()+" "+model.getTime());
                holder.userTotalPrice.setText("Prix Totale: " + model.getTotalAmout() +" DH");
                holder.userShippingAddress.setText("Adresse : " + model.getAdresse()+", "+model.getVille());
                holder.userPhoneNumber.setText("Télephone : " + model.getTelephone());
holder.ShowOrdersBtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        String uID = getRef(position).getKey();
        Intent intent = new Intent(AdminNewOrdersActivity.this,AdminUserProductsActivity.class);
        intent.putExtra("uid",uID );
        startActivity(intent);
    }
});
holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        CharSequence options[] = new CharSequence[]{
                "Oui",
                "Non"
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminNewOrdersActivity.this);
        builder.setTitle("Avez vous déja livré cette commande ?");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which ==0){
                    String uID = getRef(position).getKey();
                    RemoveOrder(uID);

                }else{
                    finish();
                }
            }
        });
        builder.show();
    }
});

            }

            @NonNull
            @Override
            public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.orders_layout,viewGroup,false);
                return new AdminOrdersViewHolder(view);
            }
        };
        ordersList.setAdapter(adapter);
        adapter.startListening();

    }



    public static class AdminOrdersViewHolder extends RecyclerView.ViewHolder{

        public TextView userName,userPhoneNumber,userTotalPrice,userDateTime,userShippingAddress;
        public Button ShowOrdersBtn;
        public AdminOrdersViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.order_userName);
            userDateTime = itemView.findViewById(R.id.order_date_time);
            userPhoneNumber = itemView.findViewById(R.id.order_phoneNumber);
            userShippingAddress = itemView.findViewById(R.id.order_shipping_Address);
            userTotalPrice = itemView.findViewById(R.id.order_price);
            ShowOrdersBtn = itemView.findViewById(R.id.show_all_products);


        }
    }
    private void RemoveOrder(String uID) {
        ordersRef.child(uID).removeValue();
    }
}
