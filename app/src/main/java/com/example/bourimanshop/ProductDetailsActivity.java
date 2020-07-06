package com.example.bourimanshop;

import java.util.Calendar;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bourimanshop.Prevalent.Prevalent;
import com.example.bourimanshop.Model.Products;
import com.example.ebm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {
    private String productId = "",state= "Normal";
   private TextView productDescription, productName, productPrice,quantity;
    private ImageView productImage;
public int num;
private  Button btnajouterpanier;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_product_details);
            quantity = findViewById(R.id.Product_quantity);
            productId = getIntent().getStringExtra("pid");
            productImage = findViewById(R.id.product_image_details);
            productDescription = findViewById(R.id.product_description_details);
            productName = findViewById(R.id.product_name_details);
            btnajouterpanier = findViewById(R.id.pd_add_to_cart_button);
            productPrice = findViewById(R.id.product_price_details);
            btnajouterpanier.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(state.equals("Order Placed")|state.equals("Order Shipped")){
                        Toast.makeText(ProductDetailsActivity.this,"vous pouvez acheter plus de produits une fois votre commande est expédiée ou confirmée",Toast.LENGTH_LONG).show();
                    }
                    else {
                        addingToCartList();
                    }
                }
            });
            getProductDetails(productId);
          num = Integer.valueOf(quantity.getText().toString());
        }catch(Exception e){
            e.printStackTrace();
        }



    }
    public void Plus(View v){
num=num+1;
quantity.setText(Integer.toString(num));
    }
public void Moins(View v ){
if(num>1)
{
    num= num-1;
    quantity.setText(Integer.toString(num));
}
}

    @Override
    protected void onStart() {
        super.onStart();
        CheckOrdersState();
    }

    public void addingToCartList(){


        String saveCurrentTime, saveCurrentDate;
        Calendar CalForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(CalForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime= currentTime.format(CalForDate.getTime());
        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
       final HashMap<String,Object> cartMap = new HashMap<>();
       cartMap.put("pid",productId);
        cartMap.put("pname",productName.getText().toString());
        cartMap.put("price",productPrice.getText().toString());
        cartMap.put("date",saveCurrentDate);
        cartMap.put("time",saveCurrentTime);
        cartMap.put("quantity",Integer.toString(num));
        cartMap.put("discount","");
        cartListRef.child("User View").child(Prevalent.currentOnlineUser.getPhone()).child("Products").child(productId).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    cartListRef.child("Admin View").child(Prevalent.currentOnlineUser.getPhone()).child("Products").child(productId).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
if(task.isSuccessful()){
    Toast.makeText(ProductDetailsActivity.this,"Ajouté au panier avec succès",Toast.LENGTH_SHORT);
    Intent i = new Intent(ProductDetailsActivity.this,HomeActivity.class);
    startActivity(i);
}
                        }
                    });
                }
            }
        });

    }
    private void getProductDetails(final String productId) {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Products");
        productRef.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Products products = dataSnapshot.getValue(Products.class);
                    productName.setText(products.getPname());
                    productPrice.setText(products.getPrice());
                  productDescription.setText(products.getDescription());
                    Picasso.get().load(products.getImage()).into(productImage);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void CheckOrdersState(){
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone());
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    String shippingState = dataSnapshot.child("state").getValue().toString();

                    if (shippingState.equals("shipped")){

state ="Order Shipped";
                    }
                    else if (shippingState.equals("not shipped")){

                        state ="Order Placed";
                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
