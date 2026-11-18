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
        
        System.out.print("Enter first name: ");
        login.setFirstName(scanner.nextLine());
        
        System.out.println("Enter last name: ");
        login.setLastName(scanner.nextLine());
        
        String registrationMessage = "";
        boolean registered = false;
        
        while (!registered){
            System.out.print("Enter a username (must contain '_' and be more than 5 characters): ");
            String username = scanner.nextLine();
            
            System.out.println("Enter password: ");
            String password = scanner.nextLine();
            
            System.out.print("Enter a South African cell phone number");
            String cellPhoneNumber = scanner.nextLine();
            
            printRegistrationFeedback(login, username, password, cellPhoneNumber);
            
            registrationMessage = login.registerUser(username, password, cellPhoneNumber);
            System.out.println(registrationMessage);
            
            registered = registrationMessage.equals(AppLogin.REGISTRATION_SUCCESS);
            if(!registered){
                System.out.println("please try again.\n");
            }
        }
        
       
        System.out.println("\n=== Login ===");
        boolean loggedIn = false;
        while (!loggedIn){
            System.out.print("Enter username: ");
        String loginUsername = scanner.nextLine();
        
        System.out.println("Enter password: ");
        String loginPassword = scanner.nextLine();
        
        loggedIn = login.loginUser(loginUsername, loginPassword);
            System.out.println(login.returnLoginStatus(loggedIn));
            
        }
        
        Message.loadStoredMessagesFromJson();
        System.out.println("\n Welcome to QuickChat.");
        runMainMenu(scanner, login);
        
        
        scanner.close();
    }
    
    private static void printRegistrationFeedback(AppLogin login, String username, String password, String cellPhoneNumber){
        if (login.checkUserName(username)){
            System.out.println("username successfully captured.");
        }else {
            System.out.println("Username is not correctly formatted.");
        }
        if (login.checkPasswordComplexity(password)){
            System.out.println("Password successfully captured.");
        }else {
            System.out.println("Password is not correctly formatted.");
        }
        if (login.checkCellPhoneNumber(cellPhoneNumber)){
            System.out.println("Cell phone number successfully added.");
        }else {
            System.out.println("Cell phone number not correctly formatted.");
        }
    }
    
    //-------------- QuickChat Main Menu ---------------------
    
    private static void runMainMenu(Scanner Scanner, AppLogin login) {
        boolean keepGoing = true;
        while (keepGoing) {
            System.out.println("\n=== QuickChat Menu ===");
            System.out.println("1) Send a new message");
            System.out.println("2) Show all sent messages");
            System.out.println("3) Show total messages sent");
            System.out.println("4) Quit");
            System.out.print("Choose an option: ");
            String option = scanner.nextLine().trim();
            
            
            switch (option){
                case "1":
                    sendMessages(scanner, login);
                    break;
                case "2":
                    System.out.println("Coming Soon.");
                    break;
                case "3":
                    System.out.println("Goodbye.");
                    break;
                case "4":
                    runStoredMessagesMenu(scanner);
                    break;
                default:
                    System.out.println("Choose a valid option");
            }
        }
    }
    
    //------------ Send Messages -------------
            
    private static void sendMessage(Scanner scanner, AppLogin login) {
        int howMany = readWholeNumber(scanner, "How many messages do you wish to enter? ", 1, Messages.MAX_MESSAGES);
        
        for (int i = 1; i<= howMany; i++){
            System.out.println("\n--- Message" + i + "of" + howMany + "---");
            
            String recipient = readRecipient(scanner);
            String text = readMessageText(scanner);
            
            Message message = new Message(recipient, text);
            message.setSender(login.getCellPhoneNumber());
            message.createMessageHash();
            
            int choice = readChoice(scanner);
            if (choice == 1){
                System.out.println(message.sentMessage("send"));
                System.out.println(message.displayMessageDetails());
            }else if (choice ==2){
                System.out.println(message.sentMessage("disregard"));
                String press = scanner.nextLine().trim();
                if (press.equals("0"))
                    System.out.println("Message deleted.");
            }
        }else{
                System.out.println(message.sentMessage("store"));
                }
        System.out.println("\nTotal number of messages sent:" + Message.returnTotalMessagess());
    }
    //keeps asking until user types a whole number between min and max//
    private static int readWholeNumber(Scanner scanner, String prompt, int min, int max){
        while(true){
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try{
                int number = Interger.parseInt(input);
                if (number >= min && number <= max){
                    return number;
                }
            }catch (NumberFormatExeption e){
                // fall through to the mesage below
            }
            System.out.println("Invalid input. Please enter a whole number from" + min + "to" + max + ".");
        }
        }
        private static String readRecipient(Scanner scanner){
            while (true){
                System.out.print("Enter the recipient cell number: ");
            }
    }
    }
    
           
      