package home.controllers;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
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

public class discountController implements Initializable {

    private Connection conn = null;
    PreparedStatement pstmt = null;

    public String vcustomerID = "";
    public String vDiscountType = "";

    boolean hasDiscount = false;

    public void setCustomer (String customerID){
        this.vcustomerID = customerID;

        setDetails();

        setDiscountType();
    }

    private int minute, hour, second, day, month, year;

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
    private ComboBox<String> DiscountType_Box;

    @FXML
    private ComboBox<String> FixedDiscountRate_Box;

    @FXML
    private ComboBox<String> DiscountRateParts_Box;

    @FXML
    private ComboBox<String> DiscountRateServices_Box;

    @FXML
    private ComboBox<String> BasicDiscountRate_Box;

    @FXML
    private ComboBox<String> StandardDiscountRate_Box;

    @FXML
    private ComboBox<String> PremiumDiscountRate_Box;

    @FXML
    private Label DiscountTypeLabel;

    @FXML
    private Label Rate1Label;

    @FXML
    private Label Rate2Label;

    @FXML
    private Label Rate3Label;

    @FXML
    private TabPane tabPane;

    @FXML
    private Label ErrorMessage;

    @FXML
    void open_Pane(ActionEvent event) {

        if(DiscountType_Box.getValue().equals("Fixed")){
            tabPane.getSelectionModel().select(0);
        }else if (DiscountType_Box.getValue().equals("Variable")){
            tabPane.getSelectionModel().select(1);
        }else if (DiscountType_Box.getValue().equals("Flexible")){
            tabPane.getSelectionModel().select(2);
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

    @FXML
    void AddEdit_Discount(ActionEvent event) {
    String type = DiscountType_Box.getValue();

    Double FixedD = Double.parseDouble(FixedDiscountRate_Box.getValue());
    Double PartsD = Double.parseDouble(DiscountRateParts_Box.getValue());
    Double ServiceD = Double.parseDouble(DiscountRateServices_Box.getValue());
    Double BasicD = Double.parseDouble(BasicDiscountRate_Box.getValue());
    Double StandardD = Double.parseDouble(StandardDiscountRate_Box.getValue());
    Double PremiumD =  Double.parseDouble(PremiumDiscountRate_Box.getValue());

    if (hasDiscount && (CustomerType.getText().equals("Account Holder") || CustomerType.getText().equals("Company"))){
        dbUpdateCustomerDiscount(vcustomerID, type, FixedD, PartsD, ServiceD, BasicD, StandardD, PremiumD);
        ErrorMessage.setVisible(false);
    }else if (!hasDiscount && (CustomerType.getText().equals("Account Holder") || CustomerType.getText().equals("Company"))){
        dbAddCustomerDiscount(vcustomerID, type, FixedD, PartsD, ServiceD, BasicD, StandardD, PremiumD);
        ErrorMessage.setVisible(false);
    }else{
        ErrorMessage.setVisible(true);
    }

    }

    //Add discount plan to the customer using CustomercustomerID
    public void dbAddCustomerDiscount(String c, String n, Double f, Double p, Double s, Double b, Double st, Double pr){
        conn = database.dbConnection.garitsConnection();
        //SQL statement = Insert values into Table discountPlan columns CustomercustomerId,name,fixedrate ect...
        String sql = "INSERT INTO DiscountPlan(CustomercustomerID, Name, FixedRate, PartsRate, ServiceRate, BasicRate, StandardRate, PremiumRate) VALUES(?,?,?,?,?,?,?,?)";

        //Sets values of each cell to the parameter passed in
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(c));
            pstmt.setString(2, n);
            pstmt.setDouble(3, f);
            pstmt.setDouble(4, p);
            pstmt.setDouble(5, s);
            pstmt.setDouble(6, b);
            pstmt.setDouble(7, st);
            pstmt.setDouble(8, pr);


            int i = pstmt.executeUpdate();
            if (i == 1){
                System.out.println("Discount Successfully Added");
            }

            conn.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }

    //Update the details in DiscountPlan table
    public void dbUpdateCustomerDiscount(String c, String n, Double f, Double p, Double s, Double b, Double st, Double pr){
        conn = database.dbConnection.garitsConnection();

        int cid = Integer.parseInt(c);

        String sql = "UPDATE DiscountPlan SET Name = ?, FixedRate = ?, PartsRate = ?, ServiceRate = ?, BasicRate = ?, StandardRate = ?, PremiumRate = ?  WHERE CustomercustomerID ='" + cid +"'";

        //Updates each cell under each column with parameters passed in
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, n);
            pstmt.setDouble(2, f);
            pstmt.setDouble(3, p);
            pstmt.setDouble(4, s);
            pstmt.setDouble(5, b);
            pstmt.setDouble(6, st);
            pstmt.setDouble(7, pr);


            int i = pstmt.executeUpdate();
            if (i == 1){
                System.out.println("Discount Successfully Added");
            }

            conn.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    //Sets details of customer
    public void setDetails() {
        try {
            conn = database.dbConnection.garitsConnection();

            ResultSet rs = conn.createStatement().executeQuery("select * from Customer WHERE customerID = '" + vcustomerID + "'");

            while (rs.next()){
                CustomerName.setText(rs.getString("Name")); // CustomerName = the value in the cell under the Name column when Query is executed
                CustomerType.setText(rs.getString("AccountType")); // CustomerType = the value in the cell under the AccountType column when Query is executed
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

    public void setDiscountType() {
        {
            try {
                conn = database.dbConnection.garitsConnection();

                ResultSet rs = conn.createStatement().executeQuery("select * from DiscountPlan WHERE CustomercustomerID = '" + vcustomerID + "'");

                //while there is rows in database
                while (rs.next()) {

                    hasDiscount = true; //Boolean variable

                    vDiscountType  = (rs.getString("Name")); // Gets DiscountType

                    if (vDiscountType.equals("Fixed")) // If discount is "fixed"
                    {
                        //Displays the fixed raye
                        DiscountTypeLabel.setText("Fixed"); // Set the label to display String "Fixed"
                        Rate1Label.setText("Fixed Discount: " + rs.getString("FixedRate") + "%");
                    }
                    else if (vDiscountType.equals("Flexible")) // else if discount is "Flexible"
                    {
                        //Sets each label to show the rates for Flexible discounts
                        DiscountTypeLabel.setText("Flexible");
                        Rate1Label.setText("Basic Discount: " + rs.getString("BasicRate") + "%");
                        Rate2Label.setText("Standard Discount: " + rs.getString("StandardRate") + "%");
                        Rate3Label.setText("Premium Discount: " + rs.getString("PremiumRate") + "%");

                    }else if (vDiscountType.equals("Variable")) // else if discount is "Variable"
                    {
                        //Sets each label to display the rate according to Parts and Services
                        DiscountTypeLabel.setText("Variable");
                        Rate1Label.setText("Parts Discount: " + rs.getString("PartsRate") + "%");
                        Rate2Label.setText("Service Discount: " + rs.getString("ServiceRate") + "%");
                    }
                }


                if (!hasDiscount){
                    DiscountTypeLabel.setText("Customer has no discount.");
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        runDateTime();
        DiscountType_Box.getItems().addAll("Fixed", "Flexible", "Variable");
        DiscountType_Box.setValue("Fixed");


        DiscountRateParts_Box.getItems().addAll("1", "2", "3");
        DiscountRateServices_Box.getItems().addAll("1", "2", "3");
        FixedDiscountRate_Box.getItems().addAll("1", "2", "3");

        BasicDiscountRate_Box.getItems().addAll("1", "2", "3", "4", "5");
        StandardDiscountRate_Box.getItems().addAll("1", "2", "3", "4", "5");
        PremiumDiscountRate_Box.getItems().addAll("1", "2", "3", "4", "5");

        DiscountTypeLabel.setText("Discount Type:");
        Rate1Label.setText("");
        Rate2Label.setText("");
        Rate3Label.setText("");


        DiscountRateParts_Box.setValue("0");
        DiscountRateServices_Box.setValue("0");
        FixedDiscountRate_Box.setValue("0");

        BasicDiscountRate_Box.setValue("0");
        StandardDiscountRate_Box.setValue("0");
        PremiumDiscountRate_Box.setValue("0");

        ErrorMessage.setVisible(false);
    }
}
