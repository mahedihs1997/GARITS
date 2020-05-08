package home.controllers;

public class CustomerTable {

    String customerID, name, contact, address, city, country, postcode, dayPhone, mobilePhone, accountType, lastMoT; // String variable for customer details

    //Pass in customer details and set value of each variable to their respective passed in values
    public CustomerTable(String customerID, String name, String contact, String address, String city, String country, String postcode, String dayPhone, String mobilePhone, String accountType, String lastMoT) {
        this.customerID = customerID;
        this.name = name;
        this.contact = contact;
        this.address = address;
        this.city = city;
        this.country = country;
        this.postcode = postcode;
        this.dayPhone = dayPhone;
        this.mobilePhone = mobilePhone;
        this.accountType = accountType;
        this.lastMoT = lastMoT;
    }

    //Getters and setters for the Variables
    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getDayPhone() {
        return dayPhone;
    }

    public void setDayPhone(String dayPhone) {
        this.dayPhone = dayPhone;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getLastMoT() {
        return lastMoT;
    }

    public void setLastMoT(String lastMoT) {
        this.lastMoT = lastMoT;
    }
}
