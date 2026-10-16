
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.poeapp;

import java.util.regex.Pattern; 
/**
 *
 * @author Student
 */
class AppLogin {
    
    private static final String CELL_REGEX = "^\\+27\\d{7,10}$";
    
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String cellPhoneNumber;
    
    public AppLogin(){
        
    }
    
    public AppLogin(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    //user validation
    // checks if username contains an underscore and is more than five characters long
    
    public boolean checkUserName(String username){
        return username != null
                && username.contains("_")
                && username.length()<= 5;
    }
    // password check 
    //password has at least 8 characters
    
    public boolean checkPasswordComplexity(String password){
        if (password == null){
            return false;
        }
        boolean hasMinimumLength = password.length() >=8;
        boolean hasCaptialLetter = Pattern.compile("[A-Z]").matcher(password).find();
        boolean hasDigit = Pattern.compile("[0-9]").matcher(password).find();
        boolean hasSpecial = Pattern.compile("[^a-zA-Z0-9]").matcher(password).find();
        return hasMinimumLength && hasCaptialLetter && hasDigit && hasSpecial;
    }
   
    public boolean checkCellPhoneNumber(String cellPhoneNumber){
        return cellPhoneNumber != null && Pattern.matches(CELL_REGEX, cellPhoneNumber);
    }
    // registration
    // validates the supplied username,password and cellphone number
    
    public String registerUser(String username, String password, String cellPhoneNumber){
        if (!checkUserName(username)){
            return "User has not been registered";
                    
        }
        if (!checkPasswordComplexity(password)){
            return "User has not been registered";
        }
        if (!checkCellPhoneNumber(cellPhoneNumber)){
            return "User has not been registered";
        }
        
        this.username = username;
        this.password = password;
        this.cellPhoneNumber = cellPhoneNumber;
        return "User Successfully registered";
    }
    public boolean loginUser(String username, String password){
        return this.username != null
                && this.username.equals(username)
                && this.password != null
                && this.password.equals(password);
    }
    
    public String returnLoginStatus(boolean loginSuccessful){
        if(loginSuccessful){
            return "Welcome " + firstName + ", " + lastName + " happy to see you again.";
        }
        return "Username or password is incorrect, try again";
    }
    public String getFirstName(){
        return firstName;
    }
    
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }
    
    public String getLastName(){
        return lastName;
    }
    
    public void setLastName(String lastName){
        this.lastName = lastName;
    }
    
    public String getUsername(){
        return username;
    }
    
    
    public String getCellPhoneNumber(){
        return cellPhoneNumber;
    }
}
