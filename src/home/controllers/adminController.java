package home.controllers;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import jdk.internal.org.objectweb.asm.tree.LocalVariableAnnotationNode;

public class adminController implements Initializable {

    private Connection conn = null;
    PreparedStatement pstmt = null;

    public String sNameLogin = ""; //Username
    public String sPositionLogin = ""; //Position

    public void setStaffNamePosition (String staffName, String staffPosition){
        this.sNameLogin = staffName; // Set staff username
        this.sPositionLogin = staffPosition; // Login depending on the position

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

            autoUpdateDay(minute, hour, second);

        }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    private double x = 0;
    private double y = 0;

    int DatabaseNumber = 0;

    public String itemname = "";


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
    private Label databaseBackedDate;

    @FXML
    private Label confirmationText;

    @FXML
    private Label info_userList;


    @FXML
    private TextField searchList;

    //CreateTableView to show list of items
    @FXML
    private TableView<StaffTable> table;

    @FXML
    private TableColumn<StaffTable, String> username_cl;

    @FXML
    private TableColumn<StaffTable, String> name_cl;

    @FXML
    private TableColumn<StaffTable, String> password_cl;

    @FXML
    private TableColumn<StaffTable, String> position_cl;

    @FXML
    private TableColumn<StaffTable, String> hourlyRate_cl;

    ObservableList<StaffTable> oblist = FXCollections.observableArrayList();


    @FXML
    private Label info_RegisterUser;

    @FXML
    private TextField name_field;

    @FXML
    private TextField username_field;

    @FXML
    private TextField password_field;

    @FXML
    private TextField hourlyRate_field;

    @FXML
    private ComboBox<String> position_Box;

    @FXML
    private Label info_editUser;

    @FXML
    private Label name_lb;

    @FXML
    private Label username_lb;

    @FXML
    private Label password_lb;

    @FXML
    private Label hourlyRate_lb;

    @FXML
    private Label position_lb;

    @FXML
    private TextField nameEdit_field;

    @FXML
    private Label usernameEdit_field;

    @FXML
    private TextField passwordEdit_field;

    @FXML
    private TextField hourlyRateEdit_field;

    @FXML
    private ComboBox<String> positionEdit_box;

    @FXML
    private Label dbPD_lb;

    @FXML
    private ComboBox<Integer> databasePeriodDay_box;

    @FXML
    private Label dbPM_lb;

    @FXML
    private ComboBox<Integer> databasePeriodMinute_box;


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
    void sign_out(ActionEvent event) {
        openWindow(event, "login");
    }

    @FXML
    void dashboard_open(ActionEvent event) {
        tabPane.getSelectionModel().select(0);
    }

    @FXML
    void userList_open(ActionEvent event) {
        tabPane.getSelectionModel().select(1);
    }

    @FXML
    void registerUser_open(ActionEvent event) {
        tabPane.getSelectionModel().select(2);
    }

    @FXML
    void backup_Database(ActionEvent event) throws IOException {
        dbBackup();
    }

