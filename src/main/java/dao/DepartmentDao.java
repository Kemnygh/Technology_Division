package dao;

import models.Department;
import models.Employee;
import models.Position;

import java.util.Date;
import java.util.List;

public interface DepartmentDao {
    // LIST
    List<Department> getAll();

    // CREATE
    void add(Department department);

    // READ
    Department findById(int id);

    // UPDATE
    void update(int id, String name, String updated);

    // DELETE
    void deleteById(int id);
    void clearAllDepartments();

    List<Employee> getAllEmployeesByDepartment(int departmentId);
    void deleteAllEmployeesByDepartment(int departmentId);
}
