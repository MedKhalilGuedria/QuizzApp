package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	 @Override
	    public void start(Stage primaryStage) throws Exception {
	        Parent root = FXMLLoader.load(getClass().getResource("/Views/Login.fxml"));
	        primaryStage.setTitle("Login");
	        primaryStage.setScene(new Scene(root, 400, 300));
	        primaryStage.show();
	    }

	    public static void main(String[] args) {
	        launch(args);
	    }
}
