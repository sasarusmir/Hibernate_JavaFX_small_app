/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.EmployeeDAO;
import dao.EmployeeDAOImpl;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.converter.NumberStringConverter;
import model.Employee;
import util.NumberTextField;

/**
 * FXML Controller class
 *
 * @author sasa
 */
public class EmployeeController implements Initializable {

    @FXML
    private TextField newNameTextField;

    @FXML
    private NumberTextField newAgeNumberTextField;

    @FXML
    private TextField newAddressTextField;

    @FXML
    private NumberTextField newIncomeNumberTextField;

    @FXML
    private VBox allEmployeesVbox;

    @FXML
    private VBox criteriumEmployeesVbox;

    @FXML
    private ChoiceBox criteriumChoiceBox;

    @FXML
    private TextField criteriumTextField;

    ObservableList<String> updateOptions = FXCollections.<String>observableArrayList();
    ObservableList<String> criteriumOptions = FXCollections.<String>observableArrayList();

    ObservableList<Employee> employees = FXCollections.<Employee>observableArrayList();
    Employee employee;
    
    private String criteriumField = null;
    private String criteriumValue = null;
    
    private EmployeeDAO employeeDAO;
    
    private ArrayList<String> errList;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        employee = new Employee();
        
        employeeDAO = new EmployeeDAOImpl();

        updateOptions.addAll("New name", "New age", "New address", "New income");
        criteriumOptions.addAll("name", "age", "address", "income");

        criteriumChoiceBox.setItems(criteriumOptions);

        showAllEmployees();

        newNameTextField.textProperty().bindBidirectional(employee.nameProperty());
        newAgeNumberTextField.textProperty().bindBidirectional(employee.ageProperty(), new NumberStringConverter());
        newAddressTextField.textProperty().bindBidirectional(employee.addressProperty());
        newIncomeNumberTextField.textProperty().bindBidirectional(employee.incomeProperty(), new NumberStringConverter());

    }

    @FXML
    private void saveEmployee() {
        if (isValid()) {
            save();
            clearEmployee();
            showAllEmployees();

        } else {
            StringBuilder errMsg = new StringBuilder();

            errList = employee.errorsProperty().get();

            for (String errList1 : errList) {
                errMsg.append(errList1);
            }

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Employee can't be saved!");
            alert.setHeaderText(null);
            alert.setContentText(errMsg.toString());
            alert.showAndWait();
            errList.clear();
        }

    }

    @FXML
    private void clearEmployee() {
        employee.nameProperty().set("");
        employee.ageProperty().set(1);
        employee.addressProperty().set("");
        employee.incomeProperty().set(1);
    }

    @FXML
    private void showEmployeeCriterium() {
        if (!criteriumTextField.getText().isEmpty()) {
            criteriumValue = criteriumTextField.getText();
            criteriumField = criteriumChoiceBox.getValue().toString();
            
            ObservableList<Employee> employeesCriterium = (ObservableList<Employee>) employeeDAO.getAllEmployeesCriteriumFromDB(criteriumChoiceBox.getValue().toString(), criteriumTextField.getText());
            showAllEmployeesCriterium(criteriumField, criteriumValue);
        }
    }

    private void showAllEmployees() {
        allEmployeesVbox.getChildren().clear();
        Label label = new Label("All Employees:");
        label.setId("titleAllLabel");
        allEmployeesVbox.getChildren().add(label);

        employees = (ObservableList<Employee>) employeeDAO.getAllEmployeesFromDB();

        for (Employee employeeFromList : employees) {
            HBox buttonsHbox = new HBox();
            buttonsHbox.setSpacing(20);
            buttonsHbox.setPadding(new Insets(10, 0, 20, 0));
            Button buttonDelete = new Button("Delete");
            Button buttonUpdate = new Button("Update");
            buttonUpdate.setDisable(true);

            HBox hbox = new HBox();
            ChoiceBox updateChoiceBox = new ChoiceBox(updateOptions);
            updateChoiceBox.setValue("New name");
            TextField updateTextField = new TextField("");

            buttonDelete.setMnemonicParsing(false);
            buttonUpdate.setMnemonicParsing(false);
            buttonDelete.setOnAction((event) -> {
                employeeDAO.deleteEmployeeInDB(employeeFromList.getId());
                showAllEmployees();
                
                if(criteriumValue != null && criteriumField != null)
                    showAllEmployeesCriterium(criteriumField, criteriumValue);
            });
            buttonUpdate.setOnAction((event) -> {
                employeeDAO.updateEmployeeInDB(employeeFromList.getId(), updateChoiceBox.getValue().toString().substring(4), updateTextField.getText());
                showAllEmployees();
                
                if(criteriumValue != null && criteriumField != null)
                    showAllEmployeesCriterium(criteriumField, criteriumValue);
                
            });
            buttonsHbox.getChildren().add(buttonUpdate);
            buttonsHbox.getChildren().add(buttonDelete);

            updateTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.isEmpty()) {
                    if (updateChoiceBox.getValue().equals("New age") || updateChoiceBox.getValue().equals("New income")) {
                        if (newValue.matches("[0-9]*")) {
                            buttonUpdate.setDisable(false);
                        } else {
                            buttonUpdate.setDisable(true);
                        }
                    } else {
                        buttonUpdate.setDisable(false);
                    }
                } else {
                    buttonUpdate.setDisable(true);
                }
            });

            hbox.getChildren().add(updateChoiceBox);
            hbox.getChildren().add(updateTextField);

            Label labelId = new Label();
            labelId.visibleProperty().set(false);
            labelId.setDisable(true);
            labelId.setText("" + employeeFromList.getId());

            Label labelName = new Label();
            labelName.setText("Name: " + employeeFromList.getName());

            Label labelAge = new Label();
            labelAge.setText("Age: " + employeeFromList.getAge());

            Label labelAddress = new Label();
            labelAddress.setText("Address: " + employeeFromList.getAddress());

            Label labelIncome = new Label();
            labelIncome.setText("Income: " + employeeFromList.getIncome());

            allEmployeesVbox.getChildren().add(labelName);
            allEmployeesVbox.getChildren().add(labelAge);
            allEmployeesVbox.getChildren().add(labelAddress);
            allEmployeesVbox.getChildren().add(labelIncome);

            allEmployeesVbox.getChildren().add(hbox);
            allEmployeesVbox.getChildren().add(buttonsHbox);
