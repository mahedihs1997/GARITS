package home.controllers;

public class PartJobTable {
    String PartName, PartPrice, PartQuantity; // Variables of type String which will be used to create the columns in part Job table

    public PartJobTable(String partName, String partPrice, String partQuantity) {
        //Pass parameters in and set the variables to these parameters.
        PartName = partName;
        PartPrice = partPrice;
        PartQuantity = partQuantity;
    }

    //Getters and setters
    public String getPartName() {
        return PartName;
    }

    public void setPartName(String partName) {
        PartName = partName;
    }

    public String getPartPrice() {
        return PartPrice;
    }

    public void setPartPrice(String partPrice) {
        PartPrice = partPrice;
    }

    public String getPartQuantity() {
        return PartQuantity;
    }

    public void setPartQuantity(String partQuantity) {
        PartQuantity = partQuantity;
    }
}
