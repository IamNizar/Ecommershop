package com.example.bourimanshop;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bourimanshop.Prevalent.Prevalent;
import com.example.ebm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmmFinalOrderActivity extends AppCompatActivity {
private String totalAmount = "";
private Button btnconfirmer;
private EditText fname,lname,city,address,phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmm_final_order);
        totalAmount=getIntent().getStringExtra("Total Price");
        fname=findViewById(R.id.livprenom);
        lname=findViewById(R.id.livnom);
        city=findViewById(R.id.livville);
        address=findViewById(R.id.livaddress);
        phone=findViewById(R.id.livtelephone);

        //Toast.makeText(ConfirmmFinalOrderActivity.this,"Le montant totale est "+totalAmount+" DH",Toast.LENGTH_LONG).show();
    btnconfirmer=findViewById(R.id.confirmsorderbtn);
    btnconfirmer.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Check();
        }
    });

    }

  private void Check() {
        String prenom = fname.getText().toString();
        String nom =lname.getText().toString();
        String adresse = address.getText().toString();
        String ville = city.getText().toString();
        String telephone =phone.getText().toString();

        if (TextUtils.isEmpty(prenom))
        {
            Toast.makeText(this, "s'il vous plaît entrez votre prénom...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(nom))
        {
            Toast.makeText(this, "s'il vous plaît entrez votre nom ...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(ville))
        {
            Toast.makeText(this, "s'il vous plaît entrez votre ville ...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(adresse))
        {
            Toast.makeText(this, "s'il vous plaît entrez votre adresse ...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(telephone)){
            Toast.makeText(this,"s'il vous plaît entrez votre telephone ...",Toast.LENGTH_LONG).show();
        } else {
            ConfirmOrder();
        }

    }

    private void ConfirmOrder() {
      final   String saveCurrentTime, saveCurrentDate;
        Calendar CalForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(CalForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime= currentTime.format(CalForDate.getTime());

        final DatabaseReference ordersref = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone());
        HashMap<String,Object> ordersMap = new HashMap<>();
        ordersMap.put("prenom",fname.getText().toString());
        ordersMap.put("nom",lname.getText().toString());
        ordersMap.put("telephone",phone.getText().toString());
        ordersMap.put("ville",city.getText().toString());
        ordersMap.put("date",saveCurrentDate);
        ordersMap.put("time",saveCurrentTime);
        ordersMap.put("adresse",address.getText().toString());
        ordersMap.put("TotalAmout",totalAmount);
        ordersMap.put("state","not shipped");
        ordersref.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View").child(Prevalent.currentOnlineUser.getPhone())
                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ConfirmmFinalOrderActivity.this,"Votre commande a été placée avec succès",Toast.LENGTH_LONG).show();
                                Intent i = new Intent(ConfirmmFinalOrderActivity.this,HomeActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                finish();
                            }
                        }
                    });
                }
            }
        });
    }

}
