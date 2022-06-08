package models;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest {

    @Test
    public void NewEmployeeObjectGetsCorrectlyCreated() throws Exception {
        Employee employee = setupNewEmployee();
        assertNotNull(employee);
    }

    @Test
    public void EmployeeInstantiatesWithFirstName() throws Exception {
        Employee employee = setupNewEmployee();
        assertEquals("John", employee.getFirst_name());
    }

    @Test
    public void EmployeeInstantiatesWithLastName() throws Exception {
        Employee employee = setupNewEmployee();
        assertEquals("Doe", employee.getLast_name());
    }

    @Test
    public void EmployeeInstantiatesWithStaffID() throws Exception {
        Employee employee = setupNewEmployee();
        assertEquals("EN001", employee.getStaff_id());
    }

    @Test
    public void gettingCurrentDate(){
        Employee employee = setupNewEmployee();
        Date todayTime = new Date();
        assertEquals(employee.getCreatedAt(), todayTime);
    }

    public Employee setupNewEmployee(){
        return new Employee("John", "Doe", "EN001", 1, 1);
    }
}