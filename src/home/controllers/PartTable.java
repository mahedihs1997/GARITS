package home.controllers;

public class PartTable {

    //String variables used for the PartTable
    String PartCode, PartName, Quantity, PartPrice, PartManufacturer, Description, VehicleType, Threshold;

    public PartTable(String partCode, String partName, String quantity, String partPrice, String partManufacturer, String description, String vehicleType, String threshold) {
        //Set variables to the parameters being passed in when called
        PartCode = partCode;
        PartName = partName;
        Quantity = quantity;
        PartPrice = partPrice;
        PartManufacturer = partManufacturer;
        Description = description;
        VehicleType = vehicleType;
        Threshold = threshold;
    }

    //Getters and setters
    public String getPartCode() {
        return PartCode;
    }

    public void setPartCode(String partCode) {
        PartCode = partCode;
    }

    public String getPartName() {
        return PartName;
    }

    public void setPartName(String partName) {
        PartName = partName;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPartPrice() {
        return PartPrice;
    }

    public void setPartPrice(String partPrice) {
        PartPrice = partPrice;
    }

    public String getPartManufacturer() {
        return PartManufacturer;
    }

    public void setPartManufacturer(String partManufacturer) {
        PartManufacturer = partManufacturer;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getVehicleType() {
        return VehicleType;
    }

    public void setVehicleType(String vehicleType) {
        VehicleType = vehicleType;
    }

    public String getThreshold() {
        return Threshold;
    }

    public void setThreshold(String threshold) {
        Threshold = threshold;
    }
}
