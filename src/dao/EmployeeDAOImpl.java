/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Employee;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import persistence.HibernateUtil;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author sasa
 */
public class EmployeeDAOImpl implements EmployeeDAO {

    private SessionFactory sessionFactory = HibernateUtil.createSessionFactory();

    @Override
    public void saveEmployeeInDB(Employee employee) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.persist(employee);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            System.out.println(e);
        } finally {
            session.close();
        }
    }

    @Override
    public void updateEmployeeInDB(int id, String field, String value) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            Employee employee = (Employee) session.get(Employee.class, id);

            switch (field) {
                case "name":
                    employee.setName(value);
                    break;
                case "age":
                    employee.setAge(Integer.valueOf(value));
                    break;
                case "address":
                    employee.setAddress(value);
                    break;
                case "income":
                    employee.setIncome(Integer.valueOf(value));
                    break;
            }

            tx = session.beginTransaction();
            session.update(employee);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            System.out.println(e);
        } finally {
            session.close();
        }
    }

    @Override
    public void deleteEmployeeInDB(int id) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
//            Employee employee = (Employee) session.get(Employee.class, id);
//            session.delete(employee);
            
//            Query query = session.createQuery("delete Employee where id = :ID");
//            query.setParameter("ID", id);
//
//            query.executeUpdate();
            Employee employee = (Employee) session.get(Employee.class, id);
            
            tx = session.beginTransaction();
            session.delete(employee);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            System.out.println(e);
        } finally {
            session.close();
        }
    }

    @Override
    public List<Employee> getAllEmployeesFromDB() {
        Session session = sessionFactory.openSession();
        ObservableList<Employee> employees = null;
        try {
            employees = FXCollections.<Employee>observableArrayList(session.createQuery("from Employee").list());
        } finally {
            session.close();
        }

        return employees;
    }

    @Override
    public List<Employee> getAllEmployeesCriteriumFromDB(String field, String value) {
        Session session = sessionFactory.openSession();
        ObservableList<Employee> employees = null;
        try {
            Criteria cr = session.createCriteria(Employee.class);
            cr.add(Restrictions.eq(field, value));
            employees = FXCollections.<Employee>observableArrayList(cr.list());
        } finally {
            session.close();
        }

        return employees;
    }

}
