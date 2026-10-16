/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.poeapp;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Student
 */
public class AppLoginTest {
    
    public AppLoginTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of checkUserName method, of class AppLogin.
     */
    @Test
    public void testCheckUserName() {
        System.out.println("checkUserName");
        String username = "kyl_";
        AppLogin instance = new AppLogin();
        boolean expResult = true;
        boolean result = instance.checkUserName(username);
        assertEquals(expResult, result);
        
    }
    
    @Test
    public void testCheckInvalidUserName() {
        System.out.println("checkUserName");
        String username = "kyl";
        AppLogin instance = new AppLogin();
        boolean expResult = false;
        boolean result = instance.checkUserName(username);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of checkPasswordComplexity method, of class AppLogin.
     */
    @Test
    public void testCheckPasswordComplexity() {
        System.out.println("checkPasswordComplexity");
        String password = "600McDon@ald&&";
        AppLogin instance = new AppLogin();
        boolean expResult = true;
        boolean result = instance.checkPasswordComplexity(password);
        assertEquals(expResult, result);
      
    }
    
    @Test
    public void testCheckWeakPasswordComplexity() {
        System.out.println("checkPasswordComplexity");
        String password = "600Mcdonald";
        AppLogin instance = new AppLogin();
        boolean expResult = false;
        boolean result = instance.checkPasswordComplexity(password);
        assertEquals(expResult, result);
      
    }

    /**
     * Test of checkCellPhoneNumber method, of class AppLogin.
     */
    @Test
    public void testCheckCellPhoneNumber() {
        System.out.println("checkCellPhoneNumber");
        String cellPhoneNumber = "+27838968976";
        AppLogin instance = new AppLogin();
        boolean expResult = true;
        boolean result = instance.checkCellPhoneNumber(cellPhoneNumber);
        assertEquals(expResult, result);
       
    }
    
    @Test
    public void testCheckInvalidCellPhoneNumber() {
        System.out.println("checkCellPhoneNumber");
        String cellPhoneNumber = "0838968976";
        AppLogin instance = new AppLogin();
        boolean expResult = false;
        boolean result = instance.checkCellPhoneNumber(cellPhoneNumber);
        assertEquals(expResult, result);
       
    }

    /**
     * Test of registerUser method, of class AppLogin.
     */
    @Test
    public void testRegisterUser() {
        System.out.println("registerUser");
        String username = "kyl_";
        String password = "600McDon@ald&&";
        String cellPhoneNumber = "+27838968976";
        AppLogin instance = new AppLogin();
        String expResult = "User Successfully registered";
        String result = instance.registerUser(username, password, cellPhoneNumber);
        assertEquals(expResult, result);
        
    }
    
     @Test
    public void testInvalidRegisterUser() {
        System.out.println("registerUser");
        String username = "kyl";
        String password = "600McDonald";
        String cellPhoneNumber = "0838968976";
        AppLogin instance = new AppLogin();
        String expResult = "User has not been registered";
        String result = instance.registerUser(username, password, cellPhoneNumber);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of loginUser method, of class AppLogin.
     */
    @Test
    public void testLoginUser() {
        System.out.println("loginUser");
        String username = "kyl_";
        String password = "600McDon@ald&&";
        String cellPhoneNumber = "+27838968976";
        AppLogin instance = new AppLogin();
        instance.registerUser(username, password, cellPhoneNumber);
        boolean expResult = true;
        boolean result = instance.loginUser(username, password);
        assertEquals(expResult, result);
       
    }
    
     @Test
    public void testInvalidLoginUser() {
        System.out.println("loginUser");
        String username = "kyl";
        String password = "600McDonald";
         String cellPhoneNumber = "0838968976";
        AppLogin instance = new AppLogin();
        boolean expResult = false;
        boolean result = instance.loginUser(username, password);
        assertEquals(expResult, result);
       
    }

    /**
     * Test of returnLoginStatus method, of class AppLogin.
     */
    @Test
    public void testReturnLoginStatus() {
        System.out.println("returnLoginStatus");
        AppLogin instance = new AppLogin();
        instance.setFirstName("Kyle");
        instance.setLastName("Smith");
        boolean loginSuccessful = true;
        String expResult = "Welcome Kyle, Smith happy to see you again.";
        String result = instance.returnLoginStatus(loginSuccessful);
        assertEquals(expResult, result);
      
    }
    
    @Test
    public void testInvalidReturnLoginStatus() {
        System.out.println("returnLoginStatus");
        boolean loginSuccessful = false;
        AppLogin instance = new AppLogin();
        String expResult = "Username or password is incorrect, try again";
        String result = instance.returnLoginStatus(loginSuccessful);
        assertEquals(expResult, result);
      
    }

    /**
     * Test of getFirstName method, of class AppLogin.
     */
    @Test
    public void testGetFirstName() {
        System.out.println("getFirstName");
        AppLogin instance = new AppLogin("Kyle", "Smith");
        String expResult = "Kyle";
        String result = instance.getFirstName();
        assertEquals(expResult, result);
        
    }

    /**
     * Test of setFirstName method, of class AppLogin.
     */
    @Test
    public void testSetFirstName() {
        System.out.println("setFirstName");
        String firstName = "Kyle";
        AppLogin instance = new AppLogin();
        instance.setFirstName(firstName);
       
    }

    /**
     * Test of getLastName method, of class AppLogin.
     */
    @Test
    public void testGetLastName() {
        System.out.println("getLastName");
        AppLogin instance = new AppLogin("Kyle", "Smith");
        String expResult = "Smith";
        String result = instance.getLastName();
        assertEquals(expResult, result);
        
    }

    /**
     * Test of setLastName method, of class AppLogin.
     */
    @Test
    public void testSetLastName() {
        System.out.println("setLastName");
        String lastName = "Smith";
        AppLogin instance = new AppLogin();
        instance.setLastName(lastName);

    }

    /**
     * Test of getUsername method, of class AppLogin.
     */
    @Test
    public void testGetUsername() {
        System.out.println("getUsername");
        AppLogin instance = new AppLogin();
        String expResult = "";
        String result = instance.getUsername();
        assertEquals(expResult, result);
      
    }

    /**
     * Test of getCellPhoneNumber method, of class AppLogin.
     */
    @Test
    public void testGetCellPhoneNumber() {
        System.out.println("getCellPhoneNumber");
        AppLogin instance = new AppLogin();
        instance.registerUser("kyl_", "600McDon@ald&&", "+27838968976");
        String expResult = "";
        String result = instance.getCellPhoneNumber();
        assertEquals(expResult, result);
    }
    
}
