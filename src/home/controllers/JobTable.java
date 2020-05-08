package home.controllers;

public class JobTable {

    String JobNumber,DateBooked, JobStatus, VehiclevehicleRegNo, VehicleCustomercustomerID, ActualPrice, ServiceType, name, Model; // Variables of type String for each column we want in Jobtable

    public JobTable(String jobNumber, String dateBooked, String jobStatus, String vehiclevehicleRegNo, String vehicleCustomercustomerID, String actualPrice, String serviceType, String name, String model) {
        //set each variable to the parameters passed in
        JobNumber = jobNumber;
        DateBooked = dateBooked;
        JobStatus = jobStatus;
        VehiclevehicleRegNo = vehiclevehicleRegNo;
        VehicleCustomercustomerID = vehicleCustomercustomerID;
        ActualPrice = actualPrice;
        ServiceType = serviceType;
        this.name = name;
        Model = model;
    }

    //Getters and setters functions
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public String getServiceType() {
        return ServiceType;
    }

    public void setServiceType(String serviceType) {
        ServiceType = serviceType;
    }

    public String getJobNumber() {
        return JobNumber;
    }

    public void setJobNumber(String jobNumber) {
        JobNumber = jobNumber;
    }

    public String getDateBooked() {
        return DateBooked;
    }

    public void setDateBooked(String dateBooked) {
        DateBooked = dateBooked;
    }

    public String getActualPrice() {
        return ActualPrice;
    }

    public void setActualPrice(String actualPrice) {
        ActualPrice = actualPrice;
    }

    public String getJobStatus() {
        return JobStatus;
    }

    public void setJobStatus(String jobStatus) {
        JobStatus = jobStatus;
    }

    public String getVehiclevehicleRegNo() {
        return VehiclevehicleRegNo;
    }

    public void setVehiclevehicleRegNo(String vehiclevehicleRegNo) {
        VehiclevehicleRegNo = vehiclevehicleRegNo;
    }

    public String getVehicleCustomercustomerID() {
        return VehicleCustomercustomerID;
    }

    public void setVehicleCustomercustomerID(String vehicleCustomercustomerID) {
        VehicleCustomercustomerID = vehicleCustomercustomerID;
    }

}
