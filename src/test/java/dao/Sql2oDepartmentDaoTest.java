package dao;

import models.Department;
import models.Employee;
import models.Position;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class Sql2oDepartmentDaoTest {
    private static Sql2oDepartmentDao departmentDao; //these variables are now static.
    private static Sql2oEmployeeDao employeeDao; //these variables are now static.
    private static Sql2oPositionDao positionDao; //these variables are now static.
    private static Connection conn; //these variables are now static.

    @BeforeAll
    public static void setUp() throws Exception { //changed to static
        String connectionString = "jdbc:postgresql://localhost:5432/technology_test"; // connect to postgres test database
        Sql2o sql2o = new Sql2o(connectionString, "postgres", "root"); // changed user and pass to null
        sql2o.getConnectionSource().getConnection().setAutoCommit(false);
        departmentDao = new Sql2oDepartmentDao(sql2o);
        employeeDao = new Sql2oEmployeeDao(sql2o);
        positionDao = new Sql2oPositionDao(sql2o);
        conn = sql2o.open(); // open connection once before this test file is run
    }

    @AfterEach
    public void tearDown() throws Exception { //I have changed
        System.out.println("clearing database");
        departmentDao.clearAllDepartments(); // clear all categories after every test
        employeeDao.clearAllEmployees(); // clear all tasks after every test

    }

    @AfterAll // changed to @AfterClass (run once after all tests in this file completed)
    public static void shutDown() throws Exception { //changed to static and shutDown
        conn.close(); // close connection once after this entire test file is finished
        System.out.println("connection closed");
    }

    @Test
    public void addingDepartmentSetsId() throws Exception {
        Department department = setupNewDepartment();
        int originalDepartmentId = department.getId();
        departmentDao.add(department);
        assertNotEquals(originalDepartmentId, department.getId());
    }

    @Test
    public void existingDepartmentsCanBeFoundById() throws Exception {
        Department department = setupNewDepartment();
        departmentDao.add(department);
        Department foundDepartment = departmentDao.findById(department.getId());
        assertEquals(department.getId(), foundDepartment.getId());
//        assertEquals(department, foundDepartment);
    }

    @Test
    public void addedDepartmentsAreReturnedFromGetAll() throws Exception {
        Department department = setupNewDepartment();
        departmentDao.add(department);
        assertEquals(1, departmentDao.getAll().size());
    }

    @Test
    public void noDepartmentsReturnsEmptyList() throws Exception {
        assertEquals(0, departmentDao.getAll().size());
    }

    @Test
    public void updateChangesDepartmentContent() throws Exception {
        String initialDescription = "Information Technology";
        Department department = new Department (initialDescription);
        departmentDao.add(department);
        departmentDao.update(department.getId(), "Network Planning");
        Department updatedDepartment = departmentDao.findById(department.getId());
        assertNotEquals(initialDescription, updatedDepartment.getName());
    }

    @Test
    public void deleteByIdDeletesCorrectDepartment() throws Exception {
        Department department = setupNewDepartment();
        departmentDao.add(department);
        departmentDao.deleteById(department.getId());
        assertEquals(0, departmentDao.getAll().size());
    }

    @Test
    public void clearAllClearsAllDepartments() throws Exception {
        Department department = setupNewDepartment();
        Department otherDepartment = new Department("Network Planning");
        departmentDao.add(department);
        departmentDao.add(otherDepartment);
        int daoSize = departmentDao.getAll().size();
        departmentDao.clearAllDepartments();
        assertTrue(daoSize > 0 && daoSize > departmentDao.getAll().size());
    }

    @Test
    public void getAllEmployeesByDepartmentReturnsEmployeesCorrectly() throws Exception {
        Department department = setupNewDepartment();
        Date todayTime = new Date();
        departmentDao.add(department);
        int departmentId = department.getId();
        Employee newEmployee = new Employee("John", "Doe", "EN001", "Analyst Networks", 1, departmentId, "2022-06-14");
        Employee anotherEmployee = new Employee("Jane", "Doe", "EN002", "Manager Networks", 2, departmentId, "2022-06-14");
        employeeDao.add(newEmployee);
        employeeDao.add(anotherEmployee); //we are not adding task 3 so we can test things precisely.
        assertEquals(2, departmentDao.getAllEmployeesByDepartment(departmentId).size());
//        assertTrue(departmentDao.getAllEmployeesByDepartment(departmentId).isEmpty());
//        assertTrue(departmentDao.getAllEmployeesByDepartment(departmentId).contains(otherTask));
    }

    @Test
    public void deleteAllEmployeesByDepartmentDeletesCorrectly() throws Exception {
        Department department = setupNewDepartment();
        Date todayTime = new Date();
        departmentDao.add(department);
        int departmentId = department.getId();
        Employee newEmployee = new Employee("John", "Doe", "EN001", "Analyst Networks", 1, departmentId, "2022-06-14");
        Employee anotherEmployee = new Employee("Jane", "Doe", "EN002", "Manager Networks", 2, departmentId, "2022-06-14");
        employeeDao.add(newEmployee);
        employeeDao.add(anotherEmployee);
        departmentDao.deleteAllEmployeesByDepartment(departmentId);
        assertEquals(0, departmentDao.getAllEmployeesByDepartment(departmentId).size());
    }



    // helper method
    public Department setupNewDepartment(){
        return new Department("Information Technology");
    }

}