package dao;

import models.Department;
import models.Employee;

import java.util.List;

public interface DepartmentDao {
    // LIST
    List<Department> getAll();

    // CREATE
    void add(Department department);

    // READ
    Department findById(int id);

    // UPDATE
    void update(int id, String name);

    // DELETE
    void deleteById(int id);
    void clearAllTasks();
}
