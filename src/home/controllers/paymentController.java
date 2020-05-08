package home.controllers;

import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
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

public class paymentController implements Initializable {

    // sets up connection
    private Connection conn = null;
    PreparedStatement pstmt = null;
    // sets customer id, job number  and variables to calculate the cost of the job
    String CustomerID;
    double PP, LP, SP;

    int Jn = 0;

    int c = 0;

    double MonthlySpending = 0;


    double BeforeVAT = 0;
    double Total = 0;

    String discountType = "";

    boolean hasDiscount = false;
    // gets the job number from the call and it sets the details using the job number to go throughout the database and find the relevant details nedded
    public void setAllDetails (int J){
        Jn = J;

        setJobNumber();

        JobNumber.setText("" + Jn);

        setDetails();

        setDiscountType();

        calculateDetails();
    }

    // variables for timer

    private int minute, hour, second, day, month, year;
    // timer
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


    @FXML
    private AnchorPane ap;

    @FXML
    private Label dateTime_lb;

    @FXML
    private Label CustomerName;

    @FXML
    private Label CustomerType;

    @FXML
    private Label JobNumber;

    @FXML
    private Label DiscountTypeLabel;

    @FXML
    private Label Label1;

    @FXML
    private Label Rate1Label;

    @FXML
    private Label Percentage1;

    @FXML
    private Label Label2;

    @FXML
    private Label Rate2Label;

    @FXML
    private Label Percentage2;

    @FXML
    private Label Label3;

    @FXML
    private Label Rate3Label;

    @FXML
    private Label Percentage3;

    @FXML
    private Label Rate1Label1;

    @FXML
    private Label Rate2Label1;

    @FXML
    private Label Rate3Label1;

    @FXML
    private ComboBox<String> PaymentType_Box;

    @FXML
    private TabPane tabPane;

    @FXML
    private TextField AmountGivenField;

    @FXML
    private Label ChangeLabel;

    @FXML
    private ComboBox<String> CardType_Box;

    @FXML
    private TextField CardNumberField;

    // opens pane depending on users choice in the combo box, either cash or card
    @FXML
    void open_Pane(ActionEvent event) {

        if(PaymentType_Box.getValue().equals("Cash")){
            tabPane.getSelectionModel().select(0);
        }else if (PaymentType_Box.getValue().equals("Card")){
            tabPane.getSelectionModel().select(1);
        }
    }

    @FXML
    void close_app(MouseEvent event) {
        Stage stage = (Stage) ap.getScene().getWindow();
        stage.close();
    }

    @FXML
    void minimize_stage(MouseEvent event) {
        Stage stage = (Stage) ap.getScene().getWindow();
        stage.setIconified(true);
    }

