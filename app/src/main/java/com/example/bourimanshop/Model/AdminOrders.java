package com.example.bourimanshop.Model;

public class AdminOrders {
   private  String TotalAmout,adresse,date,nom,prenom,ville,state,time,telephone;

    public AdminOrders() {
    }

    public AdminOrders(String totalAmout, String adresse, String date, String nom, String prenom, String ville, String state, String time, String telephone) {
        TotalAmout = totalAmout;
        this.adresse = adresse;
        this.date = date;
        this.nom = nom;
        this.prenom = prenom;
        this.ville = ville;
        this.state = state;
        this.time = time;
        this.telephone = telephone;
    }

    public String getTotalAmout() {
        return TotalAmout;
    }
    public String getFullName() {
        return nom +" "+ prenom;
    }

    public void setTotalAmout(String totalAmout) {
        TotalAmout = totalAmout;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getDate() {
        return date;
    }


    public void setDate(String date) {
        this.date = date;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
