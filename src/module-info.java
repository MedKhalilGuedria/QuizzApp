module QuizzApp {
	requires javafx.controls;
	requires java.sql;
	requires javafx.fxml;
	
    requires javafx.graphics;
    requires javafx.base;
	requires java.desktop; // Add this line to require javafx.base
    opens Controllers to javafx.fxml;
	opens application to javafx.graphics, javafx.fxml;
}
