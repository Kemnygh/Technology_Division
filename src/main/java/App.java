import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


import dao.EmployeeDao;
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
    public static void main(String[] args) {
        port(5051); //default is 4567
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
        get("/employees/delete", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            employeeDao.clearAllEmployees(); //change
            res.redirect("/");
            return null;
        }, new HandlebarsTemplateEngine());

        //get: delete an individual employee
        get("/employees/:id/delete", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int idOfEmployeeToDelete = Integer.parseInt(req.params("id"));
            employeeDao.deleteById(idOfEmployeeToDelete);
            res.redirect("/");
            return null;
        }, new HandlebarsTemplateEngine());

        //post: process new employee form
        post("/employees", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            String pattern = "MM/dd/yyyy HH:mm:ss";
            DateFormat df = new SimpleDateFormat(pattern);
            List<Department> allDepartments = departmentDao.getAll();
            model.put("departments", allDepartments);
            String firstName = req.queryParams("first_name");
            String lastName = req.queryParams("last_name");
            String staffId = req.queryParams("staff_id");
            String role = req.queryParams("role");
            int positionId = Integer.parseInt(req.queryParams("position_id"));
            int departmentId = Integer.parseInt(req.queryParams("department_id"));
//            Date createdAt = df.parse(req.queryParams("created_at"));
            Employee newEmployee = new Employee(firstName, lastName, staffId, role, positionId, departmentId );
            employeeDao.add(newEmployee);
            res.redirect("/");
            return null;
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
            int positionId = Integer.parseInt(req.queryParams("position_id"));
            int departmentId = Integer.parseInt(req.queryParams("department_id"));
            employeeDao.update(employeeToEditId, firstName, lastName, staffId, role, positionId, departmentId );
            res.redirect("/");
            return null;
        }, new HandlebarsTemplateEngine());

        //show new Department form
        get("/departments/new", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            List<Department> departments = departmentDao.getAll(); //refresh list of links for navbar
            model.put("departments", departments);
            return new ModelAndView(model, "department-form.hbs"); //new
        }, new HandlebarsTemplateEngine());

        //post: process new department form
        post("/departments", (req, res) -> { //new
            Map<String, Object> model = new HashMap<>();
            String name = req.queryParams("name");
            Department newDepartment = new Department(name);
            departmentDao.add(newDepartment);
            res.redirect("/");
            return null;
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
            System.out.println(req.params());
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
            departmentDao.update(idOfDepartmentToEdit, newName);
            res.redirect("/");
            return null;
        }, new HandlebarsTemplateEngine());

        //get: delete all departments, positions and all employees
        get("/departments/:id/delete", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int departmentId = Integer.parseInt(req.params("id"));
            departmentDao.deleteById(departmentId);
            departmentDao.deleteAllEmployeesByDepartment(departmentId);
            res.redirect("/");
            return null;
        }, new HandlebarsTemplateEngine());

        //show new positions form
        get("/positions/new", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            List<Department> allDepartments = departmentDao.getAll();
            List<Position> position = positionDao.getAll(); //refresh list of links for navbar
            model.put("departments", allDepartments);
            model.put("position", position);
            return new ModelAndView(model, "position-form.hbs"); //new
        }, new HandlebarsTemplateEngine());

        //post: process new positions form
        post("/positions", (req, res) -> { //new
            Map<String, Object> model = new HashMap<>();
            String name = req.queryParams("name");
            Position newPosition = new Position(name);
            positionDao.add(newPosition);
            res.redirect("/");
            return null;
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
            int newDepartmentId = Integer.parseInt(req.queryParams("department_id"));
            positionDao.update(idOfPositionToEdit, newName);
            res.redirect("/");
            return null;
        }, new HandlebarsTemplateEngine());

        //get: delete all departments, positions and all employees
        get("/positions/:id/delete", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int positionId = Integer.parseInt(req.params("id"));
            positionDao.deleteById(positionId);
            res.redirect("/");
            return null;
        }, new HandlebarsTemplateEngine());

        get("/delete/all", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            positionDao.clearAllPositions();
            departmentDao.clearAllDepartments();
            employeeDao.clearAllEmployees();
            res.redirect("/");
            return null;
        }, new HandlebarsTemplateEngine());
    }
}
