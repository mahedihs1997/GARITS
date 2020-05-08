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

import java.lang.invoke.MethodHandle;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class addWorkController implements Initializable {


    //Sets up database connection
    private Connection conn = null;
    PreparedStatement pstmt = null;

    //Variables to store jobnumber and customer number
    String Jn, Cn = "";

    //Timer
    private int minute, hour, second, day, month, year;

    // Running a timer to get date time
    public void runDateTime(){
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            second = LocalDateTime.now().getSecond();
            minute = LocalDateTime.now().getMinute();
            hour = LocalDateTime.now().getHour();

            day = LocalDateTime.now().getDayOfMonth();
            month = LocalDateTime.now().getMonth().getValue();
            year = LocalDateTime.now().getYear();

            dateTime_lb.setText(day + "/" + month + "/" + year + " " + hour + ":" + (minute) + ":" + second);
        }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    // Sets jobnumber from where its being called from
    // uses the job number to populate mechanics and customer name
    public void SetJobCustomer(String J, String c){
        Jn = J;
        Cn = c;

        populateMechanicBox(J);

        JobNumber.setText(Jn);
        CustomerName.setText(Cn);

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
    private ComboBox<String> InsertType_Box;

    @FXML
    private TabPane tabPane;

    @FXML
    private ComboBox<String> SelectWork_Box;

    @FXML
    private ComboBox<Integer> TimeSelect_Box;

    @FXML
    private ComboBox<Integer> TimeInput_Box;

    @FXML
    private ComboBox<String> Mechanic_Box;

    @FXML
    private TextField InputField;

    // Button to add work
    @FXML
    void AddWork(ActionEvent event) {
        System.out.println("Eh?");

        String b = InputField.getText();

        // Checks if any field is empty, and if not it selects the pane that the customer is currently on and it uses its data to call a function to
        // add the work to the database
        // Else function to see if something is empty or wrong data was entered and pops alert box if so
        if(InsertType_Box.getValue().equals("Selection Type") && !SelectWork_Box.getValue().equals(null) && !Mechanic_Box.getValue().equals(null)){

            System.out.println("Selection");

            dbAddWork(SelectWork_Box.getValue(), TimeSelect_Box.getValue());

        }else if (InsertType_Box.getValue().equals("Input Type") && !b.equals(null)  && !Mechanic_Box.getValue().equals(null)){

            System.out.println("Meh?");

            dbAddWork(InputField.getText(), TimeInput_Box.getValue());

        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("WARNING");
            alert.setHeaderText(null);
            alert.setContentText("Your fields are empty!");

            alert.showAndWait();

        }
    }

    //Closes current form
    @FXML
    void close_app(MouseEvent event) {

        dbUpdateActualPrice();

        Stage stage = (Stage) ap.getScene().getWindow();
        stage.close();

    }

    // Minimises form
    @FXML
    void minimize_stage(MouseEvent event) {
        Stage stage = (Stage) ap.getScene().getWindow();
        stage.setIconified(true);
    }

    // Opens a different tab pane depending on the selected item in the combo box
    @FXML
    void open_Pane(ActionEvent event) {
        if(InsertType_Box.getValue().equals("Selection Type")){
            tabPane.getSelectionModel().select(0);
        }else if (InsertType_Box.getValue().equals("Input Type")){
            tabPane.getSelectionModel().select(1);
        }
    }


    // Creates connection and adds work to database
    // Runs query to insert into the tasks table
    // Alter box in case query does run successfully

    public void dbAddWork(String t, int m){
        conn = database.dbConnection.garitsConnection();
        String sql = "INSERT INTO Tasks(TaskName, Duration, JobJobNumber, TaskMechanic) VALUES(?,?,?,?)";

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, t);
            pstmt.setInt(2, m);
            pstmt.setInt(3, Integer.parseInt(Jn));
            pstmt.setString(4, Mechanic_Box.getValue());

            int i = pstmt.executeUpdate();
            if (i == 1){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText(null);
                alert.setContentText("You successfully added a task!");

                alert.showAndWait();
            }

            conn.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }


    // Runs query to update actual price in job. Gets mechanic rate and the amount of time worked
    public void dbUpdateActualPrice(){
        conn = database.dbConnection.garitsConnection();
        String sql = "UPDATE Job SET ActualDuration = (SELECT SUM(Duration) \n" +
                "FROM Tasks WHERE Tasks.JobJobNumber = ? ) \n" +
                "WHERE Job.JobNumber = ?";

        try {
            try {
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, Jn);
                pstmt.setString(2, Jn);

                pstmt.executeUpdate();

            }catch (Exception e){
                e.getStackTrace();
            }

            conn.close();

        }catch (SQLException e){
            e.getStackTrace();
        }
    }

    // Runs query to find which mechanics are in the job sheet and depending on it adds the name of those mechanics in a combo box
    public void populateMechanicBox(String J){
        Mechanic_Box.getItems().clear();

        System.out.println("Running");
        conn = database.dbConnection.garitsConnection();

        ResultSet rs = null;
        try {
            rs = conn.createStatement().executeQuery("SELECT JobStaffUsername FROM JobStaff WHERE JobStaffJobNumber = '" + J + "'");

            while (rs.next()){
                Mechanic_Box.getItems().add(rs.getString("JobStaffUsername"));
                System.out.println(rs.getString("JobStaffUsername"));
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // initialises some variables in the form and adds some items into the combo boxes
    // Runs date time to create clock and date
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        InsertType_Box.getItems().addAll("Selection Type", "Input Type");

        TimeSelect_Box.getItems().addAll(5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95, 100, 105, 110, 115, 120, 125, 130, 135, 140, 145, 150);
        TimeInput_Box.getItems().addAll(5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95, 100, 105, 110, 115, 120, 125, 130, 135, 140, 145, 150);
        SelectWork_Box.getItems().addAll("Replace Tyre", "Repair Tyre", "Replace Exhaust", "Repair Exhaust", "Replace Engine Mount", "Repair Engine Mount", "Replace Spark Plugs",
                "Repair Spark Plugs", "Replace Distributor Cap", "Repair Distributor Cap", "Paint Arrogant Red Colour", "Replace Interior Bulb", "Add Interior Bulb", "Repair Interior Bulb", "Replace Motor Oil",
                "Add Motor Oil", "Add Oil Filter", "Replace Oil Filter", "Add Air Filter", "Replace Air Filter"
        );

        InputField.setText(null);

        runDateTime();
    }
}
