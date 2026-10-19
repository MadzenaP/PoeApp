/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.poeapp;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Random;
import java.util.regex.Pattern;
/**
 *
 * @author Student
 */
// begining of part 2 for the app
public class Message {
    
    private static final String RECIPIENT_REGEX = "^\\+\\d{2,12}$";
    
    private static int messageCount = 0;
    private static int totalMessagesSent = 0;
    private static String sentMessagesLog = "";
    
    private static final String JSON_FILE = "storedMessages.json";

    static boolean printMessage() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    // Instance fields
    
    private final String messageID;
    private final int messageNumber;
    private String messageText;
    private String messageHash;
    private String recipient;
    
    public Message(String recipient, String messageText){
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageNumber = messageCount;
        this.messageID = generateMessageID();
        messageCount++;
    }
    
    public Message(String recipient, String messageText, String fixedMessageID){
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageID = fixedMessageID;
        this.messageNumber = messageCount;
        messageCount++;
    }
    
    private static String generateMessageID(){
        Random random = new Random();
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < 10; i++){
            id.append(random.nextInt(10));
        }
        return id.toString();
    }
    
    // validation methods, ensures the message is less than 10 characters
     public boolean checkMessageID() {
return messageID != null && messageID.length() <= 10;
}
 

 //Ensures the recipient cell number is no more than ten characters
 //long (after the international code) and starts with a code.

public String checkRecipientCell(String recipientNumber) {
if (recipientNumber != null && Pattern.matches(RECIPIENT_REGEX, recipientNumber)) {
return "Cell phone number successfully captured.";
}
return "Cell phone number is incorrectly formatted or does not contain an "
+ "international code. Please correct the number and try again.";
}
 

 //Ensures the message does not exceed 250 characters.

public String checkMessageLength(String message) {
if (message != null && message.length() <= 250) {
return "Message ready to send.";
}
int over = (message == null ? 0 : message.length()) - 250;
return "Message exceeds 250 characters by " + over + "; please reduce the size.";
}
 
// ---------- Hash creation ----------
 

 //Creates the Message Hash: first two characters of the message ID,
 //':', the message's sequential number, ':', then the first and
 //last words of the message (punctuation stripped), in caps.

public String createMessageHash() {
String idPrefix = messageID.substring(0, Math.min(2, messageID.length()));
String trimmed = messageText.trim();
int firstSpace = trimmed.indexOf(' ');
int lastSpace = trimmed.lastIndexOf(' ');
String firstWord = (firstSpace == -1) ? trimmed : trimmed.substring(0, firstSpace);
String lastWord = (lastSpace == -1) ? trimmed : trimmed.substring(lastSpace + 1);
firstWord = stripPunctuation(firstWord);
lastWord = stripPunctuation(lastWord);
this.messageHash = idPrefix + ":" + messageNumber + ":"
+ (firstWord + lastWord).toUpperCase();
return this.messageHash;
}
 
private static String stripPunctuation(String word) {
return word.replaceAll("[^a-zA-Z0-9]", "");
}
 
// ---------- Sending / storing / disregarding ----------
 

//Lets the user choose to send, store, or disregard the message.
//choice: "send", "store", or "disregard" (case-insensitive).

public String sentMessage(String choice) {
if (choice == null) {
return "Press 0 to delete the message.";
}
switch (choice.trim().toLowerCase()) {
case "send":
if (messageHash == null) {
createMessageHash();
}
totalMessagesSent++;
if (!sentMessagesLog.isEmpty()) {
sentMessagesLog += "\n\n";
}
sentMessagesLog += formatMessageDetails();
return "Message successfully sent.";
case "store":
if (messageHash == null) {
createMessageHash();
}
storeMessage();
return "Message successfully stored.";
case "disregard":
default:
return "Press 0 to delete the message.";
}
}
 
 //Research note: stores the message as a JSON object, appended to
 //a JSON array in storedMessages.json. Built manually here with no
 //external dependency; swap in org.json or Gson if your project
 //already includes one of those libraries.
 //Reference for manual JSON escaping approach:


public String storeMessage() {
if (messageHash == null) {
createMessageHash();
}
String json = "{"
+ "\"messageID\":\"" + escapeJson(messageID) + "\","
+ "\"messageHash\":\"" + escapeJson(messageHash) + "\","
+ "\"recipient\":\"" + escapeJson(recipient) + "\","
+ "\"message\":\"" + escapeJson(messageText) + "\""
+ "}";
try {
Path path = Path.of(JSON_FILE);
String output = "[" + json + "]";
if (Files.exists(path)) {
String content = Files.readString(path).trim();
if (content.length() > 2 && content.endsWith("]")) {
// keep the existing entries and add the new one before the closing bracket
output = content.substring(0, content.length() - 1) + "," + json + "]";
}
}
Files.writeString(path, output, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
} catch (IOException e) {
System.out.println("Could not write to JSON file: " + e.getMessage());
}
return "Message successfully stored.";
}
 
private static String escapeJson(String value) {
if (value == null) {
return "";
}
return value.replace("\\", "\\\\").replace("\"", "\\\"");
}
 
// ---------- Reporting ----------
 
private String formatMessageDetails() {
return "Message ID: " + messageID
+ "\nMessage Hash: " + messageHash
+ "\nRecipient: " + recipient
+ "\nMessage: " + messageText;
}
 
 //Returns all the messages sent while the program is running.

public static String printMessages() {
if (sentMessagesLog.isEmpty()) {
return "No messages have been sent.";
}
return sentMessagesLog;
}
 

// Returns the total number of messages sent.
//(Method name matches the exact spelling given in the brief.)

public static int returnTotalMessagess() {
return totalMessagesSent;
}
 
// ---------- Getters ----------
 
public String getMessageID() {
return messageID;
}
 
public String getRecipient() {
return recipient;
}
 
public String getMessageText() {
return messageText;
}
 
public String getMessageHash() {
return messageHash;
}
 
public int getMessageNumber() {
return messageNumber;
}
 
// For unit tests that need a clean slate between test runs.
static void resetCountersForTesting() {
messageCount = 0;
totalMessagesSent = 0;
sentMessagesLog = "";
}
}
    
  
    
    

