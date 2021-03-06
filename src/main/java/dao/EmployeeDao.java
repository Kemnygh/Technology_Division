package dao;


import models.Employee;

import java.util.List;

public interface EmployeeDao {
    // LIST
    List<Employee> getAll();

    // CREATE
    void add(Employee employee);


    // READ
    Employee findById(int id);

    // UPDATE
    void update(int id,String first_name, String last_name, String staff_id, String role, String phone_no, String email, int position_id, int department_id, String updated);

    // DELETE
    void deleteById(int id);
    void clearAllEmployees();
}
