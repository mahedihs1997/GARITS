package home.controllers;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class addPartsController implements Initializable {

    private Connection conn = null; // Create connection variable
    PreparedStatement pstmt = null; // Create Statement variable

    String Jn, Cn = "";

    int PQ = 0; // Part quantity

    int PC = 0; // Variable for PartCode

    private int minute, hour, second, day, month, year; // Variables for the minute,hour,second,day,month,year

    //Function to get current date and time
    public void runDateTime(){
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            second = LocalDateTime.now().getSecond(); //Stores current second in second variable
            minute = LocalDateTime.now().getMinute(); // Stores current minute in minute variable
            hour = LocalDateTime.now().getHour(); // Stores current hour in hour variable

            day = LocalDateTime.now().getDayOfMonth(); // stores current day in day variable
            month = LocalDateTime.now().getMonth().getValue(); // stores current month in month variable
            year = LocalDateTime.now().getYear(); // Stores the year in year variable

            dateTime_lb.setText(day + "/" + month + "/" + year + " " + hour + ":" + (minute) + ":" + second); // Set how it will be displayed
        }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE); // Shows the clock (Time) going forward
        clock.play(); // Start clock
    }

    public void SetJobCustomer(String J, String c){
        Jn = J; // Set Jn to the passed in variable - (JobNumber)
        Cn = c; // Set Cn to passed in variable - (CustomerName)

        JobNumber.setText(Jn); // Sets the variable jobnumber to the value of Jn
        CustomerName.setText(Cn); // sets variable customerName to Cn

    }

    @FXML
    private AnchorPane ap;

    @FXML
    private Label dateTime_lb;

    @FXML
    private Label JobNumber;

    @FXML
    private Label CustomerName;

    @FXML
    private ComboBox<String> SelectPart_Box;

    @FXML
    private TabPane tabPane;

    @FXML
    private ComboBox<Integer> Quantity_Box;

    @FXML
    //Function to add parts
    void AddPart(ActionEvent event) {

        //If something is selected then add the part
        if(!SelectPart_Box.getValue().equals(null)){

            System.out.println("Selection"); // Print out selection

            dbAddPart(); // call dbAddPart function

        }
        //Else show a warning saying that nothing is selected
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("WARNING");
            alert.setHeaderText(null);
            alert.setContentText("Your fields are empty!");

            alert.showAndWait();

        }
    }

    @FXML
    //Closes window
    void close_app(MouseEvent event) {

        dbUpdateActualPrice(); // Updates actual price by calling this function

        Stage stage = (Stage) ap.getScene().getWindow();
        stage.close();

    }

    @FXML
    //Minimize the window that is currently opened
    void minimize_stage(MouseEvent event) {
        Stage stage = (Stage) ap.getScene().getWindow();
        stage.setIconified(true);
    }

    //Function to calculate price
    public void dbUpdateActualPrice(){
        conn = database.dbConnection.garitsConnection(); // get database connection
        String sql = "UPDATE Job SET ActualDuration = (SELECT SUM(Duration) \n" + //SQL Statement to update Job columns
                "FROM Tasks WHERE Tasks.JobJobNumber = ? ) \n" +
                "WHERE Job.JobNumber = ?";

        try {
            try {
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, Jn); //Add values to the parameterIndex
                pstmt.setString(2, Jn);

                pstmt.executeUpdate(); // Executes the update query

            }catch (Exception e){
                e.getStackTrace();
            }

            conn.close();

        }catch (SQLException e){
            e.getStackTrace();
        }
    }
    //Adds items into part box
    public void populatePartBox(){
        SelectPart_Box.getItems().clear(); //selects the part box and clears the items inside.
        conn = database.dbConnection.garitsConnection();

        ResultSet rs = null; //Variable that allows us to get the values of a cell in the database
        try {
            rs = conn.createStatement().executeQuery("SELECT PartName FROM Part "); //ResultSet based on the sql query stated here

            //While there is still rows in the database
            while (rs.next()){
                SelectPart_Box.getItems().add(rs.getString("PartName")); // get value of the partname and insert into selectPart box
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Add parts and checks if there is enough supplies to add them
    public void dbAddPart(){
        getPartQuantityAndCode(); //calls function to set part quantity and code value

        //If PartQuantity (PQ) less than the value of the quantity box show warning that there is not enough
        if (PQ < Quantity_Box.getValue()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("WARNING");
            alert.setHeaderText(null);
            alert.setContentText("You do not have enough supplies, please reorder stock!");

            alert.showAndWait();
        }
        //Else update the part quantity and part used values in the database
        else if (PQ >= Quantity_Box.getValue()){
            try{
                dbUpdatePartQuantity();
                dbInsertPartUsed();
            }catch (Exception e){
                e.getStackTrace();
            }
        }

    }
    //Get quantity and part code
    public void getPartQuantityAndCode(){
        conn = database.dbConnection.garitsConnection();
        String sql = "SELECT Quantity, PartCode FROM Part WHERE PartName = ?"; //SQL Statement

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, SelectPart_Box.getValue());

            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                PQ = rs.getInt("Quantity"); // Store value from database under column Quantity into variable
                PC = rs.getInt("PartCode"); // Store value from database under column PartCode into variable
            }
            conn.close(); // close connection

        }catch (SQLException e){
            System.out.println(e.getStackTrace());
        }
    }

    //Updates the value of part quantity
    public void dbUpdatePartQuantity(){
        conn = database.dbConnection.garitsConnection();
        String sql = "UPDATE Part SET Quantity = Quantity - ? WHERE PartCode = ?";

        try {
            try {
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, Quantity_Box.getValue()); //Set value of the part quantity using the quantity we selected in Quantity_Box
                pstmt.setInt(2, PC); // Set value of Quantity where PartCode = PC

                pstmt.executeUpdate();

            }catch (Exception e){
                e.getStackTrace();
            }

            conn.close();

        }catch (SQLException e){
            e.getStackTrace();
        }
    }
    //Insert part used into parts added window
    public void dbInsertPartUsed(){
        conn = database.dbConnection.garitsConnection();
        String sql = "INSERT INTO PartUsed(PartPartCode, JobJobNumber, PartQuantity) VALUES(?,?,?)"; // Values(?,?,?) = The values you set using pstmt.

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, PC); // Sets PartPartCode in Database to PC
            pstmt.setInt(2, Integer.parseInt(Jn)); // Sets JobJobNumber in Database to Jn
            pstmt.setInt(3, Quantity_Box.getValue()); // Sets PartQuantity to the value of the quantity box

            int i = pstmt.executeUpdate(); // int i = 1 when pstmt executes Update function.
            //If i = 1 then show successful message
            if (i == 1){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText(null);
                alert.setContentText("You successfully added a part!");

                alert.showAndWait();
            }

            conn.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    //Initialize function that gets called as soon as this class is being used
    public void initialize(URL location, ResourceBundle resources) {

        Quantity_Box.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10); // QuantityBox that adds 10 variables into the QuantityBox

        populatePartBox(); // call populatePartBox;

        runDateTime(); // call runDateTime();
    }
}
