/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import model.Employee;

/**
 *
 * @author sasa
 */
public interface EmployeeDAO {
   
    public void saveEmployeeInDB(Employee employee);
    
    public void updateEmployeeInDB(int id, String field, String value);
    
    public void deleteEmployeeInDB(int id);
    
    public List<Employee> getAllEmployeesFromDB();
    
    public List<Employee> getAllEmployeesCriteriumFromDB(String field, String value);
    
}
