package dao;

import models.Employee;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.List;

public class Sql2oEmployeeDao implements EmployeeDao {
    private final Sql2o sql2o;

    public Sql2oEmployeeDao(Sql2o sql2o){
        this.sql2o = sql2o; //making the sql2o object available everywhere so we can call methods in it
    }

    @Override
    public void add(Employee employee) {
        String sql = "INSERT INTO employees (first_name, last_name, staff_id, role, phone_no, email, position_id, department_id, created_at) VALUES (:first_name, :last_name, :staff_id, :role, :phone_no, :email, :position_id, :department_id, :created_at)"; //raw sql
        try(Connection con = sql2o.open()){ //try to open a connection
            int id = (int) con.createQuery(sql, true) //make a new variable
                    .bind(employee) //map my argument onto the query so we can use information from it
                    .executeUpdate() //run it all
                    .getKey(); //int id is now the row number (row key) //of db
            employee.setId(id); //update object to set id now from database
        } catch (Sql2oException ex) {
            System.out.println(ex); //oops we have an error!
        }
    }

    @Override
    public List<Employee> getAll() {
        try(Connection con = sql2o.open()){
            return con.createQuery("SELECT * FROM employees") //raw sql
                    .executeAndFetch(Employee.class); //fetch a list
        }
    }

    @Override
    public Employee findById(int id) {
        try(Connection con = sql2o.open()){
            return con.createQuery("SELECT * FROM employees WHERE id = :id")
                    .addParameter("id", id) //key/value pair, key must match above
                    .executeAndFetchFirst(Employee.class); //fetch an individual item
        }
    }

   @Override
    public void update(int id, String newFirst_name, String newLast_name, String newStaff_id, String newRole, String newPhone_no, String newEmail, int position_id, int department_id, String updated) {
        String sql = "UPDATE employees SET (first_name, last_name, staff_id, role, phone_no, email, position_id, department_id, updated) = (:first_name, :last_name, :staff_id, :role, :phone_no, :email, :position_id, :department_id, :updated) WHERE id=:id";
        try(Connection con = sql2o.open()){
            con.createQuery(sql)
                    .addParameter("first_name", newFirst_name)
                    .addParameter("last_name", newLast_name)
                    .addParameter("staff_id", newStaff_id)
                    .addParameter("role", newRole)
                    .addParameter("phone_no", newPhone_no)
                    .addParameter("email", newEmail)
                    .addParameter("id", id)
                    .addParameter("position_id", position_id)
                    .addParameter("department_id", department_id)
                    .addParameter("updated", updated)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE from employees WHERE id=:id";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Sql2oException ex){
            System.out.println(ex);
        }
    }

    @Override
    public void clearAllEmployees() {
        String sql = "DELETE from employees";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .executeUpdate();
        } catch (Sql2oException ex){
            System.out.println(ex);
        }
    }
}
