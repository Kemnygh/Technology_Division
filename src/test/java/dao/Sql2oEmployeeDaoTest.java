package dao;

import models.Department;
import models.Employee;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class Sql2oEmployeeDaoTest {
    private static Sql2oEmployeeDao employeeDao; //these variables are now static.
    private static Connection conn; //these variables are now static.

    @BeforeAll
    public static void setUp() throws Exception { //changed to static
        String connectionString = "jdbc:postgresql://localhost:5432/technology_test"; // connect to postgres test database
        Sql2o sql2o = new Sql2o(connectionString, "postgres", "root"); // changed user and pass to null
        sql2o.getConnectionSource().getConnection().setAutoCommit(false);
        employeeDao = new Sql2oEmployeeDao(sql2o);
        conn = sql2o.open(); // open connection once before this test file is run
    }

    @AfterEach
    public void tearDown() throws Exception { //I have changed
        System.out.println("clearing database");
        employeeDao.clearAllEmployees(); // clear all

    }

    @AfterAll // changed to @AfterClass (run once after all tests in this file completed)
    public static void shutDown() throws Exception { //changed to static and shutDown
        conn.close(); // close connection once after this entire test file is finished
        System.out.println("connection closed");
    }

    @Test
    public void addingEmployees() throws Exception {
        Employee employee = setupNewEmployee();
        int originalEmployeeId = employee.getId();
        employeeDao.add(employee);
        assertNotEquals(originalEmployeeId, employee.getId());
    }

    @Test
    public void existingEmployeesCanBeFoundById() throws Exception {
        Employee employee = setupNewEmployee();
        employeeDao.add(employee);
        Employee foundEmployee = employeeDao.findById(employee.getId());
        assertEquals(employee.getId(), foundEmployee.getId());
//        assertEquals(department, foundDepartment);
    }

    @Test
    public void addedEmployeesAreReturnedFromGetAll() throws Exception {
        Employee employee = setupNewEmployee();
        employeeDao.add(employee);
        assertEquals(1, employeeDao.getAll().size());
    }


    @Test
    public void updateChangesEmployeeContent() throws Exception {
        Employee employee = setupNewEmployee();
        employeeDao.add(employee);
        employeeDao.update(employee.getId(), "John", "Doe", "EN002", "Manager Networks", 2, 2);
        Employee updatedEmployee = employeeDao.findById(employee.getId());
        assertNotEquals(employee, updatedEmployee);
    }

    @Test
    public void deleteByIdDeletesCorrectEmployee() throws Exception {
        Employee employee = setupNewEmployee();
        employeeDao.add(employee);
        employeeDao.deleteById(employee.getId());
        assertEquals(0, employeeDao.getAll().size());
    }

    @Test
    public void clearAllClearsAllEmployees() throws Exception {
        Employee employee = setupNewEmployee();
        Date todayTime = new Date();
        Employee otherEmployee = new Employee("Jane", "Doe", "EN002", "Manager Networks", 2, 1);
        employeeDao.add(employee);
        employeeDao.add(otherEmployee);
        int daoSize = employeeDao.getAll().size();
        employeeDao.clearAllEmployees();
        assertTrue(daoSize > 0 && daoSize > employeeDao.getAll().size());
    }


    // helper method
    public Employee setupNewEmployee(){
        Date todayTime = new Date();
        return new Employee("John", "Doe", "EN001", "Analyst Networks", 1, 1);
    }

}