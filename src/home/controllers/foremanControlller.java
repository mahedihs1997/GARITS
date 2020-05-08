package home.controllers;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

//Functions here have not been commented due to the functions being the same as franchiseController functions

public class foremanControlller implements Initializable {
    private Connection conn = null;
    PreparedStatement pstmt = null;

    public String sNameLogin = "";
    public String sPositionLogin = "";

    public void setStaffNamePosition (String staffName, String staffPosition){
        this.sNameLogin = staffName;
        this.sPositionLogin = staffPosition;

        this.staffName.setText(staffName);
        this.staffPosition.setText(staffPosition);
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

            autoNotifications(minute, second);

        }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }


    private double x, y = 0;

    public String itemname = "";
    public String vehicleItem = "";
    public String partItem = "";
    public String jobItem = "";
    public String JobCustomerID = "";

    @FXML
    private TextField searchList;

    @FXML
    private AnchorPane ap;

    @FXML
    private Label dateTime_lb;

    @FXML
    private Label staffName;

    @FXML
    private Label staffPosition;

    @FXML
    private TabPane tabPane;

    @FXML
    private Label info_Dashboard;

    @FXML
    private Label info_customerList;

    @FXML
    private TableView<CustomerTable> table;

    @FXML
    private TableColumn<CustomerTable, String> customerID_cl;

    @FXML
    private TableColumn<CustomerTable, String> name_cl;

    @FXML
    private TableColumn<CustomerTable, String> contact_cl;

    @FXML
    private TableColumn<CustomerTable, String> address_cl;

    @FXML
    private TableColumn<CustomerTable, String> city_cl;

    @FXML
    private TableColumn<CustomerTable, String> country_cl;

    @FXML
    private TableColumn<CustomerTable, String> postcode_cl;

    @FXML
    private TableColumn<CustomerTable, String> dayPhone_cl;

    @FXML
    private TableColumn<CustomerTable, String> mobilePhone_cl;

    @FXML
    private TableColumn<CustomerTable, String> accountType_cl;

    @FXML
    private TableColumn<CustomerTable, String> lastMoT_cl;

    ObservableList<CustomerTable> oblist = FXCollections.observableArrayList();


    @FXML
    private TableView<VehicleTable> vehicleTable;

    @FXML
    private TableColumn<VehicleTable, String> RegNo_cl;

    @FXML
    private TableColumn<VehicleTable, String> Make_cl;

    @FXML
    private TableColumn<VehicleTable, String> Model_cl;

    @FXML
    private TableColumn<VehicleTable, String> colour_cl;

    @FXML
    private TableColumn<VehicleTable, String> Years_cl;

    ObservableList<VehicleTable> vehicleList = FXCollections.observableArrayList();



    @FXML
    private Label info_NewCustomer;

    @FXML
    private Label info_editCustomer;

    @FXML
    private Label info_viewCustomer;

    @FXML
    private TextField name_field;

    @FXML
    private TextField contact_field;

    @FXML
    private TextField address_field;

    @FXML
    private TextField city_field;

    @FXML
    private TextField country_field;

    @FXML
    private TextField postcode_field;

    @FXML
    private TextField dayPhone_field;

    @FXML
    private TextField mobilePhone_field;

    @FXML
    private ComboBox<String> accountType_box;



    @FXML
    private Label nameLabel;

    @FXML
    private Label contactLabel;

    @FXML
    private Label addressLabel;

    @FXML
    private Label cityLabel;

    @FXML
    private Label countryLabel;

    @FXML
    private Label postcodeLabel;

    @FXML
    private Label dayPhoneLabel;

    @FXML
    private Label mobilePhoneLabel;

    @FXML
    private Label accountTypeLabel;



    @FXML
    private TextField name_field1;

    @FXML
    private TextField contact_field1;

    @FXML
    private TextField address_field1;

    @FXML
    private TextField city_field1;

    @FXML
    private TextField country_field1;

    @FXML
    private TextField postcode_field1;

    @FXML
    private TextField dayPhone_field1;

    @FXML
    private TextField mobilePhone_field1;

    @FXML
    private ComboBox<String> accountType_box1;



    @FXML
    private Label customerIdLabel1;

    @FXML
    private Label nameLabel1;

    @FXML
    private Label contactLabel1;

    @FXML
    private Label addressLabel1;

    @FXML
    private Label cityLabel1;

    @FXML
    private Label countryLabel1;

    @FXML
    private Label postcodeLabel1;

    @FXML
    private Label dayPhoneLabel1;

    @FXML
    private Label mobilePhoneLabel1;

    @FXML
    private Label accountTypeLabel1;



    @FXML
    private TextField RegNo_Field;

    @FXML
    private TextField Make_Field;

    @FXML
    private TextField Model_Field;

    @FXML
    private TextField Colour_Field;

    @FXML
    private TextField Years_Field;



    @FXML
    private TextField RegNo_Field2;

    @FXML
    private TextField Make_Field2;

    @FXML
    private TextField Model_Field2;

    @FXML
    private TextField Colour_Field2;

    @FXML
    private TextField Years_Field2;



    @FXML
    private Label info_partsList;

    @FXML
    private TextField searchPartList;

    @FXML
    private TableView<PartTable> partsTable;

    @FXML
    private TableColumn<PartTable, String> PartCode_cl;

    @FXML
    private TableColumn<PartTable, String> PartName_cl;

    @FXML
    private TableColumn<PartTable, String> Quantity_cl;

    @FXML
    private TableColumn<PartTable, String> Price_cl;

    @FXML
    private TableColumn<PartTable, String> Manufacturer_cl;

    @FXML
    private TableColumn<PartTable, String> Description_cl;

    @FXML
    private TableColumn<PartTable, String> VehicleType_cl;

    @FXML
    private TableColumn<PartTable, String> Threshold_cl;

    ObservableList<PartTable> PartList = FXCollections.observableArrayList();


    @FXML
    private Label info_jobList;

    @FXML
    private TextField searchJobList;

    @FXML
    private TableView<JobTable> jobsTable;

    @FXML
    private TableColumn<JobTable, String> JobNumber_cl;

    @FXML
    private TableColumn<JobTable, String> Date_cl;

    @FXML
    private TableColumn<JobTable, String> Vehicle_cl;

    @FXML
    private TableColumn<JobTable, String> Customer_cl;

    @FXML
    private TableColumn<JobTable, String> Status_cl;

    @FXML
    private TableColumn<JobTable, String> JobCustomerName_cl;

    @FXML
    private TableColumn<JobTable, String> JobVehicleModel_cl;

    @FXML
    private TableColumn<JobTable, String> ServiceType_cl;

    ObservableList<JobTable> JobList = FXCollections.observableArrayList();

    @FXML
    private Label info_ViewPart;


    @FXML
    private Label PartCode_lb;

    @FXML
    private Label PartName_lb;

    @FXML
    private Label PartQuantity_lb;

    @FXML
    private Label PartPrice_lb;

    @FXML
    private Label PartManufacturer_lb;

    @FXML
    private Label PartDescription_lb;

    @FXML
    private Label PartVehicle_lb;

    @FXML
    private Label PartThreshold_lb;

    @FXML
    private TextField PartName_field;

    @FXML
    private TextField PartQuantity_field;

    @FXML
    private TextField PartPrice_field;

    @FXML
    private TextField PartManufacturer_field;

    @FXML
    private TextField PartDescription_field;

    @FXML
    private TextField PartVehicle_field;

    @FXML
    private TextField PartThreshold_field;

    @FXML
    private TextField PartName_field1;

    @FXML
    private TextField PartQuantity_field1;

    @FXML
    private TextField PartPrice_field1;

    @FXML
    private TextField PartManufacturer_field1;

    @FXML
    private TextField PartDescription_field1;

    @FXML
    private TextField PartVehicle_field1;

    @FXML
    private TextField PartThreshold_field1;

    @FXML
    private Label info_PendingPaymentList;

    @FXML
    private TextField searchPaymentList;

    @FXML
    private TableView<JobTable> paymentsTable;

    @FXML
    private TableColumn<JobTable, String> PendingCustomerID_cl;

    @FXML
    private TableColumn<JobTable, String> PendingCustomerName_cl;

    @FXML
    private TableColumn<JobTable, String> PendingJobNumber_cl;

    @FXML
    private TableColumn<JobTable, String> PendingAmount_cl;

    @FXML
    private TableColumn<JobTable, String> PendingDate_cl;

    @FXML
    private TableColumn<JobTable, String> PendingVehicleModel_cl;

    @FXML
    private TableColumn<JobTable, String> PendingJobStatus_cl;


    ObservableList<JobTable> PendingList = FXCollections.observableArrayList();

    @FXML
    private Label info_NewJob;

    @FXML
    private ComboBox<Integer> customerId_box;

    @FXML
    private ComboBox<String> vehicleRegNo_box;

    @FXML
    private TextField estimatedDuration_field;

    @FXML
    private DatePicker dateBooked_picker;

    @FXML
    private TextField estimatedPrice_field;

    @FXML
    private ComboBox<String> serviceType_box;

    @FXML
    private TextField ServicePrice_field;

    @FXML
    private TextArea taskDescription_area;

    @FXML
    private TextField customerNameJob;

    @FXML
    private Label TextAreaCounter;


    @FXML
    private Label JobNumberLabel;

    @FXML
    private Label MechanicLabel;

    @FXML
    private Label JobStatusLabel;

    @FXML
    private Label JobCustomerNameLabel;

    @FXML
    private Label JobVehicleRegLabel;

    @FXML
    private Label JobMakeLabel;

    @FXML
    private Label JobModelLabel;

    @FXML
    private Label JobDateLabel;

    @FXML
    private Label JobAddressLabel;

    @FXML
    private Label JobCityLabel;

    @FXML
    private Label JobCountryLabel;

    @FXML
    private Label JobPostcodeLabel;

    @FXML
    private Label EstimatedTimeLabel;

    @FXML
    private Label ActualTimeLabel;

    @FXML
    private Label workRequiredLabel;

    @FXML
    private Label JobType;

    @FXML
    private TableView<TaskTable> JobTasksTable;

    @FXML
    private TableColumn<TaskTable, String> TaskName_cl;

    @FXML
    private TableColumn<TaskTable, String> TaskDuration_cl;

    @FXML
    private TableColumn<TaskTable, String> TaskMechanic_cl;

    ObservableList<TaskTable> TaskList = FXCollections.observableArrayList();

    @FXML
    private TableView<PartJobTable> JobPartsTable;

    @FXML
    private TableColumn<PartJobTable, String> JobPartName_cl;

    @FXML
    private TableColumn<PartJobTable, String> JobQuantity_cl;

    @FXML
    private TableColumn<PartJobTable, String> JobPrice_cl;

    ObservableList<PartJobTable> JobPartList = FXCollections.observableArrayList();

    @FXML
    private ComboBox<String> SetMechanicJobNumber_Box;

    @FXML
    private ComboBox<Integer> Month_Box;

    @FXML
    private ComboBox<String> ServiceTypeBooking_Box;

    @FXML
    private ComboBox<String> JobMehcanic_Box;

    @FXML
    void close_app(MouseEvent event) {
        System.exit(0);
    }

    @FXML
    void minimize_stage(MouseEvent event) {
        Stage stage = (Stage) ap.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    void dashboard_open(ActionEvent event) {
        info_Dashboard.setText("");
        tabPane.getSelectionModel().select(0);

        addJobNumbers();

        addMechanicNames();

    }

    @FXML
    void customerList_open(ActionEvent event) {
        info_customerList.setText("");
        tabPane.getSelectionModel().select(1);
    }

    @FXML
    void newCustomer_open(ActionEvent event) {
        info_NewCustomer.setText("");
        tabPane.getSelectionModel().select(2);
    }

    @FXML
    void editCustomer_open(ActionEvent event) {
        info_editCustomer.setText("");
        tabPane.getSelectionModel().select(3);
    }

    @FXML
    public void viewCustomer(ActionEvent actionEvent) {
        try{
            itemname = table.getSelectionModel().getSelectedItem().customerID;
            info_viewCustomer.setText("");
            tabPane.getSelectionModel().select(4);
            populate_VehicleList(itemname);
            info_customerList.setVisible(false);
            set_Fields(itemname);
        }catch (Exception e){
            info_customerList.setVisible(true);
            info_customerList.setText("Please select an item from the list");
        }

        System.out.println(itemname);
    }

    @FXML
    void partsList_open(ActionEvent event) {
        info_partsList.setText("");
        tabPane.getSelectionModel().select(5);
    }

    @FXML
    void viewParts_open(ActionEvent event) {
        try{
            partItem = partsTable.getSelectionModel().getSelectedItem().PartCode;
            info_ViewPart.setText("");
            tabPane.getSelectionModel().select(6);
            info_partsList.setVisible(false);
            set_PartFields();
        }catch (Exception e){
            info_partsList.setVisible(true);
            info_partsList.setText("Please select an item from the list");
        }

        System.out.println(partItem);

    }

    @FXML
    void jobsList_open(ActionEvent event) {
        info_jobList.setText("");
        tabPane.getSelectionModel().select(7);
    }

    @FXML
    void pendingPaymentList_open(ActionEvent event) {
        info_PendingPaymentList.setText("");
        tabPane.getSelectionModel().select(8);
    }

    @FXML
    void newJob_Open(ActionEvent event) {
        info_NewJob.setText("");
        tabPane.getSelectionModel().select(9);
        setNewJobFields();
    }

    @FXML
    void viewJob(ActionEvent event) {
        try{
            jobItem = jobsTable.getSelectionModel().getSelectedItem().JobNumber;
            tabPane.getSelectionModel().select(10);
            info_jobList.setVisible(false);
            set_JobViewer(jobItem);
        }catch (Exception e){
            info_jobList.setVisible(true);
            info_jobList.setText("Please select an item from the list");
        }

        System.out.println(jobItem);


    }

    @FXML
    void sign_out(ActionEvent event) {
        openWindow(event, "login");
    }

    @FXML
    void deleteCustomerList(ActionEvent event) {
        confirmation_Box();
    }

    @FXML
    void edit_Customer(ActionEvent event) {
        dbUpdateCustomerDetails(itemname);
    }

    @FXML
    void refreshList(ActionEvent event) {
        searchList.setText("");
        table.getItems().clear();
        populate_CustomerList();
    }

    @FXML
    void register_Customer(ActionEvent event) {
        String Name = name_field.getText();
        String Contact = contact_field.getText();
        String Address = address_field.getText();
        String City = city_field.getText();
        String Country = country_field.getText();
        String Postcode = postcode_field.getText();
        String DayPhone = dayPhone_field.getText();
        String MobilePhone = mobilePhone_field.getText();
        String AccountType = accountType_box.getValue();

        Boolean cCE = checkCustomerExists(Name, Postcode, MobilePhone);

        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (cCE){
            info_NewCustomer.setText("Customer already exists!");
        }else {
            dbAddingCustomer(Name, Contact, Address, City, Country, Postcode, DayPhone, MobilePhone, AccountType);
            clear_Fields();
            info_NewCustomer.setText("Successfully registered new customer.");
        }



        System.out.println("Clicked register user");
    }

    @FXML
    void searchUsers(KeyEvent event) {
        filter();
    }

    @FXML
    public void remove_Vehicle(ActionEvent actionEvent) {
        removeVehicleConfirmation();
    }

    @FXML
    public void add_Vehicle(ActionEvent actionEvent) {
        String RegNo = RegNo_Field.getText();
        String Make = Make_Field.getText();
        String Model = Model_Field.getText();
        String Colour = Colour_Field.getText();
        String Years = Years_Field.getText();

        dbAddingVehicle(RegNo, Make, Model, Colour, Years);
        clearVehicle_Fields();

        System.out.println("Clicked register user");

    }

    @FXML
    void vehicleEditFieldsSet(MouseEvent event) {
        conn = database.dbConnection.garitsConnection();
        String sql = "SELECT * FROM Vehicle WHERE RegNo = ? ";

        try {

            try{
                vehicleItem = vehicleTable.getSelectionModel().getSelectedItem().RegNo;
            }catch (NullPointerException n){
                info_viewCustomer.setText(n.getMessage());
            }

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, vehicleItem);

            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                RegNo_Field2.setText((rs.getString("RegNo")));
                Make_Field2.setText(rs.getString("Make"));
                Model_Field2.setText(rs.getString("Model"));
                Colour_Field2.setText(rs.getString("Colour"));
                Years_Field2.setText(rs.getString("Years"));
            }

            conn.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
            info_viewCustomer.setText(e.getMessage());
        }
    }

    @FXML
    void edit_Vehicle(ActionEvent event) {
        try{
            vehicleItem = vehicleTable.getSelectionModel().getSelectedItem().RegNo;
        }catch (NullPointerException n){
            info_viewCustomer.setText(n.getMessage());
        }
        dbEditVehicle(vehicleItem);
        vehicleItem = "";
    }

    @FXML
    public void searchParts(KeyEvent event) {
        filterParts();
    }

    @FXML
    public void discount_open(ActionEvent actionEvent) {
        openDiscountWindow(actionEvent);
    }

    @FXML
    void refresh_partList(ActionEvent event) {
        populate_PartsList();
    }

    @FXML
    void RefreshJobList(ActionEvent event) {
        populate_JobsList();
    }

    @FXML
    void updatePart(ActionEvent event) {
        dbUpdatePart();
    }

    @FXML
    void addNewPart(ActionEvent event) {
        String Name = PartName_field1.getText();
        String Quantity = PartQuantity_field1.getText();
        String Price = PartPrice_field1.getText();
        String Manufacturer = PartManufacturer_field1.getText();
        String Description = PartDescription_field1.getText();
        String VehicleT = PartVehicle_field1.getText();
        String Threshold = PartThreshold_field1.getText();

        dbAddPart(Name, Quantity, Price, Manufacturer, Description, VehicleT, Threshold);

    }

    @FXML
    void deletePart(ActionEvent event) {
        deletePartConfirmation();
    }

    @FXML
    void searchJobs(KeyEvent event) {
        filterJobs();
    }

    @FXML
    void addCustomerNameField(ActionEvent event) {

        try{
            dbAddingCustomerName();
        }catch (Exception e){
            info_NewJob.setText(e.getMessage());
        }
    }

    @FXML
    void changeCounterText(KeyEvent event) {
        TextAreaCounter.setText(taskDescription_area.getText().length() + "");
    }

    @FXML
    void addNewJob(ActionEvent event) {

        try{
            int cID = customerId_box.getValue();
            String vRN = vehicleRegNo_box.getValue();
            String eD = estimatedDuration_field.getText();
            String dB = dateBooked_picker.getValue().toString();
            String eP = estimatedPrice_field.getText();
            String sT = serviceType_box.getValue();
            String tD = taskDescription_area.getText();
            String sP = ServicePrice_field.getText();

            dbAddingNewJob(cID, vRN, eD, dB, eP, sT, tD, sP);
            info_NewJob.setText("New Job Created");
        }catch (Exception e){
            info_NewJob.setText("Please insert the correct data");
        }


    }

    @FXML
    void SetJobMechanic(ActionEvent event) {
        String Jn = SetMechanicJobNumber_Box.getValue();
        String M = JobMehcanic_Box.getValue();
        dbSetMechanic(Jn, M);
        dbChangeJobStatus();
    }

    @FXML
    void refreshPaymentsTable(ActionEvent event){
        populate_PendingPaymentList();
    }

    @FXML
    void searchingPaymentList(KeyEvent event) {
        filterPendingPayments();
    }

    @FXML
    void completeJob(ActionEvent event){
        dbGetStatusSetComplete();
    }

    @FXML
    void payLaterJob(ActionEvent event) {
        dbGetStatusSetPayLater();
    }

    @FXML
    void ProduceJobInvoice(ActionEvent event) {
        dbGetStatus();
    }

    @FXML
    void AddPartsToJob(ActionEvent actionEvent) { openAddPartWindow(actionEvent); }

    @FXML
    void AddWorkToJob(ActionEvent actionEvent) { openAddWorkWindow(actionEvent);}

    @FXML
    void Pay(ActionEvent actionEvent) {
        openPaymentWindow(actionEvent);
    }

    @FXML
    void PrintBookings(ActionEvent actionEvent) {
        printBookings(actionEvent);
    }


    public boolean checkCustomerExists(String n, String p, String mp){

        conn = database.dbConnection.garitsConnection();

        try {
            pstmt = conn.prepareStatement("select * FROM Customer where name = ? AND postcode = ? AND mobilePhone = ?");

            pstmt.setString(1, n);
            pstmt.setString(2, p);
            pstmt.setString(3, mp);

            ResultSet r1 = pstmt.executeQuery();

            if (r1.next()){
                return true;
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void openWindow(javafx.event.ActionEvent actionEvent, String form){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/home/" + form + ".fxml"));

            Parent root = (Parent) loader.load();

            //setting stage borderless
            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            stage.initStyle(StageStyle.UNDECORATED);

            root.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    x = event.getSceneX();
                    y = event.getSceneY();
                }
            });
            root.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    stage.setX(event.getScreenX() - x);
                    stage.setY(event.getScreenY() - y);
                }
            });

            stage.show();
            // Hide this current window (if this is what you want)
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void populate_CustomerList(){
        try {
            conn = database.dbConnection.garitsConnection();

            ResultSet rs = conn.createStatement().executeQuery("select * from Customer");

            while (rs.next()){
                oblist.add(new CustomerTable(rs.getString("customerID"), rs.getString("name"),
                        rs.getString("contact"), rs.getString("address"), rs.getString("city") ,
                        rs.getString("country"), rs.getString("postcode"), rs.getString("dayPhone"), rs.getString("mobilePhone"),
                        rs.getString("accountType"), rs.getString("lastMoT")
                ));
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }


        customerID_cl.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        name_cl.setCellValueFactory(new PropertyValueFactory<>("name"));
        contact_cl.setCellValueFactory(new PropertyValueFactory<>("contact"));
        address_cl.setCellValueFactory(new PropertyValueFactory<>("address"));
        city_cl.setCellValueFactory(new PropertyValueFactory<>("city"));
        country_cl.setCellValueFactory(new PropertyValueFactory<>("country"));
        postcode_cl.setCellValueFactory(new PropertyValueFactory<>("postcode"));
        dayPhone_cl.setCellValueFactory(new PropertyValueFactory<>("dayPhone"));
        mobilePhone_cl.setCellValueFactory(new PropertyValueFactory<>("mobilePhone"));
        accountType_cl.setCellValueFactory(new PropertyValueFactory<>("accountType"));
        lastMoT_cl.setCellValueFactory(new PropertyValueFactory<>("lastMoT"));

        table.setItems(oblist);

        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void filter() {

        searchList.textProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable o) {
                if (searchList.textProperty().get().isEmpty()){
                    table.setItems(oblist);
                    return;
                }
            }
        });

        ObservableList<CustomerTable> tableItems = FXCollections.observableArrayList();
        ObservableList<TableColumn<CustomerTable, ?>> cols = table.getColumns();

        for(int i=0; i<oblist.size(); i++) {

            for(int j=0; j<cols.size(); j++) {
                TableColumn col = cols.get(j);
                String notNull = "";
                try{
                    notNull = col.getCellData(oblist.get(i)).toString();
                }catch (Exception e){
                    System.out.println(e);
                }
                String cellValue = notNull;

                cellValue = cellValue.toLowerCase();
                if(cellValue.contains(searchList.textProperty().get().toLowerCase())) {
                    tableItems.add(oblist.get(i));
                    break;
                }
            }
        }
        table.setItems(tableItems);

    }

    public void filterParts(){

        searchPartList.textProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable o) {
                if (searchPartList.textProperty().get().isEmpty()){
                    partsTable.setItems(PartList);
                    return;
                }
            }
        });

        ObservableList<PartTable> tableItems = FXCollections.observableArrayList();
        ObservableList<TableColumn<PartTable, ?>> cols = partsTable.getColumns();

        for(int i=0; i<PartList.size(); i++) {

            for(int j=0; j<cols.size(); j++) {
                TableColumn col = cols.get(j);
                String notNull = "";
                try{
                    notNull = col.getCellData(PartList.get(i)).toString();
                }catch (Exception e){
                    System.out.println(e);
                }
                String cellValue = notNull;

                cellValue = cellValue.toLowerCase();
                if(cellValue.contains(searchPartList.textProperty().get().toLowerCase())) {
                    tableItems.add(PartList.get(i));
                    break;
                }
            }
        }
        partsTable.setItems(tableItems);
    }

    public void confirmation_Box(){
        Alert alert = new Alert(Alert.AlertType.WARNING, "Please confirm to delete the customer.", ButtonType.YES, ButtonType.NO);
        alert.setTitle("DELETING CUSTOMER!");

        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {

            try {
                itemname = table.getSelectionModel().getSelectedItem().customerID;
                info_customerList.setVisible(false);
                delete_Staff_Db(itemname);
                table.getItems().removeAll(table.getSelectionModel().getSelectedItem());
                itemname = "";

            }catch (Exception e){
                info_customerList.setVisible(true);
                info_customerList.setText("Select a user to delete");
            }

        }

    }

    public void delete_Staff_Db(String itemname){
        try {
            conn = database.dbConnection.garitsConnection();
            conn.createStatement().executeQuery("DELETE FROM Customer WHERE customerID='" + itemname + "'");
            conn.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void clear_Fields(){
        name_field.clear();
        contact_field.clear();
        address_field.clear();
        city_field.clear();
        country_field.clear();
        postcode_field.clear();
        dayPhone_field.clear();
        mobilePhone_field.clear();
    }

    public void dbAddingCustomer(String n, String cn, String a, String ci, String co, String p, String dp, String mp, String at){
        conn = database.dbConnection.garitsConnection();
        String sql = "INSERT INTO Customer(name, contact, address, city, country, postcode, dayPhone, mobilePhone, accountType, lastMoT, annualService) VALUES(?,?,?,?,?,?,?,?,?,?,?)";

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate localDate = LocalDate.now();

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, n);
            pstmt.setString(2, cn);
            pstmt.setString(3, a);
            pstmt.setString(4, ci);
            pstmt.setString(5, co);
            pstmt.setString(6, p);
            pstmt.setString(7, dp);
            pstmt.setString(8, mp);
            pstmt.setString(9, at);
            pstmt.setString(10, dtf.format(localDate) );
            pstmt.setString(11, dtf.format(localDate));

            int i = pstmt.executeUpdate();
            if (i == 1){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Successfully Added New Customer!");

                alert.showAndWait();
            }

            conn.close();
        }catch (SQLException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("WARNING");
            alert.setHeaderText(null);
            alert.setContentText("Please enter correct fields!");

            alert.showAndWait();

            System.out.println(e.getMessage());
            info_NewCustomer.setText(e.getMessage());
        }
    }

    public void set_Fields(String s){
        conn = database.dbConnection.garitsConnection();
        String sql = "SELECT * FROM Customer WHERE customerID = ? ";

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, s);

            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                nameLabel.setText((rs.getString("name")));
                contactLabel.setText(rs.getString("contact"));
                addressLabel.setText(rs.getString("address"));
                cityLabel.setText(rs.getString("city"));
                countryLabel.setText(rs.getString("country"));
                postcodeLabel.setText((rs.getString("postcode")));
                dayPhoneLabel.setText(rs.getString("dayPhone"));
                mobilePhoneLabel.setText(rs.getString("mobilePhone"));
                accountTypeLabel.setText(rs.getString("accountType"));

                nameLabel1.setText((rs.getString("name")));
                contactLabel1.setText(rs.getString("contact"));
                addressLabel1.setText(rs.getString("address"));
                cityLabel1.setText(rs.getString("city"));
                countryLabel1.setText(rs.getString("country"));
                postcodeLabel1.setText((rs.getString("postcode")));
                dayPhoneLabel1.setText(rs.getString("dayPhone"));
                mobilePhoneLabel1.setText(rs.getString("mobilePhone"));
                accountTypeLabel1.setText(rs.getString("accountType"));

                customerIdLabel1.setText(rs.getString("customerID"));
                name_field1.setText((rs.getString("name")));
                contact_field1.setText(rs.getString("contact"));
                address_field1.setText(rs.getString("address"));
                city_field1.setText(rs.getString("city"));
                country_field1.setText(rs.getString("country"));
                postcode_field1.setText((rs.getString("postcode")));
                dayPhone_field1.setText(rs.getString("dayPhone"));
                mobilePhone_field1.setText(rs.getString("mobilePhone"));
                accountType_box1.setValue(rs.getString("accountType"));
            }

            conn.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void dbUpdateCustomerDetails(String s){
        conn = database.dbConnection.garitsConnection();
        String sql = "UPDATE Customer SET name = ?, contact = ?, address = ?, city = ?, country = ?, postcode = ?, dayPhone = ?, mobilePhone = ?, accountType = ? WHERE customerID = '" + s + "'";

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name_field1.getText());
            pstmt.setString(2, contact_field1.getText());
            pstmt.setString(3, address_field1.getText());
            pstmt.setString(4, city_field1.getText());
            pstmt.setString(5, country_field1.getText());
            pstmt.setString(6, postcode_field1.getText());
            pstmt.setString(7, dayPhone_field1.getText());
            pstmt.setString(8, mobilePhone_field1.getText());
            pstmt.setString(9, accountType_box1.getValue());

            int i = pstmt.executeUpdate();
            if (i == 1){
                info_editCustomer.setText("Successfully updated!");
                System.out.println("Successfully updated!");
            }

            conn.close();

        }catch (SQLException e){
            info_editCustomer.setText(e.getMessage());
        }
    }

    public void populate_VehicleList(String s){
        try {
            vehicleTable.getItems().clear();

            conn = database.dbConnection.garitsConnection();

            ResultSet rs = conn.createStatement().executeQuery("select * from Vehicle WHERE CustomercustomerID = '" + s + "'");

            while (rs.next()){
                vehicleList.add(new VehicleTable(rs.getString("RegNo"), rs.getString("Make"),
                        rs.getString("Model"), rs.getString("Colour"), rs.getString("Years"), rs.getString("CustomercustomerID")
                ));
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        RegNo_cl.setCellValueFactory(new PropertyValueFactory<>("RegNo"));
        Make_cl.setCellValueFactory(new PropertyValueFactory<>("Make"));
        Model_cl.setCellValueFactory(new PropertyValueFactory<>("Model"));
        colour_cl.setCellValueFactory(new PropertyValueFactory<>("Colour"));
        Years_cl.setCellValueFactory(new PropertyValueFactory<>("Years"));

        vehicleTable.setItems(vehicleList);

        System.out.println(vehicleTable.getItems().toString());

        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeVehicleConfirmation(){
        Alert alert = new Alert(Alert.AlertType.WARNING, "Please confirm to delete the vehicle.", ButtonType.YES, ButtonType.NO);
        alert.setTitle("DELETING VEHICLE!");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {

            try {
                vehicleItem = vehicleTable.getSelectionModel().getSelectedItem().RegNo;
                info_viewCustomer.setVisible(false);
                remove_VehicleDb(vehicleItem);
                vehicleTable.getItems().removeAll(vehicleTable.getSelectionModel().getSelectedItem());
                vehicleItem = "";

            }catch (Exception e){
                info_viewCustomer.setVisible(true);
                info_viewCustomer.setText(e.getMessage());
            }

        }

    }

    public void remove_VehicleDb(String s){
        try {
            conn = database.dbConnection.garitsConnection();
            conn.createStatement().executeQuery("DELETE FROM Vehicle WHERE RegNo='" + s + "'");
            vehicleItem = "";
            conn.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
            info_viewCustomer.setText(e.getMessage());
        }
    }

    public void clearVehicle_Fields(){
        RegNo_Field.clear();
        Make_Field.clear();
        Model_Field.clear();
        Colour_Field.clear();
        Years_Field.clear();
    }

    public void dbAddingVehicle(String r, String ma, String mo, String c, String y){
        conn = database.dbConnection.garitsConnection();
        String sql = "INSERT INTO Vehicle(RegNo, Make, Model, Colour, Years, CustomercustomerID) VALUES(?,?,?,?,?,?)";

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, r);
            pstmt.setString(2, ma);
            pstmt.setString(3, mo);
            pstmt.setString(4, c);
            pstmt.setString(5, y);
            pstmt.setString(6, itemname);

            int i = pstmt.executeUpdate();
            if (i == 1){
                System.out.println("Data inserted successfully");
                info_viewCustomer.setText("Vehicle Added Successfully.");
            }

            conn.close();

            populate_VehicleList(itemname);

        }catch (SQLException e){
            System.out.println(e.getMessage());
            info_viewCustomer.setText(e.getMessage());
        }
    }

    public void dbEditVehicle(String s){
        conn = database.dbConnection.garitsConnection();
        String sql = "UPDATE Vehicle SET Make = ?, Model = ?, Colour = ?, Years = ? WHERE RegNo = '" + s + "'";

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, Make_Field2.getText());
            pstmt.setString(2, Model_Field2.getText());
            pstmt.setString(3, Colour_Field2.getText());
            pstmt.setString(4, Years_Field2.getText());


            int i = pstmt.executeUpdate();
            if (i == 1){
                info_viewCustomer.setText("Successfully edited!");
                System.out.println("Successfully updated!");
            }



            conn.close();

            populate_VehicleList(itemname);

        }catch (SQLException e){
            info_viewCustomer.setText(e.getMessage());
        }
    }

    public void populate_PartsList(){
        try {
            partsTable.getItems().clear();
            conn = database.dbConnection.garitsConnection();

            ResultSet rs = conn.createStatement().executeQuery("select * from Part");

            while (rs.next()){
                PartList.add(new PartTable(rs.getString("PartCode"), rs.getString("PartName"),
                        rs.getString("Quantity"), rs.getString("PartPrice"), rs.getString("PartManufacturer") ,
                        rs.getString("Description"), rs.getString("VehicleType"), rs.getString("Threshold")
                ));
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
            info_partsList.setText(e.getMessage());
        }


        PartCode_cl.setCellValueFactory(new PropertyValueFactory<>("PartCode"));
        PartName_cl.setCellValueFactory(new PropertyValueFactory<>("PartName"));
        Quantity_cl.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
        Price_cl.setCellValueFactory(new PropertyValueFactory<>("PartPrice"));
        Manufacturer_cl.setCellValueFactory(new PropertyValueFactory<>("PartManufacturer"));
        Description_cl.setCellValueFactory(new PropertyValueFactory<>("Description"));
        VehicleType_cl.setCellValueFactory(new PropertyValueFactory<>("VehicleType"));
        Threshold_cl.setCellValueFactory(new PropertyValueFactory<>("Threshold"));

        partsTable.setItems(PartList);

        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            info_partsList.setText(e.getMessage());
        }
    }

    public void populate_JobsList(){
        jobsTable.getItems().clear();
        try {
            conn = database.dbConnection.garitsConnection();

            ResultSet rs = conn.createStatement().executeQuery("SELECT DISTINCT JobNumber, JobStatus, DateBooked, VehiclevehicleRegNo, \n" +
                    "VehicleCustomercustomerID, ActualPrice, ServiceType, name, Model\n" +
                    "FROM Job \n" +
                    "JOIN Customer ON (Job.VehicleCustomercustomerID = Customer.customerID) \n" +
                    "JOIN Vehicle ON (Job.VehiclevehicleRegNo = Vehicle.RegNo)");

            while (rs.next()){
                JobList.add(new JobTable(rs.getString("JobNumber"), rs.getString("DateBooked") ,
                        rs.getString("JobStatus"), rs.getString("VehiclevehicleRegNo"),
                        rs.getString("VehicleCustomercustomerID"), rs.getString("ActualPrice"),
                        rs.getString("ServiceType"), rs.getString("name"), rs.getString("Model")
                ));
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
            info_jobList.setText(e.getMessage());
        }




        JobNumber_cl.setCellValueFactory(new PropertyValueFactory<>("JobNumber"));
        Date_cl.setCellValueFactory(new PropertyValueFactory<>("DateBooked"));
        Vehicle_cl.setCellValueFactory(new PropertyValueFactory<>("VehiclevehicleRegNo"));
        Customer_cl.setCellValueFactory(new PropertyValueFactory<>("VehicleCustomercustomerID"));
        Status_cl.setCellValueFactory(new PropertyValueFactory<>("JobStatus"));

        JobCustomerName_cl.setCellValueFactory(new PropertyValueFactory<>("name"));
        JobVehicleModel_cl.setCellValueFactory(new PropertyValueFactory<>("Model"));
        ServiceType_cl.setCellValueFactory(new PropertyValueFactory<>("ServiceType"));

        jobsTable.setItems(JobList);

        System.out.println(jobsTable.getItems().toString());


        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            info_jobList.setText(e.getMessage());
        }
    }

    public void openDiscountWindow(javafx.event.ActionEvent actionEvent){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/home/discount.fxml"));

            Parent root = (Parent) loader.load();

            discountController aD = loader.getController();
            aD.setCustomer(itemname);

            //setting stage borderless
            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            stage.initStyle(StageStyle.UNDECORATED);

            root.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    x = event.getSceneX();
                    y = event.getSceneY();
                }
            });
            root.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    stage.setX(event.getScreenX() - x);
                    stage.setY(event.getScreenY() - y);
                }
            });

            stage.showAndWait();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void set_PartFields(){
        conn = database.dbConnection.garitsConnection();
        String sql = "SELECT * FROM Part WHERE PartCode = ? ";

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, partItem);

            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                PartCode_lb.setText((rs.getString("PartCode")));
                PartName_lb.setText(rs.getString("PartName"));
                PartQuantity_lb.setText(rs.getString("Quantity"));
                PartPrice_lb.setText(rs.getString("PartPrice"));
                PartManufacturer_lb.setText(rs.getString("PartManufacturer"));
                PartDescription_lb.setText((rs.getString("Description")));
                PartVehicle_lb.setText(rs.getString("VehicleType"));
                PartThreshold_lb.setText(rs.getString("Threshold"));

                PartName_field.setText(rs.getString("PartName"));
                PartQuantity_field.setText(rs.getString("Quantity"));
                PartPrice_field.setText(rs.getString("PartPrice"));
                PartManufacturer_field.setText(rs.getString("PartManufacturer"));
                PartDescription_field.setText((rs.getString("Description")));
                PartVehicle_field.setText(rs.getString("VehicleType"));
                PartThreshold_field.setText(rs.getString("Threshold"));


            }

            conn.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void dbAddPart(String n, String q, String p, String pm, String d, String v, String t){
        conn = database.dbConnection.garitsConnection();
        String sql = "INSERT INTO Part(PartName, Quantity, PartPrice, PartManufacturer, Description, VehicleType, Threshold) VALUES(?,?,?,?,?,?,?)";

        try {
            try {
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, n);
                pstmt.setInt(2, Integer.parseInt(q));
                pstmt.setDouble(3, Double.parseDouble(p));
                pstmt.setString(4, pm);
                pstmt.setString(5, d);
                pstmt.setString(6, v);
                pstmt.setInt(7, Integer.parseInt(t));

                pstmt.executeUpdate();

                info_ViewPart.setText("Added Part");

            }catch (Exception e){
                info_ViewPart.setText(e.getMessage());
            }

            conn.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
            info_ViewPart.setText(e.getMessage());
        }
    }

    public void dbUpdatePart(){
        conn = database.dbConnection.garitsConnection();
        String sql = "UPDATE Part SET PartName = ?, Quantity = ?, PartPrice = ?, PartManufacturer = ?, Description = ?, VehicleType = ?, Threshold = ? WHERE PartCode = '" + partItem + "'";

        try {
            try {
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, PartName_field.getText());
                pstmt.setInt(2, Integer.parseInt(PartQuantity_field.getText()));
                pstmt.setDouble(3, Double.parseDouble(PartPrice_field.getText()));
                pstmt.setString(4, PartManufacturer_field.getText());
                pstmt.setString(5, PartDescription_field.getText());
                pstmt.setString(6, PartVehicle_field.getText());
                pstmt.setInt(7, Integer.parseInt(PartThreshold_field.getText()));

                pstmt.executeUpdate();

                info_ViewPart.setText("Successfully updated!");
                System.out.println("Successfully updated!");
            }catch (Exception e){
                info_ViewPart.setText(e.getMessage());
            }

            conn.close();

        }catch (SQLException e){
            info_ViewPart.setText(e.getMessage());
        }
    }

    public void filterJobs(){

        searchJobList.textProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable o) {
                if (searchJobList.textProperty().get().isEmpty()){
                    jobsTable.setItems(JobList);
                    return;
                }
            }
        });

        ObservableList<JobTable> tableItems = FXCollections.observableArrayList();
        ObservableList<TableColumn<JobTable, ?>> cols = jobsTable.getColumns();

        for(int i=0; i<JobList.size(); i++) {

            for(int j=0; j<cols.size(); j++) {
                TableColumn col = cols.get(j);
                String notNull = "";
                try{
                    notNull = col.getCellData(JobList.get(i)).toString();
                }catch (Exception e){
                    System.out.println(e);
                }
                String cellValue = notNull;

                cellValue = cellValue.toLowerCase();
                if(cellValue.contains(searchJobList.textProperty().get().toLowerCase())) {
                    tableItems.add(JobList.get(i));
                    break;
                }
            }
        }
        jobsTable.setItems(tableItems);
    }

    public void deletePartConfirmation(){
        Alert alert = new Alert(Alert.AlertType.WARNING, "Please confirm to delete the part.", ButtonType.YES, ButtonType.NO);
        alert.setTitle("DELETING VEHICLE!");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            remove_partDb();
        }

    }

    public void remove_partDb(){
        try {
            conn = database.dbConnection.garitsConnection();
            conn.createStatement().executeQuery("DELETE FROM Part WHERE PartCode='" + partItem + "'");
            conn.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
            info_ViewPart.setText("Part Deleted Successfully");
        }
    }

    public void populate_PendingPaymentList(){
        paymentsTable.getItems().clear();

        try {
            conn = database.dbConnection.garitsConnection();

            ResultSet rs = conn.createStatement().executeQuery("SELECT DISTINCT JobNumber, DateBooked, JobStatus, VehiclevehicleRegNo, \n" +
                    "VehicleCustomercustomerID, ActualPrice, ServiceType, name, Model\n" +
                    "FROM Job \n" +
                    "JOIN Customer ON (Job.VehicleCustomercustomerID = Customer.customerID) \n" +
                    "JOIN Vehicle ON (Job.VehiclevehicleRegNo = Vehicle.RegNo)\n" +
                    "WHERE JobStatus = 'NotPaid'");

            while (rs.next()){
                PendingList.add(new JobTable(rs.getString("JobNumber"), rs.getString("DateBooked") ,
                        rs.getString("JobStatus"), rs.getString("VehiclevehicleRegNo"),
                        rs.getString("VehicleCustomercustomerID"), rs.getString("ActualPrice"),
                        rs.getString("ServiceType"), rs.getString("name"), rs.getString("Model")
                ));
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
            info_PendingPaymentList.setText(e.getMessage());
        }

        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            info_PendingPaymentList.setText(e.getMessage());
        }

        PendingCustomerID_cl.setCellValueFactory(new PropertyValueFactory<>("VehicleCustomercustomerID"));
        PendingAmount_cl.setCellValueFactory(new PropertyValueFactory<>("ActualPrice"));
        PendingDate_cl.setCellValueFactory(new PropertyValueFactory<>("DateBooked"));

        PendingCustomerName_cl.setCellValueFactory(new PropertyValueFactory<>("name"));
        PendingJobNumber_cl.setCellValueFactory(new PropertyValueFactory<>("JobNumber"));
        PendingVehicleModel_cl.setCellValueFactory(new PropertyValueFactory<>("Model"));
        PendingJobStatus_cl.setCellValueFactory(new PropertyValueFactory<>("JobStatus"));

        paymentsTable.setItems(PendingList);

    }

    public void setNewJobFields(){
        dbAddCustomerNameJob();
        serviceType_box.getItems().clear();
        serviceType_box.getItems().addAll("MoT", "Service Repair", "Annual Service");
    }

    public void dbAddCustomerNameJob(){
        customerId_box.getItems().clear();
        conn = database.dbConnection.garitsConnection();

        ResultSet rs = null;
        try {
            rs = conn.createStatement().executeQuery("SELECT customerID FROM Customer ");

            while (rs.next()){
                customerId_box.getItems().add(rs.getInt("customerID"));
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void dbAddingCustomerName(){
        conn = database.dbConnection.garitsConnection();

        ResultSet rs = null;
        try {
            rs = conn.createStatement().executeQuery("SELECT name FROM Customer WHERE customerID = '" + customerId_box.getValue() + "'");

            while(rs.next()){
                customerNameJob.setText(rs.getString("name"));
            }

            conn.close();

            dbAddVehicleJob(customerId_box.getValue());

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void dbAddVehicleJob(int i){

        vehicleRegNo_box.getItems().clear();

        conn = database.dbConnection.garitsConnection();

        ResultSet rs = null;
        try {
            rs = conn.createStatement().executeQuery("SELECT RegNo FROM Vehicle WHERE CustomercustomerID = '" + customerId_box.getValue() + "'");

            while (rs.next()){
                vehicleRegNo_box.getItems().add(rs.getString("RegNo"));
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dbAddingNewJob(int cID, String vRN, String eD, String dB, String eP, String sT, String tD, String sp){
        conn = database.dbConnection.garitsConnection();
        String sql = "INSERT INTO Job(EstimatedDuration, ActualDuration, TaskDescription, DateBooked, JobStatus, VehiclevehicleRegNo, VehicleCustomercustomerID, EstimatedPrice, ServiceType, ServicePrice, Month) VALUES(?,?,?,?,?,?,?,?,?,?,?)";

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(eD));
            pstmt.setInt(2, 0);
            pstmt.setString(3, tD);
            pstmt.setString(4, dB);
            pstmt.setString(5, "Pending");
            pstmt.setString(6, vRN);
            pstmt.setInt(7, cID);
            pstmt.setInt(8 , Integer.parseInt(eP));
            pstmt.setString(9, sT);
            pstmt.setString(10, sp);
            pstmt.setInt(11, LocalDateTime.now().getMonth().getValue());


            int i = pstmt.executeUpdate();
            if (i == 1){
                System.out.println("Data inserted successfully");
            }

            conn.close();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Successfully Added New Job!");

            alert.showAndWait();

        }catch (SQLException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("WARNING");
            alert.setHeaderText(null);
            alert.setContentText("The data you entered is incorrect!");

            alert.showAndWait();
        }
    }

    public void set_JobViewer(String s){
        JobNumberLabel.setText(s);

        dbGetJobDetails(s);

        dbGetJobMechanics(s);

        dbSetCustomerDetails(JobCustomerID);

        String vD = JobVehicleRegLabel.getText();

        dbSetVehicleDetails(vD);

        populate_TaskDone();

        populate_JobPartList();


    }

    public void dbGetJobDetails(String s){

        conn = database.dbConnection.garitsConnection();

        ResultSet rs = null;
        try {
            rs = conn.createStatement().executeQuery("SELECT * FROM Job WHERE JobNumber = '" + s + "'");

            while (rs.next()){
                JobVehicleRegLabel.setText(rs.getString("VehiclevehicleRegNo"));
                JobStatusLabel.setText(rs.getString("JobStatus"));
                JobDateLabel.setText(rs.getString("DateBooked"));
                EstimatedTimeLabel.setText(rs.getString("EstimatedDuration"));
                workRequiredLabel.setText("Work Required: \n" + rs.getString("TaskDescription"));
                JobCustomerID = rs.getString("VehicleCustomercustomerID");
                JobType.setText(rs.getString("ServiceType"));
                ActualTimeLabel.setText(rs.getString("ActualDuration"));
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void dbGetJobMechanics(String s){

        MechanicLabel.setText("");

        conn = database.dbConnection.garitsConnection();

        ResultSet rs = null;
        try {
            rs = conn.createStatement().executeQuery("SELECT * FROM JobStaff WHERE JobStaffJobNumber = '" + s + "'");

            while (rs.next()){
                MechanicLabel.setText( rs.getString("JobStaffUsername") + " , " + MechanicLabel.getText() );
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void dbSetCustomerDetails(String s){
        conn = database.dbConnection.garitsConnection();

        ResultSet rs = null;
        try {
            rs = conn.createStatement().executeQuery("SELECT * FROM Customer WHERE customerID = '" + s + "'");

            while (rs.next()){
                JobCustomerNameLabel.setText(rs.getString("name"));
                JobAddressLabel.setText(rs.getString("address"));
                JobCityLabel.setText(rs.getString("city"));
                JobCountryLabel.setText(rs.getString("country"));
                JobPostcodeLabel.setText(rs.getString("postcode"));
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dbSetVehicleDetails(String s){
        conn = database.dbConnection.garitsConnection();

        ResultSet rs = null;
        try {
            rs = conn.createStatement().executeQuery("SELECT * FROM Vehicle WHERE RegNo = '" + s + "'");

            while (rs.next()){
                JobMakeLabel.setText(rs.getString("Make"));
                JobModelLabel.setText(rs.getString("Model"));
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void populate_TaskDone(){
        try {
            JobTasksTable.getItems().clear();

            conn = database.dbConnection.garitsConnection();

            ResultSet rs = conn.createStatement().executeQuery("select * from Tasks WHERE JobJobNumber = '" + jobItem + "'");

            while (rs.next()){
                TaskList.add(new TaskTable(rs.getString("TaskName"), rs.getString("Duration"),
                        rs.getString("JobJobNumber"), rs.getString("TaskMechanic")
                ));
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        TaskName_cl.setCellValueFactory(new PropertyValueFactory<>("TaskName"));
        TaskDuration_cl.setCellValueFactory(new PropertyValueFactory<>("Duration"));
        TaskMechanic_cl.setCellValueFactory(new PropertyValueFactory<>("TaskMechanic"));

        JobTasksTable.setItems(TaskList);

        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void populate_JobPartList(){
        try {
            JobPartsTable.getItems().clear();
            conn = database.dbConnection.garitsConnection();

            ResultSet rs = conn.createStatement().executeQuery("SELECT DISTINCT PartName, PartPrice, PartQuantity FROM Job JOIN PartUsed ON (PartUsed.JobJobNumber = '" + jobItem +  "') JOIN Part ON (Part.PartCode = PartUsed.PartPartCode)");


            while (rs.next()){
                JobPartList.add(new PartJobTable(rs.getString("PartName"), rs.getString("PartPrice"),
                        rs.getString("PartQuantity")
                ));
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }


        JobPartName_cl.setCellValueFactory(new PropertyValueFactory<>("PartName"));
        JobQuantity_cl.setCellValueFactory(new PropertyValueFactory<>("PartQuantity"));
        JobPrice_cl.setCellValueFactory(new PropertyValueFactory<>("PartPrice"));

        JobPartsTable.setItems(JobPartList);

        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addJobNumbers(){
        SetMechanicJobNumber_Box.getItems().clear();

        conn = database.dbConnection.garitsConnection();

        ResultSet rs = null;
        try {
            rs = conn.createStatement().executeQuery("SELECT JobNumber FROM Job WHERE JobStatus = 'Pending' OR JobStatus = 'Ongoing'");

            while (rs.next()){
                SetMechanicJobNumber_Box.getItems().add(rs.getString("JobNumber"));
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addMechanicNames(){
        JobMehcanic_Box.getItems().clear();

        conn = database.dbConnection.garitsConnection();

        ResultSet rs = null;
        try {
            rs = conn.createStatement().executeQuery("SELECT username FROM Staff WHERE Position = 'Mechanic' OR Position = 'Foreman' ");

            while (rs.next()){
                JobMehcanic_Box.getItems().add(rs.getString("username"));
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dbSetMechanic(String Jn, String M){
        conn = database.dbConnection.garitsConnection();
        String sql = "INSERT INTO JobStaff (JobStaffUsername, JobStaffJobNumber) VALUES (?, ?)";

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, M);
            pstmt.setString(2, Jn);

            int i = pstmt.executeUpdate();
            if (i == 1){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText(null);
                alert.setContentText("You added a mechanic to the job!");

                alert.showAndWait();
            }

            conn.close();

        }catch (SQLException e){
            System.out.println(e.getStackTrace());
        }
    }

    public void filterPendingPayments() {


        searchPaymentList.textProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable o) {
                if (searchPaymentList.textProperty().get().isEmpty()){
                    paymentsTable.setItems(PendingList);
                    return;
                }
            }
        });

        ObservableList<JobTable> tableItems = FXCollections.observableArrayList();
        ObservableList<TableColumn<JobTable, ?>> cols = paymentsTable.getColumns();

        for(int i=0; i<PendingList.size(); i++) {

            for(int j=0; j<cols.size(); j++) {
                TableColumn col = cols.get(j);
                String notNull = "";
                try{
                    notNull = col.getCellData(PendingList.get(i)).toString();
                }catch (Exception e){
                    System.out.println(e);
                }
                String cellValue = notNull;

                cellValue = cellValue.toLowerCase();
                if(cellValue.contains(searchPaymentList.textProperty().get().toLowerCase())) {
                    tableItems.add(PendingList.get(i));
                    break;
                }
            }
        }
        paymentsTable.setItems(tableItems);

    }

    public void dbGetStatusSetComplete(){
        conn = database.dbConnection.garitsConnection();
        String sql = "UPDATE Job SET JobStatus = 'Complete' WHERE JobNumber = ? AND JobStatus = 'Ongoing'";

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, jobItem);

            int i = pstmt.executeUpdate();
            if (i == 1){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText(null);
                alert.setContentText("Job is now complete!");

                alert.showAndWait();
            }else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("You can only complete jobs which status are OnGoing!");

                alert.showAndWait();
            }

            conn.close();

        }catch (SQLException e){
            System.out.println(e.getStackTrace());
        }
    }

    public void dbGetStatusSetPayLater(){
        conn = database.dbConnection.garitsConnection();
        String sql = "UPDATE Job SET JobStatus = 'NotPaid' WHERE JobNumber = ? AND JobStatus = 'Complete'";

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, jobItem);

            int i = pstmt.executeUpdate();
            if (i == 1){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText(null);
                alert.setContentText("Job is now set to pay later!");

                alert.showAndWait();
            }else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("You can only pay later for jobs that are complete!");

                alert.showAndWait();
            }

            conn.close();

        }catch (SQLException e){
            System.out.println(e.getStackTrace());
        }
    }

    public void dbGetStatus(){

        String JS = "";

        conn = database.dbConnection.garitsConnection();
        String sql = "SELECT JobStatus FROM Job WHERE JobNumber = ?";

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, jobItem);

            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                JS = rs.getString("JobStatus");
            }
            conn.close();

        }catch (SQLException e){
            System.out.println(e.getStackTrace());
        }

        if (JS.equals("Complete") || JS.equals("NotPaid")){
            JI jb = new JI();
            try {
                jb.Main(jobItem);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("You can only create invoices for paid jobs!");

            alert.showAndWait();
        }



    }

    public void openAddWorkWindow(javafx.event.ActionEvent actionEvent){


        String JS = "";

        conn = database.dbConnection.garitsConnection();
        String sql = "SELECT JobStatus FROM Job WHERE JobNumber = ?";

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, jobItem);

            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                JS = rs.getString("JobStatus");
            }
            conn.close();

        }catch (SQLException e){
            System.out.println(e.getStackTrace());
        }

        if (JS.equals("Ongoing")){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/home/addWork.fxml"));

                Parent root = (Parent) loader.load();

                addWorkController aD = loader.getController();
                aD.SetJobCustomer(jobItem, JobCustomerNameLabel.getText());

                //setting stage borderless
                Stage stage = new Stage();
                stage.setScene(new Scene(root));

                stage.initStyle(StageStyle.UNDECORATED);

                root.setOnMousePressed(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        x = event.getSceneX();
                        y = event.getSceneY();
                    }
                });
                root.setOnMouseDragged(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        stage.setX(event.getScreenX() - x);
                        stage.setY(event.getScreenY() - y);
                    }
                });

                stage.showAndWait();

            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("You can only add tasks to ongoing jobs!");

            alert.showAndWait();
        }




    }

    public void openAddPartWindow(javafx.event.ActionEvent actionEvent){


        String JS = "";

        conn = database.dbConnection.garitsConnection();
        String sql = "SELECT JobStatus FROM Job WHERE JobNumber = ?";

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, jobItem);

            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                JS = rs.getString("JobStatus");
            }
            conn.close();

        }catch (SQLException e){
            System.out.println(e.getStackTrace());
        }

        if (JS.equals("Ongoing")){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/home/addParts.fxml"));

                Parent root = (Parent) loader.load();

                addPartsController aD = loader.getController();
                aD.SetJobCustomer(jobItem, JobCustomerNameLabel.getText());

                //setting stage borderless
                Stage stage = new Stage();
                stage.setScene(new Scene(root));

                stage.initStyle(StageStyle.UNDECORATED);

                root.setOnMousePressed(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        x = event.getSceneX();
                        y = event.getSceneY();
                    }
                });
                root.setOnMouseDragged(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        stage.setX(event.getScreenX() - x);
                        stage.setY(event.getScreenY() - y);
                    }
                });

                stage.showAndWait();

            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("You can only add parts to ongoing jobs!");

            alert.showAndWait();
        }




    }

    public void openPaymentWindow(javafx.event.ActionEvent actionEvent){


        String JS = "";

        conn = database.dbConnection.garitsConnection();
        String sql = "SELECT JobStatus FROM Job WHERE JobNumber = ?";

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, jobItem);

            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                JS = rs.getString("JobStatus");
            }
            conn.close();

        }catch (SQLException e){
            System.out.println(e.getStackTrace());
        }

        if (JS.equals("Complete") || JS.equals("NotPaid")){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/home/payment.fxml"));

                Parent root = (Parent) loader.load();

                paymentController aD = loader.getController();
                aD.setAllDetails(Integer.parseInt(jobItem));

                //setting stage borderless
                Stage stage = new Stage();
                stage.setScene(new Scene(root));

                stage.initStyle(StageStyle.UNDECORATED);

                root.setOnMousePressed(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        x = event.getSceneX();
                        y = event.getSceneY();
                    }
                });
                root.setOnMouseDragged(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        stage.setX(event.getScreenX() - x);
                        stage.setY(event.getScreenY() - y);
                    }
                });

                stage.showAndWait();

            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("You can only pay for jobs that are completed!");

            alert.showAndWait();
        }


    }

    public void dbChangeJobStatus(){
        conn = database.dbConnection.garitsConnection();
        String sql = "UPDATE Job SET JobStatus = 'Ongoing' WHERE JobNumber = '"+ SetMechanicJobNumber_Box.getValue()+"'";

        try {
            pstmt = conn.prepareStatement(sql);

            int i = pstmt.executeUpdate();
            conn.close();

        }catch (SQLException e){
            System.out.println(e.getStackTrace());
        }
    }

    public void autoNotifications(int m, int s){

        int M = 1;

        try {
            if ((m % M == 0) && s == 0){
                System.out.println("Done");
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Replenish stock", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
                alert.setTitle("Replenish");
                alert.setHeaderText(null);
                if (alert.getResult() == ButtonType.YES) {
                    PO p= new PO();
                    try {
                        p.Main();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }

                alert.showAndWait();
            }
        }catch (Exception e){

        }

    }

    public void printBookings(ActionEvent event){
        serviceMonthlyReport s = new serviceMonthlyReport();
        try {
            s.Main(Month_Box.getValue(), ServiceTypeBooking_Box.getValue());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Month_Box.getValue();
        ServiceTypeBooking_Box.getValue();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        info_customerList.setText("");
        info_editCustomer.setText("");
        info_NewCustomer.setText("");
        info_viewCustomer.setText("");
        info_partsList.setText("");
        info_jobList.setText("");
        info_PendingPaymentList.setText("");
        info_Dashboard.setText("");

        runDateTime();
        populate_CustomerList();
        populate_PartsList();
        populate_JobsList();
        populate_PendingPaymentList();

        accountType_box.getItems().addAll("Casual", "Account Holder");
        accountType_box1.getItems().addAll("Casual", "Account Holder");

        customerNameJob.setDisable(true);

        taskDescription_area.setWrapText(true);

        taskDescription_area.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= 200 ? change : null));

        RegNo_Field2.setDisable(true);

        workRequiredLabel.setWrapText(true);

        addJobNumbers();
        addMechanicNames();

    }


}
