package dao;

import models.Department;
import models.Employee;
import models.Position;

import java.util.List;

public interface PositionDao {
    // LIST
    List<Position> getAll();

    // CREATE
    void add(Position position);

    // READ
    Position findById(int id);

    // UPDATE
    void update(int id, String name);

    // DELETE
    void deleteById(int id);
    void clearAllPositions();

    List<Employee> getAllEmployeesByPosition(int positionId);
}
