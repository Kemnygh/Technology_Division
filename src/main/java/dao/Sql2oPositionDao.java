package dao;

import models.Employee;
import models.Position;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.List;

public class Sql2oPositionDao implements PositionDao {
    private final Sql2o sql2o;

    public Sql2oPositionDao(Sql2o sql2o){
        this.sql2o = sql2o; //making the sql2o object available everywhere so we can call methods in it
    }

    @Override
    public void add(Position position) {
        String sql = "INSERT INTO positions (name, created_at) VALUES (:name, :created_at)"; //raw sql
        try(Connection con = sql2o.open()){ //try to open a connection
            int id = (int) con.createQuery(sql, true) //make a new variable
                    .bind(position) //map my argument onto the query so we can use information from it
                    .executeUpdate() //run it all
                    .getKey(); //int id is now the row number (row key) //of db
            position.setId(id); //update object to set id now from database
        } catch (Sql2oException ex) {
            System.out.println(ex); //oops we have an error!
        }
    }

    @Override
    public List<Position> getAll() {
        try(Connection con = sql2o.open()){
            return con.createQuery("SELECT * FROM positions") //raw sql
                    .executeAndFetch(Position.class); //fetch a list
        }
    }

    @Override
    public Position findById(int id) {
        try(Connection con = sql2o.open()){
            return con.createQuery("SELECT * FROM positions WHERE id = :id")
                    .addParameter("id", id) //key/value pair, key must match above
                    .executeAndFetchFirst(Position.class); //fetch an individual item
        }
    }

    @Override
    public void update(int id, String newPosition, String updated){
        String sql = "UPDATE positions SET (name, updated) = (:name, :updated) WHERE id=:id";
        try(Connection con = sql2o.open()){
            con.createQuery(sql)
                    .addParameter("name", newPosition)
                    .addParameter("updated", updated)
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE from positions WHERE id=:id";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Sql2oException ex){
            System.out.println(ex);
        }
    }

    @Override
    public void clearAllPositions() {
        String sql = "DELETE from positions";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .executeUpdate();
        } catch (Sql2oException ex){
            System.out.println(ex);
        }
    }

    @Override
    public List<Employee> getAllEmployeesByPosition(int positionId) {
        try(Connection con = sql2o.open()){
            return con.createQuery("SELECT * FROM employees WHERE position_id = :positionId")
                    .addParameter("positionId", positionId)
                    .executeAndFetch(Employee.class);
        }
    }
}
