package models;

import java.util.Date;
import java.util.Objects;

public class Position {
    private String name;
    private int id;
    private Date createdAt;

    public Position(String name){
        this.name = name;
        this.createdAt = new Date();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        Position position = (Position) o;
        return getId() == position.getId() && getName().equals(position.getName()) && Objects.equals(getCreatedAt(), position.getCreatedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getId(), getCreatedAt());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public String getName() {
        return name;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
}
