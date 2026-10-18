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
        
        //starting of part 2 where users can send messages after they have logged in successfully
        if(loginSuccessful){
            runMessagingMenu(scanner);
        }
        
        scanner.close();
    }
    private static void runMessagingMenu(Scanner scanner) {
        boolean keepGoing = true;
        while (keepGoing) {
            System.out.println("\n=== Messages ===");
            System.out.println("1) Send a new message");
            System.out.println("2) Show all sent messages");
            System.out.println("3) Show total messages sent");
            System.out.println("4) Quit");
            System.out.print("Choose an option: ");
            String option = scanner.nextLine();
            switch (option) {
                case "1":
                    sendNewMessage(scanner);
                    break;
                    case "2":
                        System.out.println(Message.printMessages());
                        break;
                        case "3":
                            System.out.println("Total messages sent: " + Message.returnTotalMessagess());
                            break;
                            case "4":
                                keepGoing = false;
                                break;
                                default:
                                    System.out.println("Please choose a valid option (1-4).");
                                    }
            }
        }
    private static void sendNewMessage(Scanner scanner) {
        System.out.print("Enter recipient cell number (e.g. +27718693002): ");
        String recipient = scanner.nextLine();
        System.out.print("Enter your message (max 250 characters): ");
        String text = scanner.nextLine();
        Message message = new Message(recipient, text);
        String recipientCheck = message.checkRecipientCell(recipient);
        System.out.println(recipientCheck);
        if (!recipientCheck.equals("Cell phone number successfully captured.")) {
            return;
            }
        String lengthCheck = message.checkMessageLength(text);
        System.out.println(lengthCheck);
        if (!lengthCheck.equals("Message ready to send.")) {
            return;
            }
        System.out.println("Message Hash: " + message.createMessageHash());
        System.out.print("Choose an action - Send / Store / Disregard: ");
        String choice = scanner.nextLine();
        System.out.println(message.sentMessage(choice));
    }
}
