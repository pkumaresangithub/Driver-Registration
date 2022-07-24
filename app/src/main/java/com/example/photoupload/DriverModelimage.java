package com.example.photoupload;

public class DriverModelimage {
    String mobileno,fname,mname,lname,address, state,city,pin,imageurl;

    public DriverModelimage() {
    }

    public DriverModelimage(String mobileno, String fname, String mname, String lname, String address, String state, String city, String pin, String imageurl) {
        this.mobileno = mobileno;
        this.fname = fname;
        this.mname = mname;
        this.lname = lname;
        this.address = address;
        this.state = state;
        this.city = city;
        this.pin = pin;
        this.imageurl = imageurl;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
}
