package models;

import org.junit.jupiter.api.Test;

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
    void getCreatedInstantiatesCorrectly() {
        Department department = setupNewDepartment();
        assertEquals("2022-06-14", department.getCreated_at());
    }

    @Test
    public void DepartmentInstantiatesWithGetId() throws Exception {
        Department department = setupNewDepartment();
        department.setId(1);
        assertEquals(1, department.getId());
    }

    @Test
    public void DepartmentInstantiatesWithUpdated() throws Exception {
        Department department = setupNewDepartment();
        department.setUpdated("2022-04-15");
        assertEquals("2022-04-15", department.getUpdated());
    }

    public Department setupNewDepartment(){
        return new Department("Information Technology", "2022-06-14");
    }
}