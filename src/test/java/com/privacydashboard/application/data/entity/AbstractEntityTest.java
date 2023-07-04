package com.privacydashboard.application.data.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import java.util.UUID;

public class AbstractEntityTest {
    
    @Test
    public void getSetId(){
        AbstractEntity entity = new AbstractEntity() {
            
        };
        entity.setId(new UUID(0, 1));
        assertEquals(new UUID(0, 1), entity.getId());
    }

    @Test
    public void hashCodeNotNullEqualsTest(){
        AbstractEntity entity = new AbstractEntity() {
            
        };
        entity.setId(new UUID(0, 7));
        AbstractEntity entity2 = new AbstractEntity() {
            
        };
        entity2.setId(new UUID(0, 7));
        assertEquals(entity, entity2);
        assertEquals(entity.hashCode(), entity2.hashCode());
    }

    @Test
    public void hashCodeNotNullNotEqualsTest(){
        AbstractEntity entity = new AbstractEntity() {
            
        };
        entity.setId(new UUID(0, 7));
        AbstractEntity entity2 = new AbstractEntity() {
            
        };
        entity2.setId(new UUID(0, 6));
        assertNotEquals(entity, entity2);
        assertNotEquals(entity.hashCode(), entity2.hashCode());
    }


    @Test
    public void hashCodeNullNotEqualsTest(){
        AbstractEntity entity = new AbstractEntity() {
            
        };
        AbstractEntity entity2 = new AbstractEntity() {
            
        };
        assertNotEquals(entity, entity2);
        assertNotEquals(entity.hashCode(), entity2.hashCode());
    }

    @Test
    public void equalsTest(){
        AbstractEntity entity = new AbstractEntity() {
            
        };
        assertEquals(false, entity.equals(null));
    }
}
