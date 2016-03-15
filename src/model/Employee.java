/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import dao.EmployeeDAO;
import dao.EmployeeDAOImpl;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.ArrayList;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author sasa
 */
@Entity
@Table(name = "employee")
public class Employee implements Externalizable {

    private final IntegerProperty id = new SimpleIntegerProperty(this, "id", 0);
    private final StringProperty name = new SimpleStringProperty(this, "name", "");
    private final IntegerProperty age = new SimpleIntegerProperty(this, "age", 1);
    private final StringProperty address = new SimpleStringProperty(this, "address", "");
    private final IntegerProperty income = new SimpleIntegerProperty(this, "income", 1);

    public Employee() {
    }

    public Employee(int id, String name, int age, String address, int income) {
        this.id.set(id);
        this.name.set(name);
        this.age.set(age);
        this.address.set(address);
        this.income.set(income);
    }

    public Employee(String name, int age, String address, int income) {
        this.name.set(name);
        this.age.set(age);
        this.address.set(address);
        this.income.set(income);
    }

    @Id
    @Column(name = "employee_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    @Column(name = "name", nullable = false, length = 50)
    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    @Column(name = "age", nullable = false)
    public int getAge() {
        return age.get();
    }

    public void setAge(int age) {
        this.age.set(age);
    }

    public IntegerProperty ageProperty() {
        return age;
    }

    @Column(name = "address", nullable = false, length = 50)
    public String getAddress() {
        return address.get();
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public StringProperty addressProperty() {
        return address;
    }

    @Column(name = "income", nullable = false)
    public int getIncome() {
        return income.get();
    }

    public void setIncome(int income) {
        this.income.set(income);
    }

    public IntegerProperty incomeProperty() {
        return income;
    }

    private final ObjectProperty<ArrayList<String>> errorList = new SimpleObjectProperty<>(this, "errorList", new ArrayList<>());

    public ObjectProperty<ArrayList<String>> errorsProperty() {
        return errorList;
    }

  

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(getId());
        out.writeObject(getName());
        out.writeInt(getAge());
        out.writeObject(getAddress());
        out.writeInt(getIncome());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        setId(in.readInt());
        setName((String) in.readObject());
        setAge(in.readInt());
        setAddress((String) in.readObject());
        setIncome(in.readInt());
    }
}
