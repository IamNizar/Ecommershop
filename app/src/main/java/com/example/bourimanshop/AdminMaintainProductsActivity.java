package com.example.bourimanshop;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ebm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainProductsActivity extends AppCompatActivity {

    private Button applyChangesBtn;
    private EditText name,description,price;
    private ImageView imageView;
    private String productID="";
    private DatabaseReference productsRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productID = getIntent().getStringExtra("pid");
        productsRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productID);
        setContentView(R.layout.activity_admin_maintain_products);
      applyChangesBtn =findViewById(R.id.apply_changes_btn);
      name =findViewById(R.id.product_name_maintain);
      description=findViewById(R.id.product_description_maintain);
      price=findViewById(R.id.product_price_maintain);
      imageView =findViewById(R.id.product_image_maintain);
      DisplayproductInfo();
      applyChangesBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              applyChanges();
          }
      });
    }

    private void applyChanges() {
        String pName = name.getText().toString();
        String pPrice = price.getText().toString();
        String pDescription= description.getText().toString();
        if(pName.equals("")){
            Toast.makeText(AdminMaintainProductsActivity.this,"Veuillez remplir le champ de la designation",Toast.LENGTH_SHORT).show();
        }else if(pPrice.equals("")){
            Toast.makeText(AdminMaintainProductsActivity.this,"Veuillez saisir le prix",Toast.LENGTH_SHORT).show();

        }else if(pDescription.equals("")){
            Toast.makeText(AdminMaintainProductsActivity.this,"Veuillez saisir la description",Toast.LENGTH_SHORT).show();

        }else{
            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put("pid", productID);
            productMap.put("description", pDescription);
            productMap.put("price", pPrice);
            productMap.put("pname", pName);
            productsRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(AdminMaintainProductsActivity.this,"Modification appliqué avec succès",Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(AdminMaintainProductsActivity.this,AdminCategoryActivity.class);
                        startActivity(i);
                        finish();
                    }
                }
            });
        }
    }

    private void DisplayproductInfo() {
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String pName = dataSnapshot.child("pname").getValue().toString();
                    String pPrice = dataSnapshot.child("price").getValue().toString();
                    String pDescription = dataSnapshot.child("description").getValue().toString();
                    String pImage = dataSnapshot.child("image").getValue().toString();
                    name.setText(pName);
                    description.setText(pDescription);
                    price.setText(pPrice);
                    Picasso.get().load(pImage).into(imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void deleteProduct(View v){
        productsRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
               if(task.isSuccessful()){
                   Toast.makeText(AdminMaintainProductsActivity.this,"Le produit est supprimé avec succès",Toast.LENGTH_SHORT).show();
                  Intent intent =new Intent(AdminMaintainProductsActivity.this,AdminCategoryActivity.class);
                  startActivity(intent);
                   finish();
               }
            }
        });
    }
}
