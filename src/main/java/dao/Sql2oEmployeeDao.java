package dao;

import models.Department;
import models.Employee;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;
import spark.Response;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Sql2oEmployeeDao implements EmployeeDao {
    private final Sql2o sql2o;

    public Sql2oEmployeeDao(Sql2o sql2o){
        this.sql2o = sql2o; //making the sql2o object available everywhere so we can call methods in it
    }

    @Override
    public void add(Employee employee) {
        String sql = "INSERT INTO employees (first_name, last_name, staff_id, role, position_id, department_id) VALUES (:first_name, :last_name, :staff_id, :role, :position_id, :department_id)"; //raw sql
//        Date todayTime = new Date();
//        String pattern = "MM/dd/yyyy HH:mm:ss";
//        DateFormat df = new SimpleDateFormat(pattern);
//        String todayAsString = df.format(todayTime);
        try(Connection con = sql2o.open()){ //try to open a connection
            int id = (int) con.createQuery(sql, true) //make a new variable
                    .bind(employee) //map my argument onto the query so we can use information from it
                    .executeUpdate() //run it all
                    .getKey(); //int id is now the row number (row key) //of db
            employee.setId(id); //update object to set id now from database
//            employee.setCreated_at(todayAsString);
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
    public void update(int id, String newFirst_name, String newLast_name, String newStaff_id, String newRole, int position_id, int department_id, String updated) {
        String sql = "UPDATE employees SET (first_name, last_name, staff_id, role, position_id, department_id, updated) = (:first_name, :last_name, :staff_id, :role, :position_id, :department_id, :updated) WHERE id=:id";
        try(Connection con = sql2o.open()){
            con.createQuery(sql)
                    .addParameter("first_name", newFirst_name)
                    .addParameter("last_name", newLast_name)
                    .addParameter("staff_id", newStaff_id)
                    .addParameter("role", newRole)
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
