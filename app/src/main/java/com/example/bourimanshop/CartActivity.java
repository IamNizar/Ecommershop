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
import android.widget.Toast;

import com.example.bourimanshop.Prevalent.Prevalent;
import com.example.bourimanshop.Model.Cart;
import com.example.bourimanshop.Model.CartViewHolder;
import com.example.ebm.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CartActivity extends AppCompatActivity {
private RecyclerView recyclerView;
private RecyclerView.LayoutManager layoutManager;
private Button NextProcessBtn;
    private TextView txtTotalAmount,txtMsg;
private int somme=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        recyclerView = findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        NextProcessBtn = findViewById(R.id.next_process_btn);
        txtTotalAmount=findViewById(R.id.total_price);
        txtMsg=findViewById(R.id.msg1);
        NextProcessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CartActivity.this,ConfirmmFinalOrderActivity.class);
                i.putExtra("Total Price",String.valueOf(somme));
                startActivity(i);
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
CheckOrdersState();
       final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>().setQuery(cartListRef.child("User View").child(Prevalent.currentOnlineUser.getPhone()).child("Products"),Cart.class).build();
        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Cart model) {
holder.txtProductQuantity.setText(model.getQuantity());
holder.txtProductPrice.setText(model.getPrice());
                holder.txtProductName.setText(model.getPname());
               int oneTypeProducTPrice = (Integer.valueOf(model.getPrice())) * Integer.valueOf(model.getQuantity());
              somme+= oneTypeProducTPrice;
                txtTotalAmount.setText("Le montant totale est "+String.valueOf(somme) +" DH" );
NextProcessBtn.setVisibility(View.VISIBLE);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[] = new CharSequence[]{
                                "Modifier",
                                "Supprimer"
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Options du panier");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which ==0){
                                    Intent intent = new Intent(CartActivity.this,ProductDetailsActivity.class);
                                    intent.putExtra("pid",model.getPid());
                                    startActivity(intent);
                                }
                                if(which==1){
                                    cartListRef.child("User View").child(Prevalent.currentOnlineUser.getPhone()).child("Products").child(model.getPid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(CartActivity.this,"Article supprimé avec succès",Toast.LENGTH_SHORT);
                                            //Intent i = new Intent(CartActivity.this,HomeActivity.class);
                                            //startActivity(i);
                                                finish();
                                            }
                                        }
                                    });
                                }
                            }
                        });
                        builder.show();
                    }
                });

            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_items_layout,viewGroup,false);
           CartViewHolder holder = new CartViewHolder(view);
           return  holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
    private void CheckOrdersState(){
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone());
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    String shippingState = dataSnapshot.child("state").getValue().toString();
                    String nom = dataSnapshot.child("nom").getValue().toString();
                    String prenom = dataSnapshot.child("prenom").getValue().toString();
                    String usernName = prenom + " " + nom;

                    if (shippingState.equals("shipped")){

txtTotalAmount.setText("la commande a été expédiée avec succès");
recyclerView.setVisibility(View.GONE);
txtMsg.setVisibility(View.VISIBLE);
txtMsg.setText("Félicitations, votre commande a été expédiée avec succès, bientôt vous recevrez votre commande jusqu'à chez vous .");
NextProcessBtn.setVisibility(View.GONE);
                           }
                         else if (shippingState.equals("not shipped")){

                        txtTotalAmount.setText("la commande n'est pas encore expédiée");
                        recyclerView.setVisibility(View.GONE);
                        txtMsg.setVisibility(View.VISIBLE);
                        NextProcessBtn.setVisibility(View.GONE);
                         }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
