package models;

import java.util.Date;
import java.util.Objects;

public class Department {
    private String name;
    private int id;
    private Date createdAt;

    public Department(String name){
        this.name = name;
        this.createdAt = new Date();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Department)) return false;
        Department that = (Department) o;
        return id == that.id && name.equals(that.name) && createdAt.equals(that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id, createdAt);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

//    public void setCreatedAt(Date createdAt) {
//        this.createdAt = createdAt;
//    }

    public String getName() {
        return name;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
}
