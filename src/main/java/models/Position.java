package models;

import java.util.Objects;

public class Position {
    private String name;
    private int id;
    private String created_at;
    private String updated;

    public Position(String name, String created_at){
        this.name = name;
        this.created_at = created_at;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        Position position = (Position) o;
        return getId() == position.getId() && getName().equals(position.getName()) && getCreated_at().equals(position.getCreated_at()) && getUpdated().equals(position.getUpdated());
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

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated() {
        return updated;
    }

    public String getName() {
        return name;
    }


}
