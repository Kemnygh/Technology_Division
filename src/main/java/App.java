import java.util.*;

import dao.Sql2oDepartmentDao;
import dao.Sql2oEmployeeDao;
import dao.Sql2oPositionDao;
import models.Department;
import models.Employee;
import models.Position;
import org.sql2o.Sql2o;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;
import static spark.Spark.*;

public class App {
    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 5051; //return port if heroku-port isn't set (i.e. on localhost)
    }
    public static void main(String[] args) {
        port(getHerokuAssignedPort());
        staticFileLocation("/public");
        String connectionString = "jdbc:postgresql://localhost:5432/technology";
        Sql2o sql2o = new Sql2o(connectionString, "postgres", "root");
        Sql2oEmployeeDao employeeDao = new Sql2oEmployeeDao(sql2o);
        Sql2oDepartmentDao departmentDao = new Sql2oDepartmentDao(sql2o);
        Sql2oPositionDao positionDao = new Sql2oPositionDao(sql2o);

        //get: show all employees in all departments and show all departments
        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            List<Department> departments = departmentDao.getAll();
            List<Employee> employees = employeeDao.getAll();
            List<Position> positions = positionDao.getAll();
            model.put("departments", departments);
            model.put("employees", employees);
            model.put("positions", positions);
            return new ModelAndView(model, "index.hbs");
        }, new HandlebarsTemplateEngine());

        //get: show new employee form
        get("/employees/new", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            List<Department> allDepartments = departmentDao.getAll();
            List<Position> allPositions = positionDao.getAll();
            model.put("departments", allDepartments);
            model.put("positions", allPositions);
            return new ModelAndView(model, "employee-form.hbs");
        }, new HandlebarsTemplateEngine());

        //get: delete all employees
        get("/employees/delete/all", (req, res) -> {
            employeeDao.clearAllEmployees(); //change
            res.redirect("/");
            return null;
        }, new HandlebarsTemplateEngine());

        //get: delete an individual employee
        get("/employees/:id/delete", (req, res) -> {
            int idOfEmployeeToDelete = Integer.parseInt(req.params("id"));
            employeeDao.deleteById(idOfEmployeeToDelete);
            res.redirect("/");
            return null;
        }, new HandlebarsTemplateEngine());

        //post: process new employee form
        post("/employees", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            String errMsg = "Employee already exists";
            String successMsg = "Employee created successfully";
            List<Department> allDepartments = departmentDao.getAll();
            List<Employee> employees = employeeDao.getAll();
            model.put("departments", allDepartments);
            String firstName = req.queryParams("first_name");
            String lastName = req.queryParams("last_name");
            String staffId = req.queryParams("staff_id");
            String role = req.queryParams("role");
            String phoneNo = req.queryParams("phone_no");
            String email = req.queryParams("email");
            int positionId = Integer.parseInt(req.queryParams("position_id"));
            int departmentId = Integer.parseInt(req.queryParams("department_id"));
            String createdAt = req.queryParams("created_at");
            Employee newEmployee = new Employee(firstName, lastName, staffId, role, phoneNo, email, positionId, departmentId, createdAt );
            for(Employee employee : employees){
                if(employee.getStaff_id().equals(staffId) || employee.getPhone_no().equals(phoneNo)|| employee.getEmail().equals(email)){
                    model.put("errors", errMsg);
                    model.put("positions", positionDao.getAll());
                    model.put("departments", departmentDao.getAll());
                    return new ModelAndView(model, "employee-form.hbs");
                }
            }
            employeeDao.add(newEmployee);
            model.put("success", successMsg);
            model.put("positions", positionDao.getAll());
            model.put("departments", departmentDao.getAll());
            return new ModelAndView(model, "employee-form.hbs");
        }, new HandlebarsTemplateEngine());

        //get: show an individual Employee
        get("/employees/:id/:position_id/:department_id", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int idOfEmployeeToFind = Integer.parseInt(req.params("id"));
            int idOfEmployeeDepartment = Integer.parseInt(req.params("department_id"));
            int idOfEmployeePosition = Integer.parseInt(req.params("position_id"));
            Employee foundEmployee = employeeDao.findById(idOfEmployeeToFind);
            Department employeeDepartment = departmentDao.findById(idOfEmployeeDepartment);
            Position employeePosition = positionDao.findById(idOfEmployeePosition);
            model.put("employee", foundEmployee);
            model.put("department", employeeDepartment);
            model.put("position", employeePosition);
            model.put("departments", departmentDao.getAll());
            model.put("positions", positionDao.getAll());
            return new ModelAndView(model, "employee-details.hbs");
        }, new HandlebarsTemplateEngine());

        //get: show a form to update an Employee
        get("/employees/:id/edit", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            List<Department> allDepartments = departmentDao.getAll();
            List<Position> allPositions = positionDao.getAll();
            model.put("departments", allDepartments);
            model.put("positions", allPositions);
            Employee employee = employeeDao.findById(Integer.parseInt(req.params("id")));
            model.put("employee", employee);
            model.put("editEmployee", true);
            return new ModelAndView(model, "employee-form.hbs");
        }, new HandlebarsTemplateEngine());

        //post: process a form to update an employee
        post("/employees/:id", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int employeeToEditId = Integer.parseInt(req.params("id"));
            String firstName = req.queryParams("first_name");
            String lastName = req.queryParams("last_name");
            String staffId = req.queryParams("staff_id");
            String role = req.queryParams("role");
            String phoneNo = req.queryParams("phone_no");
            String email = req.queryParams("email");
            int positionId = Integer.parseInt(req.queryParams("position_id"));
            int departmentId = Integer.parseInt(req.queryParams("department_id"));
            String updated = req.queryParams("updated");
            employeeDao.update(employeeToEditId, firstName, lastName, staffId, role, phoneNo, email, positionId, departmentId, updated );
            model.put("updated", "Employee updated successfully");
            return new ModelAndView(model, "employee-form.hbs");
        }, new HandlebarsTemplateEngine());

        //show new Department form
        get("/departments/new", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            List<Department> departments = departmentDao.getAll(); //refresh list of links for navbar
            model.put("departments", departments);
            model.put("positions", positionDao.getAll());
            return new ModelAndView(model, "department-form.hbs"); //new
        }, new HandlebarsTemplateEngine());

        //post: process new department form
        post("/departments", (req, res) -> { //new
            Map<String, Object> model = new HashMap<>();
            List<Department> departments = departmentDao.getAll();
            String errMsg = "Department already exists";
            String successMsg = "Department created successfully";
            String name = req.queryParams("name");
            String createdAt = req.queryParams("created_at");
            Department newDepartment = new Department(name, createdAt);
            for(Department dep : departments){
                if(dep.getName().equals(name)){
                    model.put("errors", errMsg);
                    model.put("positions", positionDao.getAll());
                    model.put("departments", departmentDao.getAll());
                    return new ModelAndView(model, "department-form.hbs");
                }
            }
            departmentDao.add(newDepartment);
            model.put("positions", positionDao.getAll());
            model.put("departments", departmentDao.getAll());
            model.put("success", successMsg);
            return new ModelAndView(model, "department-form.hbs");
        }, new HandlebarsTemplateEngine());

        //get: show an individual departments and employees it contains
        get("/departments/:id", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int idOfDepartmentToFind = Integer.parseInt(req.params("id")); //new
            Department foundDepartment = departmentDao.findById(idOfDepartmentToFind);
            model.put("department", foundDepartment);
            List<Employee> allEmployeesByDepartment = departmentDao.getAllEmployeesByDepartment(idOfDepartmentToFind);
            model.put("employees", allEmployeesByDepartment);
            model.put("departments", departmentDao.getAll()); //refresh list of links for navbar
            model.put("positions", positionDao.getAll());
            return new ModelAndView(model, "department-details.hbs"); //new
        }, new HandlebarsTemplateEngine());

        //get: show a form to update a department
        get("/departments/:id/edit", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("editDepartment", true);
            Department department = departmentDao.findById(Integer.parseInt(req.params("id")));
            model.put("department", department);
            model.put("departments", departmentDao.getAll()); //refresh list of links for navbar
            return new ModelAndView(model, "department-form.hbs");
        }, new HandlebarsTemplateEngine());

        //post: process a form to update a department
        post("/departments/:id", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int idOfDepartmentToEdit = Integer.parseInt(req.params("id"));
            String newName = req.queryParams("newDepartment");
            String updateTime = req.queryParams("updated");
            departmentDao.update(idOfDepartmentToEdit, newName, updateTime);
            model.put("updated", "Department updated successfully");
            model.put("dep", newName);
            return new ModelAndView(model, "department-form.hbs");
        }, new HandlebarsTemplateEngine());

        //get: delete all departments, positions and all employees
        get("/departments/:id/delete", (req, res) -> {
            int departmentId = Integer.parseInt(req.params("id"));
            departmentDao.deleteById(departmentId);
            departmentDao.deleteAllEmployeesByDepartment(departmentId);
            res.redirect("/");
            return null;
        }, new HandlebarsTemplateEngine());

        get("/delete/departments/all", (req, res) -> {
            departmentDao.clearAllDepartments();
            res.redirect("/");
            return null;
        }, new HandlebarsTemplateEngine());

        //show new positions form
        get("/positions/new", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            List<Department> allDepartments = departmentDao.getAll();
            List<Position> position = positionDao.getAll(); //refresh list of links for navbar
            model.put("departments", allDepartments);
            model.put("positions", position);
            return new ModelAndView(model, "position-form.hbs"); //new
        }, new HandlebarsTemplateEngine());

        //post: process new positions form
        post("/positions", (req, res) -> { //new
            Map<String, Object> model = new HashMap<>();
            String errMsg = "Position already exists";
            String successMsg = "Position created successfully";
            List<Position> positions = positionDao.getAll();
            String name = req.queryParams("name");
            String createdAt = req.queryParams("created_at");
            Position newPosition = new Position(name, createdAt);
            for(Position pos : positions){
                if(pos.getName().equals(name)){
                    model.put("errors", errMsg);
                    model.put("positions", positionDao.getAll());
                    model.put("departments", departmentDao.getAll());
                    return new ModelAndView(model, "position-form.hbs");
                }
            }
            positionDao.add(newPosition);
            model.put("success", successMsg);
            model.put("positions", positionDao.getAll());
            model.put("departments", departmentDao.getAll());
            return new ModelAndView(model, "position-form.hbs");
        }, new HandlebarsTemplateEngine());

        //get: show an individual positions and employees it contains
        get("/positions/:id", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int idOfPositionToFind = Integer.parseInt(req.params("id")); //new
            Position foundPosition = positionDao.findById(idOfPositionToFind);
            model.put("position", foundPosition);
            List<Employee> allEmployeesByPosition = positionDao.getAllEmployeesByPosition(idOfPositionToFind);
            model.put("employees", allEmployeesByPosition);
            model.put("positions", positionDao.getAll()); //refresh list of links for navbar
            model.put("departments", departmentDao.getAll());
            return new ModelAndView(model, "position-details.hbs"); //new
        }, new HandlebarsTemplateEngine());

        //get: show a form to update a position
        get("/positions/:id/edit", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("editPosition", true);
            Position position = positionDao.findById(Integer.parseInt(req.params("id")));
            model.put("position", position);
            model.put("positions", positionDao.getAll()); //refresh list of links for navbar
            model.put("departments", departmentDao.getAll());
            return new ModelAndView(model, "position-form.hbs");
        }, new HandlebarsTemplateEngine());

        //post: process a form to update a position
        post("/positions/:id", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int idOfPositionToEdit = Integer.parseInt(req.params("id"));
            String newName = req.queryParams("newPosition");
            String updateTime = req.queryParams("updated");
            positionDao.update(idOfPositionToEdit, newName, updateTime);
            model.put("updated", "Position updated successfully");
            model.put("pos", newName);
            return new ModelAndView(model, "position-form.hbs");
        }, new HandlebarsTemplateEngine());

        //get: delete all departments, positions and all employees
        get("/positions/:id/delete", (req, res) -> {
            int positionId = Integer.parseInt(req.params("id"));
            positionDao.deleteById(positionId);
            res.redirect("/");
            return null;
        }, new HandlebarsTemplateEngine());

        get("/delete/positions/all", (req, res) -> {
            positionDao.clearAllPositions();
            res.redirect("/");
            return null;
        }, new HandlebarsTemplateEngine());

        get("/delete/all", (req, res) -> {
            positionDao.clearAllPositions();
            departmentDao.clearAllDepartments();
            employeeDao.clearAllEmployees();
            res.redirect("/");
            return null;
        }, new HandlebarsTemplateEngine());
    }
}
