
package org.examproject.blog.util;

import org.junit.*;
import static org.junit.Assert.*;

/**
 * @author hiroxpepe
 */
public class EntryUtilsTest {
    
    public EntryUtilsTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testCreateCode() {
        System.out.println("createCode");
        EntryUtils instance = new EntryUtils();
        String expResult = "";
        String result = instance.createCode();
        //assertEquals(expResult, result);
        assertNotNull(result);
    }
}