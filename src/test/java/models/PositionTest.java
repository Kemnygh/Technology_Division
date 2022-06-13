package models;

import org.junit.jupiter.api.Test;

import java.util.Date;

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
    void getCreatedAtInstantiatesWithCurrentDate() {
        Position position = setupNewPosition();
        Date todayTime = new Date();
        assertEquals(position.getCreatedAt(), todayTime);
    }

    public Position setupNewPosition(){
        return new Position("HOD");
    }

}