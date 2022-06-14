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



    public Position setupNewPosition(){
        return new Position("HOD", "2022-06-14");
    }

}