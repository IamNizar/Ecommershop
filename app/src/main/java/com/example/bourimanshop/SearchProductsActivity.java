package com.example.bourimanshop;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.bourimanshop.Model.Products;
import com.example.bourimanshop.ViewHolder.ProductViewHolder;
import com.example.ebm.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SearchProductsActivity extends AppCompatActivity {

private ImageButton searchBtn;
private RecyclerView searchList;
private EditText inputText;
String SearchInput ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_products);
        searchBtn= findViewById(R.id.search_btn);
        searchList = findViewById(R.id.search_list);
        inputText = findViewById(R.id.search_product_name);
        searchList.setLayoutManager(new LinearLayoutManager(SearchProductsActivity.this));
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchInput = inputText.getText().toString();
                onStart();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Products");
        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>().setQuery(reference.orderByChild("pname").startAt(SearchInput),Products.class).build();
        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model) {



                holder.txtProductName.setText(model.getPname());
                holder.txtProductDescription.setText(model.getDescription());
                holder.txtProductPrice.setText("Prix = " + model.getPrice()+" DH");
                Picasso.get().load(model.getImage()).into(holder.imageView);
                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(SearchProductsActivity.this,ProductDetailsActivity.class);
                        i.putExtra("pid",model.getPid());
                        startActivity(i);
                        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                    }
                });




            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_items_layout, viewGroup, false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;
            }
        };
searchList.setAdapter(adapter);
adapter.startListening();
    }
}
