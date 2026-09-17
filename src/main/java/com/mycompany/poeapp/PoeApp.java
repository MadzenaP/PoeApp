/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.poeapp;

import java.util.Scanner;

/**
 *
 * @author Student
 */
public class PoeApp {

    public static void main(String[] args) {
       Scanner scanner = new Scanner(System.in);
        AppLogin login = new AppLogin();
        
        System.out.println("=== User Registation===");
        
        System.out.print("Enter first name:");
        login.setFirstName(scanner.nextLine());
        
        System.out.println("Enter last name: ");
        login.setLastName(scanner.nextLine());
        
        String registrationMessage = "";
        boolean registered = false;
        
        while (!registered){
            System.out.print("Enter a username (must contain '_' and be more than 5 characters): ");
            String username = scanner.nextLine();
            
            System.out.println("Enter a valid password: ");
            String password = scanner.nextLine();
            
            System.out.print("Enter a South African cell phone number");
            String cellPhoneNumber = scanner.nextLine();
            
            registrationMessage = login.registerUser(username, password, cellPhoneNumber);
            System.out.println(registrationMessage);
            
            registered = registrationMessage.equals("USer successfully registered.");
            if(!registered){
                System.out.println("please try again.\n");
            }
        }
        System.out.println("\n=== Login ===");
        System.out.print("Enter username: ");
        String loginUsername = scanner.nextLine();
        
        System.out.println("Enter password: ");
        String loginPassword = scanner.nextLine();
        
        boolean loginSuccessful = login.loginUser(loginUsername, loginPassword);
        System.out.println(login.returnLoginStatus(loginSuccessful));
        
        scanner.close();
    }
}
