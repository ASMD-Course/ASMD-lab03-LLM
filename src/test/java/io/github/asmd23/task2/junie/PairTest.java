package io.github.asmd23.task2.junie;

import io.github.asmd23.task2.Pair;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Pair utility class.
 */
public class PairTest {

    @Test
    public void testConstructorAndGetters() {
        // Test with String and Integer
        Pair<String, Integer> pair1 = new Pair<>("test", 42);
        assertEquals("test", pair1.get1());
        assertEquals(42, pair1.get2());
        
        // Test with null values
        Pair<String, String> pair2 = new Pair<>(null, null);
        assertNull(pair2.get1());
        assertNull(pair2.get2());
        
        // Test with two different objects
        Object obj1 = new Object();
        Object obj2 = new Object();
        Pair<Object, Object> pair3 = new Pair<>(obj1, obj2);
        assertSame(obj1, pair3.get1());
        assertSame(obj2, pair3.get2());
    }
    
    @Test
    public void testEquals() {
        // Test equality with identical pairs
        Pair<String, Integer> pair1 = new Pair<>("test", 42);
        Pair<String, Integer> pair2 = new Pair<>("test", 42);
        assertEquals(pair1, pair2);
        
        // Test equality with different pairs
        Pair<String, Integer> pair3 = new Pair<>("different", 42);
        Pair<String, Integer> pair4 = new Pair<>("test", 43);
        Pair<Integer, String> pair5 = new Pair<>(42, "test");
        
        assertNotEquals(pair1, pair3);
        assertNotEquals(pair1, pair4);
        assertNotEquals(pair1, pair5);
        
        // Test equality with null and different types
        assertNotEquals(pair1, null);
        assertNotEquals(pair1, "not a pair");
        
        // Test reflexivity
        assertEquals(pair1, pair1);
        
        // Test with null values
        Pair<String, Integer> pairWithNull1 = new Pair<>(null, 42);
        Pair<String, Integer> pairWithNull2 = new Pair<>(null, 42);
        Pair<String, Integer> pairWithNull3 = new Pair<>("test", null);
        
        assertEquals(pairWithNull1, pairWithNull2);
        assertNotEquals(pairWithNull1, pairWithNull3);
        assertNotEquals(pairWithNull1, pair1);
    }
    
    @Test
    public void testHashCode() {
        // Test that equal objects have equal hash codes
        Pair<String, Integer> pair1 = new Pair<>("test", 42);
        Pair<String, Integer> pair2 = new Pair<>("test", 42);
        
        assertEquals(pair1.hashCode(), pair2.hashCode());
        
        // Test with null values
        Pair<String, Integer> pairWithNull1 = new Pair<>(null, 42);
        Pair<String, Integer> pairWithNull2 = new Pair<>(null, 42);
        
        assertEquals(pairWithNull1.hashCode(), pairWithNull2.hashCode());
    }
    
    @Test
    public void testToString() {
        // Test the string representation
        Pair<String, Integer> pair = new Pair<>("test", 42);
        String expected = "Pair [e1=test, e2=42]";
        assertEquals(expected, pair.toString());
        
        // Test with null values
        Pair<String, Integer> pairWithNull = new Pair<>(null, null);
        String expectedWithNull = "Pair [e1=null, e2=null]";
        assertEquals(expectedWithNull, pairWithNull.toString());
    }
}