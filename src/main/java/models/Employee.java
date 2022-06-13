package models;

import java.util.Date;
import java.util.Objects;

public class Employee {
    private String first_name;
    private String last_name;
    private String staff_id;
    private String role;
    private int id;
    private int department_id;
    private int position_id;
    private Date createdAt;

    public Employee(String first_name, String last_name, String staff_id, String role, int position_id, int department_id){
        this.first_name = first_name;
        this.last_name = last_name;
        this.staff_id = staff_id;
        this.role = role;
        this.position_id = position_id;
        this.department_id = department_id;
        this.createdAt = new Date();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee)) return false;
        Employee employee = (Employee) o;
        return getId() == employee.getId() && getDepartment_id() == employee.getDepartment_id() && getRole().equals(employee.getRole()) &&  getPosition_id() == employee.getPosition_id() && getFirst_name().equals(employee.getFirst_name()) && getLast_name().equals(employee.getLast_name()) && getStaff_id().equals(employee.getStaff_id()) && getCreatedAt().equals(employee.getCreatedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirst_name(), getLast_name(), getStaff_id(), getRole(), getId(), getDepartment_id(), getPosition_id(), getCreatedAt());
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    //    public void setRole_id(int role_id) {
//        this.position_id = role_id;
//    }
//
//    public void setDepartment_id(int department_id) {
//        this.department_id = department_id;
//    }

    public int getId() {
        return id;
    }

    public int getPosition_id() {
        return position_id;
    }

    public int getDepartment_id() {
        return department_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getRole() {
        return role;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getStaff_id() {
        return staff_id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }


}
