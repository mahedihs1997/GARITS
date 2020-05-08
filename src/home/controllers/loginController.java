package home.controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class loginController implements Initializable {

    private Connection conn = null;
    PreparedStatement pstmt = null;

    public String sNameLogin = "";
    public String sPositionLogin = "";
    public String sRate = "";

    private double x = 0;
    private double y = 0;

    @FXML
    private AnchorPane parent;

    @FXML
    private TextField username_field;

    @FXML
    private PasswordField password_field;

    @FXML
    private HBox wrong_Text;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        conn = database.dbConnection.garitsConnection();
        wrong_Text.setVisible(false);
    }

    @FXML
    void close_app(MouseEvent event) {
        System.exit(0);
    }

    @FXML
    void minimize_stage(MouseEvent event) {
        Stage stage = (Stage) parent.getScene().getWindow();
        stage.setIconified(true);
    }

    //Manage logging into the program
    public void handle_login(javafx.event.ActionEvent actionEvent) throws SQLException {

        String username = username_field.getText(); //Set username to the value of the username field
        String password = password_field.getText(); // set password to the value of the password field

        if (databseConnection(username, password)){
            wrong_Text.setVisible(false);


            //If position = administrator ,Open the admin dashboard
            if (sPositionLogin.equals("Administrator")){
                openWindowAdmin(actionEvent, "admin");
            }
            //If position = franchise, Open franchise dashboard
            else if (sPositionLogin.equals("Franchise")){
                openWindowFranchise(actionEvent, "franchise");
            }
            //If position is foremane, Open foreman dashboard.
            else if (sPositionLogin.equals("Foreman")){
                openWindowForeperson(actionEvent, "foreman");

            }
            //If position is receptionist, Open receptionist dashboard
            else if (sPositionLogin.equals("Receptionist")){
                openWindowReceptionist(actionEvent, "receptionist");

            }
            //If position is Mechanic, open mechanic dashboard.
            else if (sPositionLogin.equals("Mechanic")){
                openWindowMechanic(actionEvent, "mechanic");
            }

            conn.close();

        }

        else {
            wrong_Text.setVisible(true); //Show Error when incorrect details have been entered
        }

    }

    public boolean databseConnection(String username, String password){

        String sql = "SELECT * FROM STAFF WHERE username = ? and password = ?"; //Select staff where username and password = Value that is passed in

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username); //Set username in database to the value of username parameter
            pstmt.setString(2, password); // Set password in database to the value of password parameter

            ResultSet rs = pstmt.executeQuery();

            //If there are not any rows in the db
            if (!rs.next()){
                return false; //return false
            }else {
                //sets the Name,Position and rate variables to the cells under column name,position and hourlyRate.
                //This is what you see on the top left of the program when logged in.
                sNameLogin = rs.getString("name");
                sPositionLogin = rs.getString("position");
                sRate = rs.getString("hourlyRate");
                return true;
            }


        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void openWindowAdmin(javafx.event.ActionEvent actionEvent, String form){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/home/" + form + ".fxml")); // Opens Admin.fxml file

            Parent root = (Parent) loader.load();

            adminController aD = loader.getController();
            aD.setStaffNamePosition(sNameLogin, sPositionLogin); //Calls function while passing in parameters


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

    public void openWindowForeperson(javafx.event.ActionEvent actionEvent, String form){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/home/" + form + ".fxml"));  // Opens Foreperson.fxml file

            Parent root = (Parent) loader.load();

            foremanControlller aD = loader.getController();
            aD.setStaffNamePosition(sNameLogin, sPositionLogin); //Calls function while passing in parameters


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

    public void openWindowFranchise(javafx.event.ActionEvent actionEvent, String form){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/home/" + form + ".fxml")); //Opens Franchise.fxml

            Parent root = (Parent) loader.load();

            franchiseController aD = loader.getController();
            aD.setStaffNamePosition(sNameLogin, sPositionLogin);//Calls function while passing in parameters


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

    public void openWindowReceptionist(javafx.event.ActionEvent actionEvent, String form){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/home/" + form + ".fxml")); // Open Receptionist.fxml

            Parent root = (Parent) loader.load();

            receptionistController aD = loader.getController();
            aD.setStaffNamePosition(sNameLogin, sPositionLogin); //Calls function while passing in parameters


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

    public void openWindowMechanic(javafx.event.ActionEvent actionEvent, String form){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/home/" + form + ".fxml")); // Opens Mechanic.fxml

            Parent root = (Parent) loader.load();

            mechanicController aD = loader.getController();
            aD.setStaffNamePosition(sNameLogin, sPositionLogin);//Calls function while passing in parameters


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


}