    // pays completed
    @FXML
    void Pay(ActionEvent event){

// checks which type of payment if card , directly calls add payment and change the job status
// if cash it checks that amount given is greater than job cost and it calls payment
        if (PaymentType_Box.getValue().equals("Card")){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Paid");
            alert.setHeaderText(null);
            alert.setContentText("Not Enoguh Money!");

            alert.showAndWait();

            dbAddPayment();

            changeJobStatus();

        }else if (PaymentType_Box.getValue().equals("Cash") && !AmountGivenField.getText().equals(null)){

            Double aG = Double.parseDouble(AmountGivenField.getText());

            if (aG < Total){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("WARNING");
                alert.setHeaderText(null);
                alert.setContentText("Not Enough Money!");

                alert.showAndWait();
            }else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Paid");
                alert.setHeaderText(null);
                alert.setContentText("Change is: " + (aG - Total));

                alert.showAndWait();

                dbAddPayment();

                changeJobStatus();
            }


        }

    }

    //calculates amount to pay
    public void calculateDetails(){
        // access database using job number to get relevant information for the Payment
        dbJobInformation(Jn);
        dbGetTotalPriceParts(Jn);
        dbGetTotalPriceLabour(Jn);
        dbGetCurrentMonthSpending();

        Double PartMarkup = PP * 1.3;

// if casual calculates price with no discount
// if account holder, checks discount type, and uses relevant variables to calculate the total amount
        if (CustomerType.getText().equals("Casual")){
            Double BVAT = PartMarkup + LP + SP;
            BeforeVAT = BVAT;
            Total = BVAT * 1.2;
        }else if (CustomerType.getText().equals("Account Holder")){
            if(DiscountTypeLabel.getText().equals("No Discount")){
                Double BVAT = PartMarkup + LP + SP;
                BeforeVAT = BVAT;
                Total = BVAT * 1.2;
            }else if (DiscountTypeLabel.getText().equals("Fixed")){
                Double BVAT = PartMarkup + LP + SP;
                int FixedDiscount = Integer.parseInt(Rate1Label.getText());
                BeforeVAT = (BVAT * (100 - FixedDiscount)) / 100;
                Total = BeforeVAT * 1.2;
            }else if (DiscountTypeLabel.getText().equals("Variable")){

                int PartsDiscount = Integer.parseInt(Rate1Label.getText());
                int ServiceDiscount = Integer.parseInt(Rate2Label.getText());

                Double newLP = (LP * (100 - ServiceDiscount)) / 100;
                Double newSP = (SP * (100 - ServiceDiscount)) / 100;
                Double newPP = (PartMarkup * (100 - PartsDiscount)) / 100;

                Double BVAT = newPP + newSP + newLP;

                BeforeVAT = BVAT;
                Total = BeforeVAT * 1.2;
            }else if (DiscountTypeLabel.getText().equals("Flexible")){
                if(MonthlySpending < 1000){
                    Double BVAT = PartMarkup + LP + SP;
                    int BasicDiscount = Integer.parseInt(Rate1Label.getText());
                    BeforeVAT = (BVAT * (100 - BasicDiscount)) / 100;
                    Total = BeforeVAT * 1.2;
                }else if (MonthlySpending > 1000 && MonthlySpending < 5000){
                    Double BVAT = PartMarkup + LP + SP;
                    int StandardDiscount = Integer.parseInt(Rate2Label.getText());
                    BeforeVAT = (BVAT * (100 - StandardDiscount)) / 100;
                    Total = BeforeVAT * 1.2;
                }else if (MonthlySpending > 5000){
                    Double BVAT = PartMarkup + LP + SP;
                    int PremiumDiscount = Integer.parseInt(Rate3Label.getText());
                    BeforeVAT = (BVAT * (100 - PremiumDiscount)) / 100;
                    Total = BeforeVAT * 1.2;
                }

            }

        }

// shows the price before vat and after and vat amount
        Rate1Label1.setText("Price: £" + BeforeVAT);
        Rate2Label1.setText("VAT: £" + BeforeVAT*0.2 );
        Rate3Label1.setText("Total: £" + Total);

    }
    // gets service price
    public void dbJobInformation(int s){
        conn = database.dbConnection.garitsConnection();

        ResultSet rs = null;
        try {
            rs = conn.createStatement().executeQuery("SELECT * FROM Job WHERE JobNumber = '" + s + "'");

            while (rs.next()){
                PP = rs.getDouble("ServicePrice");
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // gets total price of parts calculating using part quantity and price and adding all the parts used
    public void dbGetTotalPriceParts(int j){
        conn = database.dbConnection.garitsConnection();

        ResultSet rs = null;
        try {
            rs = conn.createStatement().executeQuery("SELECT SUM(PartPrice * PartQuantity) FROM (\n" +
                    "\n" +
                    "SELECT DISTINCT PartName, PartPrice, PartQuantity, PartCode \n" +
                    "FROM Job JOIN PartUsed ON (PartUsed.JobJobNumber = '"+ j +"') \n" +
                    "JOIN Part ON (Part.PartCode = PartUsed.PartPartCode)\n" +
                    ")\n");

            while (rs.next()) {
                PP = rs.getInt("SUM(PartPrice * PartQuantity)");
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // gets total price of labour calculating time spent hourly rate mechanic rate and adding all tasks
    public void dbGetTotalPriceLabour(int j){
        conn = database.dbConnection.garitsConnection();

        ResultSet rs = null;
        try {
            rs = conn.createStatement().executeQuery("SELECT SUM(Duration * (hourlyRate/60)) FROM (\n" +
                    "\n" +
                    "SELECT DISTINCT TaskName, Duration, TaskMechanic, hourlyRate \n" +
                    "FROM Job JOIN Tasks ON (Tasks.JobJobNumber = '" + j + "') \n" +
                    "JOIN Staff ON (Staff.username = Tasks.TaskMechanic)\n" +
                    ")\n");

            while (rs.next()) {
                LP = rs.getInt("SUM(Duration * (hourlyRate/60))");
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // checks how much the customer spent in the current month
    public void dbGetCurrentMonthSpending(){
        conn = database.dbConnection.garitsConnection();

        int Month = LocalDateTime.now().getMonth().getValue();

        ResultSet rs = null;
        try {
            rs = conn.createStatement().executeQuery("SELECT SUM(AmountPaid) FROM Payment WHERE PaymentMonth = '" + Month + "' AND PaymentCustomerID = '" + c + "'");

            while (rs.next()){
                MonthlySpending = rs.getDouble("SUM(AmountPaid)");
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // sets the customer id number
    public void setJobNumber(){
        try {
            conn = database.dbConnection.garitsConnection();

            ResultSet rs = conn.createStatement().executeQuery("select * from Job WHERE JobNumber = '" + Jn + "'");

            while (rs.next()){
                c = rs.getInt("VehicleCustomercustomerID");

            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // sets customer name and account type
    public void setDetails() {
        try {
            conn = database.dbConnection.garitsConnection();

            ResultSet rs = conn.createStatement().executeQuery("select * from Customer WHERE customerID = '" + c + "'");

            while (rs.next()){
                CustomerName.setText(rs.getString("Name"));
                CustomerType.setText(rs.getString("AccountType"));
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // sets the discount type in the form and shows the discount for the customer and which type of discount they have
    public void setDiscountType() {
        {
            try {
                conn = database.dbConnection.garitsConnection();

                ResultSet rs = conn.createStatement().executeQuery("select * from DiscountPlan WHERE CustomercustomerID = '" + c + "'");

                while (rs.next()) {

                    hasDiscount = true;

                    discountType  = (rs.getString("Name"));

                    if (discountType.equals("Fixed")){
                        DiscountTypeLabel.setText("Fixed");
                        Percentage1.setVisible(true);
                        Label1.setText("Fixed Rate: ");
                        Rate1Label.setText(rs.getString("FixedRate"));
                    }else if (discountType.equals("Flexible")){
                        DiscountTypeLabel.setText("Flexible");

                        Percentage1.setVisible(true);
                        Percentage2.setVisible(true);
                        Percentage3.setVisible(true);

                        Label1.setText("Basic Rate: ");
                        Label2.setText("Standard Rate: ");
                        Label3.setText("Premium Rate: ");

                        Rate1Label.setText(rs.getString("BasicRate"));
                        Rate2Label.setText(rs.getString("StandardRate"));
                        Rate3Label.setText(rs.getString("PremiumRate"));

                    }else if (discountType.equals("Variable")){

                        Percentage1.setVisible(true);
                        Percentage2.setVisible(true);

                        Label1.setText("Part Rate: ");
                        Label2.setText("Service Rate: ");

                        DiscountTypeLabel.setText("Variable");
                        Rate1Label.setText(rs.getString("PartsRate"));
                        Rate2Label.setText(rs.getString("ServiceRate"));
                    }
                }


                if (!hasDiscount){
                    DiscountTypeLabel.setText("No Discount");
                }


            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    // runs SQL query to add the payment in the database
    public void dbAddPayment(){
        conn = database.dbConnection.garitsConnection();
        String sql = "INSERT INTO Payment(PaymentType, JobJobNumber, PaymentMonth, PaymentCustomerID, AmountPaid) VALUES(?,?,?,?,?)";

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, PaymentType_Box.getValue());
            pstmt.setInt(2, Jn);
            pstmt.setInt(3, LocalDateTime.now().getMonth().getValue());
            pstmt.setInt(4, c);
            pstmt.setDouble(5, Total);

            int i = pstmt.executeUpdate();

            conn.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }
    // changes the job status after the payment has been complete from not paid/complete to paid
    public void changeJobStatus(){
        conn = database.dbConnection.garitsConnection();
        String sql = "UPDATE Job SET JobStatus = 'Paid' WHERE JobNumber = ? ";

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Jn);

            int i = pstmt.executeUpdate();
            if (i == 1){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText(null);
                alert.setContentText("Job is now paid!");

                alert.showAndWait();
            }else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("");

                alert.showAndWait();
            }

            conn.close();

        }catch (SQLException e){
            System.out.println(e.getStackTrace());
        }
    }
    // initializes variables and cobmbo boxes
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        runDateTime();
        PaymentType_Box.getItems().addAll("Cash", "Card");
        PaymentType_Box.setValue("Cash");


        CardType_Box.getItems().addAll("Visa", "MasterCard", "American Express");


        Label1.setText("");
        Label2.setText("");
        Label3.setText("");

        Rate1Label.setText("");
        Rate2Label.setText("");
        Rate3Label.setText("");

        Percentage1.setVisible(false);
        Percentage2.setVisible(false);
        Percentage3.setVisible(false);
    }


}
