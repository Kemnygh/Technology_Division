package models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    @Test
    public void NewPositionObjectGetsCorrectlyCreated_true() throws Exception {
        Position position = setupNewPosition();
        assertTrue(position instanceof Position);
    }

    @Test
    void getNameInstantiatesCorrectly() {
        Position position = setupNewPosition();
        assertEquals("HOD", position.getName());
    }

    @Test
    void getCreatedInstantiatesCorrectly() {
        Position position = setupNewPosition();
        assertEquals("2022-06-14", position.getCreated_at());
    }

    @Test
    public void PositionInstantiatesWithGetId() throws Exception {
        Position position = setupNewPosition();
        position.setId(1);
        assertEquals(1, position.getId());
    }

    @Test
    public void PositionInstantiatesWithUpdated() throws Exception {
        Position position = setupNewPosition();
        position.setUpdated("2022-04-15");
        assertEquals("2022-04-15", position.getUpdated());
    }

    public Position setupNewPosition(){
        return new Position("HOD", "2022-06-14");
    }

}