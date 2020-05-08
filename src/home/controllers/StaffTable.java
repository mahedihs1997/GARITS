package home.controllers;

public class StaffTable {

    String username, name, password, position, hourlyRate; // String variables for the Staff table

    public StaffTable(String username, String name, String password, String position, String hourlyRate) {
        //Pass parameters and set the variables to these parameters
        this.username = username;
        this.name = name;
        this.password = password;
        this.position = position;
        this.hourlyRate = hourlyRate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(String hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
