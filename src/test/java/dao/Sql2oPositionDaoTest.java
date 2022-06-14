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

class Sql2oPositionDaoTest {
    private static Sql2oPositionDao positionDao; //these variables are now static.
    private static Sql2oEmployeeDao employeeDao; //these variables are now static.
    private static Connection conn; //these variables are now static.

    @BeforeAll
    public static void setUp() throws Exception { //changed to static
        String connectionString = "jdbc:postgresql://localhost:5432/technology_test"; // connect to postgres test database
        Sql2o sql2o = new Sql2o(connectionString, "postgres", "root"); // changed user and pass to null
        sql2o.getConnectionSource().getConnection().setAutoCommit(false);
        positionDao = new Sql2oPositionDao(sql2o);
        employeeDao = new Sql2oEmployeeDao(sql2o);
        conn = sql2o.open(); // open connection once before this test file is run
    }

    @AfterEach
    public void tearDown() throws Exception { //I have changed
        System.out.println("clearing database");
        positionDao.clearAllPositions(); // clear all categories after every test
        employeeDao.clearAllEmployees(); // clear all tasks after every test

    }

    @AfterAll // changed to @AfterClass (run once after all tests in this file completed)
    public static void shutDown() throws Exception { //changed to static and shutDown
        conn.close(); // close connection once after this entire test file is finished
        System.out.println("connection closed");
    }

    @Test
    public void addingPositionSetsId() throws Exception {
        Position position = setupNewPosition();
        int originalDepartmentId = position.getId();
        positionDao.add(position);
        assertNotEquals(originalDepartmentId, position.getId());
    }

    @Test
    public void existingPositionsCanBeFoundById() throws Exception {
        Position position = setupNewPosition();
        positionDao.add(position);
        Position foundPosition = positionDao.findById(position.getId());
        assertEquals(position.getId(), foundPosition.getId());
//        assertEquals(department, foundDepartment);
    }

    @Test
    public void addedPositionsAreReturnedFromGetAll() throws Exception {
        Position position = setupNewPosition();
        positionDao.add(position);
        assertEquals(1, positionDao.getAll().size());
    }

    @Test
    public void noPositionsReturnsEmptyList() throws Exception {
        assertEquals(0, positionDao.getAll().size());
    }

    @Test
    public void updateChangesPositionContent() throws Exception {
        String initialDescription = "Senior Manager";
        Position position = new Position (initialDescription, "2022-06-14");
        positionDao.add(position);
        positionDao.update(position.getId(), "Senior Management", "2022-06-14");
        Position updatedPosition = positionDao.findById(position.getId());
        assertNotEquals(initialDescription, updatedPosition.getName());
    }

    @Test
    public void deleteByIdDeletesCorrectPosition() throws Exception {
        Position position = setupNewPosition();
        positionDao.add(position);
        positionDao.deleteById(position.getId());
        assertEquals(0, positionDao.getAll().size());
    }

    @Test
    public void clearAllClearsAllPositions() throws Exception {
        Position position = setupNewPosition();
        Position otherPosition = new Position("Manager", "2022-06-14");
        positionDao.add(position);
        positionDao.add(otherPosition);
        int daoSize = positionDao.getAll().size();
        positionDao.clearAllPositions();
        assertTrue(daoSize > 0 && daoSize > positionDao.getAll().size());
    }

    @Test
    public void getAllEmployeesByPositionsReturnsEmployeesCorrectly() throws Exception {
        Position position = setupNewPosition();
        Date todayTime = new Date();
        positionDao.add(position);
        int positionId = position.getId();
        Employee newEmployee = new Employee("John", "Doe", "EN001", "Analyst Networks",  positionId, 1, "2022-06-14");
        Employee anotherEmployee = new Employee("Jane", "Doe", "EN002", "Manager Networks", positionId, 1, "2022-06-14");
        employeeDao.add(newEmployee);
        employeeDao.add(anotherEmployee); //we are not adding task 3 so we can test things precisely.
        assertEquals(2, positionDao.getAllEmployeesByPosition(positionId).size());
//        assertTrue(departmentDao.getAllEmployeesByDepartment(departmentId).isEmpty());
//        assertTrue(departmentDao.getAllEmployeesByDepartment(departmentId).contains(otherTask));
    }

    // helper method
    public Position setupNewPosition(){
        return new Position("Senior Manager", "2022-06-14");
    }

}