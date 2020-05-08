package home.controllers;

public class VehicleTable {

    String RegNo, Make, Model, Colour, Years, CustomercustomerID; // String variables of the column names

    public VehicleTable(String regNo, String make, String model, String colour, String years, String customercustomerID) {
        //Sets each variable to their respected parameters
        RegNo = regNo;
        Make = make;
        Model = model;
        Colour = colour;
        Years = years;
        CustomercustomerID = customercustomerID;
    }

    public String getRegNo() {
        return RegNo;
    }

    public void setRegNo(String regNo) {
        RegNo = regNo;
    }

    public String getYears() {
        return Years;
    }

    public void setYears(String years) {
        Years = years;
    }

    public String getMake() {
        return Make;
    }

    public void setMake(String make) {
        Make = make;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public String getColour() {
        return Colour;
    }

    public void setColour(String colour) {
        Colour = colour;
    }



    public String getCustomercustomerID() {
        return CustomercustomerID;
    }

    public void setCustomercustomerID(String customercustomerID) {
        CustomercustomerID = customercustomerID;
    }
}
