package home;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    private double x = 0;
    private double y = 0;

    public static Stage primaryStage = null;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml")); // Open te Login.fxml on startup
         primaryStage.setScene(new Scene(root));
        //setting stage borderless
        primaryStage.initStyle(StageStyle.UNDECORATED);

        Image image = new Image("images/icon.png"); // set icon image

        primaryStage.getIcons().add(image); //Adds the image
        primaryStage.setTitle("GARITS"); // sets title

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
                primaryStage.setX(event.getScreenX() - x);
                primaryStage.setY(event.getScreenY() - y);
            }
        });
        this.primaryStage = primaryStage;

        primaryStage.show();
    }
    public static void main(String[] args) {

        launch(args);
    }
}
