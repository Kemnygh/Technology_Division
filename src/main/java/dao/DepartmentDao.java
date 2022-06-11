package dao;

import models.Employee;

import java.util.List;

public interface EmployeeDao {
    // LIST
    List<Employee> getAll();

    // CREATE
    void add(Employee task);

    // READ
    Employee findById(int id);

    // UPDATE
    void update(int id,String first_name, String last_name, String staff_id, String role, int position_id, int department_id);

    // DELETE
    void deleteById(int id);
    void clearAllTasks();
}