    @FXML
    void restore_Database(ActionEvent event) {
        String sl = "SELECT * FROM Data WHERE num = '1'";

        try {
            Connection conn = database.dbConnection.garitsConnection();

            PreparedStatement pstmt;

            pstmt = conn.prepareStatement(sl);


            ResultSet rs = pstmt.executeQuery();

            DatabaseNumber = rs.getInt("databaseNumber");

            conn.close();

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        Path b = Paths.get("src/database/backup/" + DatabaseNumber + ".db");
        Path e = Paths.get("src/database/GARITSdb.db");

        try {
            Files.delete(e);
            Files.copy(b, e);

            confirmationText.setVisible(true);
            confirmationText.setText("You successfully restored your database to database N: " + DatabaseNumber);

            System.out.println("Database restored");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    void deleteStaffList(ActionEvent event) {
        confirmation_Box();
    }

    @FXML
    void editStaffList(ActionEvent event) {
        try{
            itemname = table.getSelectionModel().getSelectedItem().username;
            info_userList.setVisible(false);
            tabPane.getSelectionModel().select(3);
            set_Fields(itemname);

        }catch (Exception e){
            info_userList.setVisible(true);
            info_userList.setText("Please select an item from the list");
        }
    }

    @FXML
    void refreshList(ActionEvent event) {
        searchList.setText("");
        table.getItems().clear();
        populate_StaffList();
    }

    @FXML
    void edit_Staff(ActionEvent event) {
        dbUpdateStaffDetails(itemname);
    }

    @FXML
    void register_User(ActionEvent event){
        String Name = name_field.getText();
        String Username = username_field.getText();
        String Password = password_field.getText();
        String HourlyRate = hourlyRate_field.getText();
        String Position = position_Box.getValue();

        if(check_Username(Username)){
            info_RegisterUser.setText("User already exists!");
        }else{
            dbAddingUser(Name, Username, Password, Position, HourlyRate);
            clear_Fields();
            info_RegisterUser.setText("Successfully created user!");

        }

        System.out.println("Clicked register user");
    }

    @FXML
    void searchUsers(KeyEvent event) {
        filter();
    }

    @FXML
    void updateDatabasePeriod(ActionEvent event) {
        dbUpdatePeriod();
    }


    public void filter() {
        //Searchlist
        searchList.textProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable o) {
                if (searchList.textProperty().get().isEmpty()){ // If search list is empty
                    table.setItems(oblist); // Set Table to oblist (ObservableList) of list of staffs
                    return;
                }
            }
        });

            ObservableList<StaffTable> tableItems = FXCollections.observableArrayList();
            ObservableList<TableColumn<StaffTable, ?>> cols = table.getColumns();

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

    //Opens window
    public void openWindow(javafx.event.ActionEvent actionEvent, String form){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/home/" + form + ".fxml"));//Opens window depending on the form

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

    //Sets when the database was last backed up

    public void dbSetDateLastChecked (){
        String sl = "SELECT * FROM Data WHERE num = '1'"; // Select All rows from data where num = 1

        try {
            Connection conn = database.dbConnection.garitsConnection();

            PreparedStatement pstmt;

            pstmt = conn.prepareStatement(sl);


            ResultSet rs = pstmt.executeQuery();

            DatabaseNumber = rs.getInt("databaseNumber"); // Database = value of cell under column databasenumber

            conn.close();

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        Path b = Paths.get("src/database/backup/" + DatabaseNumber + ".db"); //Gets the last backed up database

        try {
            BasicFileAttributes atr = Files.readAttributes(b, BasicFileAttributes.class ); // allows us to read the attributes of file

            LocalDateTime dT = atr.creationTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(); //Get the time attribute from file

            System.out.println(dT);

            databaseBackedDate.setText("Database Last Backed: " + dT); // Print the creation data of last backed up database

        } catch (IOException e) {
            e.printStackTrace();
        }

        confirmationText.setVisible(false);
    }

    //Opens confirmation box
    public void confirmation_Box(){


        Alert alert = new Alert(Alert.AlertType.WARNING, "Please confirm to delete the user.", ButtonType.YES, ButtonType.NO);
        alert.setTitle("DELETING USER!");

        alert.showAndWait(); // Waits until user has pressed something

        // If selection is yes then
        if (alert.getResult() == ButtonType.YES) {

            //Deletes user that is selected
            try {
                itemname = table.getSelectionModel().getSelectedItem().username;
                info_userList.setVisible(false);
                delete_Staff_Db(itemname);
                table.getItems().removeAll(table.getSelectionModel().getSelectedItem());
                itemname = "";

            }catch (Exception e){
                info_userList.setVisible(true);
                info_userList.setText("Select a user to delete");
            }

        }

    }

    //Delete staff using passed in variable itemname
    public void delete_Staff_Db(String itemname){
        try {
            conn = database.dbConnection.garitsConnection(); // Get connection
            conn.createStatement().executeQuery("DELETE FROM STAFF WHERE username='" + itemname + "'"); // SQL statement to delete where username = value we pass in
            conn.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    //Populate stafflist
    public void populate_StaffList(){
        try {
            conn = database.dbConnection.garitsConnection();

            ResultSet rs = conn.createStatement().executeQuery("select * from STAFF"); // Select all rows from Table Staff

            //While there is still rows
            while (rs.next()){
                //Add staff rows into observableList
                oblist.add(new StaffTable(rs.getString("username"), rs.getString("name"),
                        rs.getString("password"), rs.getString("position"), rs.getString("hourlyRate")
                ));
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }


        username_cl.setCellValueFactory(new PropertyValueFactory<>("username")); // Sets username lable to the value of username from database
        name_cl.setCellValueFactory(new PropertyValueFactory<>("name"));  // Sets username lable to the value of name from database
        password_cl.setCellValueFactory(new PropertyValueFactory<>("password"));  // Sets username lable to the value of password  from database
        position_cl.setCellValueFactory(new PropertyValueFactory<>("position"));  // Sets username lable to the value of position from database
        hourlyRate_cl.setCellValueFactory(new PropertyValueFactory<>("hourlyRate"));  // Sets username lable to the value of hourlyRate from database

        table.setItems(oblist); //Sets the table with the list that we populated

        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //Checks username
    public boolean check_Username(String username){
        conn = database.dbConnection.garitsConnection();

        try {
            pstmt = conn.prepareStatement("select * FROM STAFF where username ='" + username + "'"); //SQL statement to get rows of Staff where username = value passed in

            ResultSet r1 = pstmt.executeQuery();

            //If there is rows left
            if (r1.next()){
                return true; //Return true
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; //Return false if no rows left
    }

    //Add user to database
    public void dbAddingUser(String n, String u, String P, String a, String h){
        conn = database.dbConnection.garitsConnection();
        String sql = "INSERT INTO Staff(username, password, position, hourlyRate, name) VALUES(?,?,?,?,?)"; // Sql statement to insert staff details

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, u); // Insert passed in variable to username column
            pstmt.setString(2, P); // Insert passed in variable to password column
            pstmt.setString(3, a); // Insert passed in variable to position column
            pstmt.setDouble(4, Double.parseDouble(h)); // Insert passed in variable to hourlyRate column
            pstmt.setString(5, n); // Insert passed in variable to name column

            int i = pstmt.executeUpdate();
            if (i == 1){
                System.out.println("Data inserted successfully");
            }

            conn.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    //Clears text fields/field boxes
    public void clear_Fields(){
        name_field.clear(); // Clears whatever is in name field box
        username_field.clear(); // Clears whatever is in username field box
        password_field.clear(); // Clears whatever is in password field box
        hourlyRate_field.clear(); // Clears whatever is in hourlyRate field box
    }

    //Set fields
    public void set_Fields(String s){
        conn = database.dbConnection.garitsConnection();
        String sql = "SELECT * FROM STAFF WHERE username = ? ";

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, s);

            ResultSet rs = pstmt.executeQuery();

            //while there is still rows
            while(rs.next()){
                //set labels to what rs.get gives
                username_lb.setText((rs.getString("username")));
                name_lb.setText(rs.getString("name"));
                password_lb.setText(rs.getString("password"));
                hourlyRate_lb.setText(rs.getString("hourlyRate"));
                position_lb.setText(rs.getString("position"));

                //set field boxes to what rs.get gives
                usernameEdit_field.setText((rs.getString("username")));
                nameEdit_field.setText(rs.getString("name"));
                passwordEdit_field.setText(rs.getString("password"));
                hourlyRateEdit_field.setText(rs.getString("hourlyRate"));
                positionEdit_box.setValue(rs.getString("position"));
            }

            conn.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    //Updates staff details
    public void dbUpdateStaffDetails(String s){
        conn = database.dbConnection.garitsConnection();
        String sql = "UPDATE Staff SET name = ?, password = ?, position = ?, hourlyRate = ? WHERE username = '" + s + "'"; // Update staff where username = parameter passed

        try {
            //Updates the staff details depending on value of the fields
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nameEdit_field.getText());
            pstmt.setString(2, passwordEdit_field.getText());
            pstmt.setString(3, positionEdit_box.getValue());
            pstmt.setDouble(4, Double.parseDouble(hourlyRateEdit_field.getText()));

            int i = pstmt.executeUpdate();
            if (i == 1){
                info_editUser.setText("Successfully updated!");
                System.out.println("Successfully updated!");
            }

            conn.close();

        }catch (SQLException e){
            info_editUser.setText(e.getMessage());
        }
    }

    // Function to back up database
    public void dbBackup() {
        String sl = "SELECT * FROM Data WHERE num = '1'"; // SQL select all rows from Table Data where num = '1'

        try {
            Connection conn = database.dbConnection.garitsConnection();

            PreparedStatement pstmt;

            pstmt = conn.prepareStatement(sl);


            ResultSet rs = pstmt.executeQuery();

            DatabaseNumber = rs.getInt("databaseNumber"); // Gets value of databaseNumber and store into variable

            conn.close();

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        DatabaseNumber ++; // Increase variable

        String sql = "UPDATE Data SET databaseNumber = ? WHERE num = '1'"; // Updates the databaseNumber that = to '1'

        try {
            Connection conn = database.dbConnection.garitsConnection();

            PreparedStatement pstmt;


            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, DatabaseNumber); // Replaces with incremented variable

            int i = pstmt.executeUpdate();
            if (i == 1){
                Path b = Paths.get("src/database/GARITSdb.db"); // Gets original .db
                Path e = Paths.get("src/database/backup/" + DatabaseNumber + ".db"); //Gets backed up db

                Files.copy(b, e); // Copy the backed up db to the db that we use (GARITSdb.db);
            }

            confirmationText.setVisible(true);
            confirmationText.setText("You successfully backed your database");

            conn.close();

            System.out.println("Databse backed");

        }catch (SQLException e){
            System.out.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int Day, Minute, yearDay; // Int variables Day,Minute,yearDay

    public void autoUpdateDay(int m, int h, int s){

        yearDay = LocalDateTime.now().getDayOfYear(); // Get the day in years so out of 365 days

        try{
            //If day mod current Day = 0 and the hour of the day = 17 then call dbBackup
            if ((yearDay % Day == 0) && h == 17 && m == 0 && s == 0){
                dbBackup();//Function that copys content from backup to the db that is going to be used
            }
        }catch (Exception e){
            e.getStackTrace();
        }

        try {
            //Testing purposes as we do not want to wait a day to see if it worked
            if ((m % Minute == 0) && s == 0){
                System.out.println("Done");
                dbBackup();
            }
        }catch (Exception e){
            e.getStackTrace();
        }

    }

    //Backs up data
    public void setAutoUpdateData(){
        try {
            Connection conn = database.dbConnection.garitsConnection();
            String sl = "SELECT * FROM AutomaticPeriods";

            pstmt = conn.prepareStatement(sl);
            ResultSet rs = pstmt.executeQuery();

            Day = rs.getInt("Day");
            Minute = rs.getInt("Minute");

            dbPM_lb.setText("Database Updates Every: " + Minute + "Minutes");
            dbPD_lb.setText("Database Updates Every: " + Day + "Days");

            databasePeriodDay_box.setValue(Day);
            databasePeriodMinute_box.setValue(Minute);

            conn.close();
        }catch (SQLException e){
            e.getStackTrace();
        }
    }

    //Changes db update period
    public void dbUpdatePeriod(){
        conn = database.dbConnection.garitsConnection();
        String sql = "UPDATE AutomaticPeriods SET Day = ?, Minute = ?";

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, databasePeriodDay_box.getValue()); //Set Day that we want to update
            pstmt.setInt(2, databasePeriodMinute_box.getValue()); // Set the minute we want to update


            int i = pstmt.executeUpdate();
            if (i == 1){
                confirmationText.setText("Successfully updated period!");
                System.out.println("Successfully updated!");
            }

            conn.close();

        }catch (SQLException e){
            confirmationText.setText(e.getMessage());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        runDateTime();
        dbSetDateLastChecked();
        populate_StaffList();
        position_Box.getItems().addAll("Franchise", "Receptionist" , "Mechanic", "Foreperson");
        positionEdit_box.getItems().addAll("Franchise", "Receptionist" , "Mechanic", "Foreperson");

        info_userList.setText("");
        info_editUser.setText("");
        info_RegisterUser.setText("");

        databasePeriodDay_box.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10); //Back up database in days selected
        databasePeriodMinute_box.getItems().addAll(0, 1, 2, 5, 10, 15, 30); // Backup database in minutes selected

        setAutoUpdateData();

    }



}
