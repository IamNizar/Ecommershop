package com.example.bourimanshop.Model;

public class Users {
    private String firstname,lastname, phone, password,address,image;

    public Users()
    {

    }

    public Users(String firstname, String lastname, String phone, String password, String address, String image) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.password = password;
        this.address = address;
        this.image = image;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }
public String getName(){
        return firstname+" "+lastname;
}
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
