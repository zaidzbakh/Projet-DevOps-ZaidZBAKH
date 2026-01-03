package com.devops;

import org.junit.Test;
import static org.junit.Assert.*;

public class AppTest {
    @Test
    public void testGetMessage() {
        App app = new App();
        String expected = "Bonjour et bon courage dans votre projet en DevOps";
        assertEquals(expected, app.getMessage());
    }
}