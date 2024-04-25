package Controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import util.DatabaseConnector;

import java.sql.SQLException;
import java.util.List;

import Models.Classes;

public class ClassesController {

    @FXML
    private TableView<Classes> classesTable;

    @FXML
    private TableColumn<Classes, Integer> classIdColumn;

    @FXML
    private TableColumn<Classes, String> classNameColumn;

    @FXML
    private TextField newClassNameField;

    private ObservableList<Classes> classesList;

    public void initialize() {
        loadData(); // Initialize the table
    }

    private void loadData() {
        try {
            List<Classes> allClasses = DatabaseConnector.getAllClasses();
            classesList = FXCollections.observableArrayList(allClasses);
            classIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getClassId()).asObject());
            classNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getClassName()));
            classesTable.setItems(classesList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddClass(ActionEvent event) {
        String newClassName = newClassNameField.getText();
        if (!newClassName.isEmpty()) {
            try {
                DatabaseConnector.createClass(newClassName);
                loadData(); // Reload data after adding a new class
                newClassNameField.clear();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Please enter a class name.");
        }
    }

    @FXML
    private void handleDeleteClass(ActionEvent event) {
        Classes selectedClass = classesTable.getSelectionModel().getSelectedItem();
        if (selectedClass != null) {
            try {
                DatabaseConnector.deleteClass(selectedClass.getClassId());
                loadData(); // Reload data after deleting a class
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Please select a class to delete.");
        }
    }

    @FXML
    private void handleUpdateClass(ActionEvent event) {
        Classes selectedClass = classesTable.getSelectionModel().getSelectedItem();
        if (selectedClass != null) {
            String newClassName = newClassNameField.getText();
            if (!newClassName.isEmpty()) {
                try {
                    DatabaseConnector.updateClass(selectedClass.getClassId(), newClassName);
                    loadData(); // Reload data after updating a class
                    newClassNameField.clear();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                showAlert("Please enter a new class name.");
            }
        } else {
            showAlert("Please select a class to update.");
        }
    }

    @FXML
    private void handleTableSelection() {
        Classes selectedClass = classesTable.getSelectionModel().getSelectedItem();
        if (selectedClass != null) {
            newClassNameField.setText(selectedClass.getClassName());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
