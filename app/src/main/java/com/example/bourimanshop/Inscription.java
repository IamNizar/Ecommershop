package com.example.bourimanshop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ebm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Inscription extends AppCompatActivity {
EditText nom,prenom,mdp,telephone;
Button CreateAccountButton;
    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        nom=findViewById(R.id.nomusertxt);
        prenom=findViewById(R.id.prenomusertxt);
        mdp=findViewById(R.id.passwordusertxt);
        telephone=findViewById(R.id.phonetxt);
        CreateAccountButton =findViewById(R.id.signupbtn);
        loadingBar = new ProgressDialog(this);

        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                CreateAccount();
            }
        });
    }
    private void CreateAccount()
    {
        String fname = prenom.getText().toString();
        String lname = nom.getText().toString();
        String phone= telephone.getText().toString();
        String password = mdp.getText().toString();

        if (TextUtils.isEmpty(fname))
        {
            Toast.makeText(this, "s'il vous plaît entrez votre prénom...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "s'il vous plaît entrez votre telephone ...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(lname))
        {
            Toast.makeText(this, "s'il vous plaît entrez votre nom ...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "s'il vous plaît entrez votre mot de passe ...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Créer un compte");
            loadingBar.setMessage("Veuillez attendre s'il vous plaît ... ");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidateEmailNumber(fname, lname,phone, password);
        }
    }
    private void ValidateEmailNumber(final String fname, final String lname,final String phone, final String password)
    {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (!(dataSnapshot.child("Users").child(phone).exists()))
                {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("lastname", lname);
                    userdataMap.put("firstname", fname);
                    userdataMap.put("phone",phone );
                    userdataMap.put("password", password);

                    RootRef.child("Users").child(phone).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(Inscription.this, "Félicitations, Votre compte a été crée", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent = new Intent(Inscription.this, Connexion.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        loadingBar.dismiss();
                                        Toast.makeText(Inscription.this, "Erreur de connexion, veuillez s'il vous plaît réssayer ", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else
                {
                    Toast.makeText(Inscription.this, "Ce numéro  " + phone + " existe déja, réessayer avec un autre numéro.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
telephone.getText().clear();
                    telephone.setFocusableInTouchMode(true);
                    telephone.setFocusable(true);
                    telephone.requestFocus();
                   /*Intent intent = new Intent(Inscription.this, Connexion.class);
                    startActivity(intent);*/
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
