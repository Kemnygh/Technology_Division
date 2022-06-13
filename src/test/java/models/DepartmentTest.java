package models;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class DepartmentTest {

    @Test
    public void NewDepartmentObjectGetsCorrectlyCreated_true() throws Exception {
        Department department = setupNewDepartment();
        assertTrue(department instanceof Department);
    }

    @Test
    void getNameInstantiatesCorrectly() {
        Department department = setupNewDepartment();
        assertEquals("Information Technology", department.getName());
    }

    @Test
    void getCreatedAtInstantiatesWithCurrentDate() {
        Department department = setupNewDepartment();
        Date todayTime = new Date();
        assertEquals(department.getCreatedAt(), todayTime);
    }

    public Department setupNewDepartment(){
        return new Department("Information Technology");
    }
}