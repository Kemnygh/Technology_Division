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
    public void EmployeeInstantiatesWithRole() throws Exception {
        Employee employee = setupNewEmployee();
        assertEquals("Manager DevOps", employee.getRole());
    }

    @Test
    public void EmployeeInstantiatesWithPhoneNumber() throws Exception {
        Employee employee = setupNewEmployee();
        assertEquals("0712345678", employee.getPhone_no());
    }

    @Test
    public void EmployeeInstantiatesWithEmail() throws Exception {
        Employee employee = setupNewEmployee();
        assertEquals("jdoe@company.co.ke", employee.getEmail());
    }

    @Test
    public void EmployeeInstantiatesWithPositionId() throws Exception {
        Employee employee = setupNewEmployee();
        assertEquals(1, employee.getPosition_id());
    }

    @Test
    public void EmployeeInstantiatesWithDepartmentId() throws Exception {
        Employee employee = setupNewEmployee();
        assertEquals(1, employee.getDepartment_id());
    }

    @Test
    public void EmployeeInstantiatesWithCreatedDate() throws Exception {
        Employee employee = setupNewEmployee();
        assertEquals("2022-06-14", employee.getCreated_at());
    }

    @Test
    public void EmployeeInstantiatesWithGetId() throws Exception {
        Employee employee = setupNewEmployee();
        employee.setId(1);
        assertEquals(1, employee.getId());
    }

    @Test
    public void EmployeeInstantiatesWithUpdated() throws Exception {
        Employee employee = setupNewEmployee();
        employee.setUpdated("2022-04-15");
        assertEquals("2022-04-15", employee.getUpdated());
    }

    public Employee setupNewEmployee(){
        Date todayTime = new Date();
        return new Employee("John", "Doe", "EN001", "Manager DevOps","0712345678", "jdoe@company.co.ke",1, 1, "2022-06-14");
    }
}