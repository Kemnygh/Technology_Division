package models;

import java.util.Date;
import java.util.Objects;

public class Department {
    private String name;
    private int id;
    private String created_at;
    private String updated;

    public Department(String name, String created_at){
        this.name = name;
        this.created_at = created_at;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Department)) return false;
        Department that = (Department) o;
        return getId() == that.getId() && getName().equals(that.getName()) && getCreated_at().equals(that.getCreated_at()) && getUpdated().equals(that.getUpdated());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getId(), getCreated_at(), getUpdated());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUpdated() {
        return updated;
    }

    public String getName() {
        return name;
    }

    public String getCreated_at() {
        return created_at;
    }
}