//            allEmployeesVbox.getChildren().add(labelId);

        }
    }

    private void showAllEmployeesCriterium(String field, String value) {
        criteriumEmployeesVbox.getChildren().clear();
        
        employees = (ObservableList<Employee>) employeeDAO.getAllEmployeesCriteriumFromDB(field, value);

        for (Employee employeeFromList : employees) {
            HBox buttonsHbox = new HBox();
            buttonsHbox.setSpacing(20);
            buttonsHbox.setPadding(new Insets(10, 0, 20, 0));
            Button buttonDelete = new Button("Delete");
            Button buttonUpdate = new Button("Update");
            buttonUpdate.setDisable(true);

            HBox hbox = new HBox();
            ChoiceBox updateChoiceBox = new ChoiceBox(updateOptions);
            updateChoiceBox.setValue("New name");
            TextField updateTextField = new TextField("");

            buttonDelete.setMnemonicParsing(false);
            buttonUpdate.setMnemonicParsing(false);
            buttonDelete.setOnAction((event) -> {
                employeeDAO.deleteEmployeeInDB(employeeFromList.getId());
                showAllEmployees();
                
                if(criteriumValue != null && criteriumField != null)
                    showAllEmployeesCriterium(criteriumField, criteriumValue);
            });
            buttonUpdate.setOnAction((event) -> {
                employeeDAO.updateEmployeeInDB(employeeFromList.getId(), updateChoiceBox.getValue().toString().substring(4), updateTextField.getText());
                showAllEmployees();
                
                if(criteriumValue != null && criteriumField != null)
                    showAllEmployeesCriterium(criteriumField, criteriumValue);
            });
            buttonsHbox.getChildren().add(buttonUpdate);
            buttonsHbox.getChildren().add(buttonDelete);

            updateTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.isEmpty()) {
                    if (updateChoiceBox.getValue().equals("New age") || updateChoiceBox.getValue().equals("New income")) {
                        if (newValue.matches("[0-9]*")) {
                            buttonUpdate.setDisable(false);
                        } else {
                            buttonUpdate.setDisable(true);
                        }
                    } else {
                        buttonUpdate.setDisable(false);
                    }
                } else {
                    buttonUpdate.setDisable(true);
                }
            });

            hbox.getChildren().add(updateChoiceBox);
            hbox.getChildren().add(updateTextField);

            Label labelId = new Label();
            labelId.visibleProperty().set(false);
            labelId.setDisable(true);
            labelId.setText("" + employeeFromList.getId());

            Label labelName = new Label();
            labelName.setText("Name: " + employeeFromList.getName());

            Label labelAge = new Label();
            labelAge.setText("Age: " + employeeFromList.getAge());

            Label labelAddress = new Label();
            labelAddress.setText("Address: " + employeeFromList.getAddress());

            Label labelIncome = new Label();
            labelIncome.setText("Income: " + employeeFromList.getIncome());

            criteriumEmployeesVbox.getChildren().add(labelName);
            criteriumEmployeesVbox.getChildren().add(labelAge);
            criteriumEmployeesVbox.getChildren().add(labelAddress);
            criteriumEmployeesVbox.getChildren().add(labelIncome);

            criteriumEmployeesVbox.getChildren().add(hbox);
            criteriumEmployeesVbox.getChildren().add(buttonsHbox);

        }
    }
    
      public boolean isValid() {

        boolean isValid = true;

        if (employee.getName() != null && employee.getName().equals("")) {
            errList.add("Name can't be empty! ");
            isValid = false;
        }

        if (employee.getAddress() != null && employee.getAddress().equals("")) {
            errList.add("Address can't be empty! ");
            isValid = false;
        }

        return isValid;
    }

    public boolean save() {
        if (isValid()) {
            employeeDAO.saveEmployeeInDB(employee);

            return true;
        }
        return false;
    }
}
