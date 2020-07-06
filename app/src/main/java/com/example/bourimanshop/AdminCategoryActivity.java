package com.example.bourimanshop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.ebm.R;

public class AdminCategoryActivity extends AppCompatActivity {
private ImageView laptopTab,server,surveillanceCamera,accessories,printScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);
        laptopTab=findViewById(R.id.ordinateurcat);
        server=findViewById(R.id.serveurcat);
        surveillanceCamera=findViewById(R.id.camerasurveillancecat);
        accessories=findViewById(R.id.accessoirecat);
        printScanner=findViewById(R.id.printscannercat);

    }
    public void MaintainOrders(View view){

        Intent i = new Intent(AdminCategoryActivity.this,HomeActivity.class);
        i.putExtra("Admin","Admin");
        startActivity(i);
    }
    public void CheckNewOrdersbtn(View view){
        Intent i = new Intent(AdminCategoryActivity.this,AdminNewOrdersActivity.class);
        startActivity(i);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }
  public void logOut(View view){
        Intent i = new Intent(AdminCategoryActivity.this,Connexion.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
      overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        finish();
    }
    public void openLaptop(View view){
        Intent i = new Intent(AdminCategoryActivity.this,AdminAddNewProducts.class);
        i.putExtra("category","Ordinateurs fixes, ordinateurs portables et tablettes");
        startActivity(i);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }
    public void openServer(View view){
        Intent i = new Intent(AdminCategoryActivity.this,AdminAddNewProducts.class);
        i.putExtra("category","Serveurs Informatiques physiques");
        startActivity(i);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }
    public void openSurveillanceCamera(View view){
        Intent i = new Intent(AdminCategoryActivity.this,AdminAddNewProducts.class);
        i.putExtra("category","Vidéocaméra De Surveillance");
        startActivity(i);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }
    public void openAccessories(View view){
        Intent i = new Intent(AdminCategoryActivity.this,AdminAddNewProducts.class);
        i.putExtra("category","Périphériques informatiques");
        startActivity(i);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    } public void openPrintScanner(View view){
        Intent i = new Intent(AdminCategoryActivity.this,AdminAddNewProducts.class);
        i.putExtra("category","Imprimantes et Scanners");
        startActivity(i);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }


}
