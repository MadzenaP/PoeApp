/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.poeapp;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;
/**
 *
 * @author Student
 */
public class Message {
    private static final String RECIPIENT_REGEX = "^\\+\\d{2,12}$";
    
    private static int messageCount = 0;
    private static int totalMessageSent = 0;
    private static final List<String> SENT_MESSAGES = new ArrayList<>();
    
    private static final String JSON_FILE = "storedMessage.json";
    
// ---------- Instance fields ----------
private final String messageID;
private final int messageNumber; // this message's position (0-indexed) among all created
private String recipient;
private String messageText;
private String messageHash;
 
public Message(String recipient, String messageText) {
this.recipient = recipient;
this.messageText = messageText;
this.messageID = generateMessageID();
this.messageNumber = messageCount;
messageCount++;
}
 
// Lets a unit test fix the message ID so hash output is deterministic.
public Message(String recipient, String messageText, String fixedMessageID) {
this.recipient = recipient;
this.messageText = messageText;
this.messageID = fixedMessageID;
this.messageNumber = messageCount;
messageCount++;
}
 
private static String generateMessageID() {
Random random = new Random();
StringBuilder id = new StringBuilder();
for (int i = 0; i < 10; i++) {
id.append(random.nextInt(10));
}
return id.toString();
}
 
// ---------- Validation methods ----------
 
/**
* Ensures the message ID is not more than ten characters.
     * @return 
*/
public boolean checkMessageID() {
return messageID != null && messageID.length() <= 10;
}
 
/**
* Ensures the recipient cell number is no more than ten characters
* long (after the international code) and starts with a code.
     * @param recipientNumber
     * @return 
*/
public String checkRecipientCell(String recipientNumber) {
if (recipientNumber != null && Pattern.matches(RECIPIENT_REGEX, recipientNumber)) {
return "Cell phone number successfully captured.";
}
return "Cell phone number is incorrectly formatted or does not contain an "
+ "international code. Please correct the number and try again.";
}
 
/**
* Ensures the message does not exceed 250 characters.
     * @param message
     * @return 
*/
public String checkMessageLength(String message) {
if (message != null && message.length() <= 250) {
return "Message ready to send.";
}
int over = (message == null ? 0 : message.length()) - 250;
return "Message exceeds 250 characters by " + over + "; please reduce the size.";
}
 
// ---------- Hash creation ----------
 
/**
* Creates the Message Hash: first two characters of the message ID,
* ':', the message's sequential number, ':', then the first and
* last words of the message (punctuation stripped), in caps.
     * @return 
*/
public String createMessageHash() {
String idPrefix = messageID.substring(0, Math.min(2, messageID.length()));
String[] words = messageText.trim().split("\\s+");
String firstWord = stripPunctuation(words[0]);
String lastWord = stripPunctuation(words[words.length - 1]);
this.messageHash = idPrefix + ":" + messageNumber + ":"
+ (firstWord + lastWord).toUpperCase();
return this.messageHash;
}
 
private static String stripPunctuation(String word) {
return word.replaceAll("[^a-zA-Z0-9]", "");
}
 
// ---------- Sending / storing / disregarding ----------
 
/**
* Lets the user choose to send, store, or disregard the message.
* choice: "send", "store", or "disregard" (case-insensitive).
     * @param choice
     * @return 
*/
public String sentMessage(String choice) {
if (choice == null) {
return "Press 0 to delete the message.";
}
switch (choice.trim().toLowerCase()) {
case "send":
if (messageHash == null) {
createMessageHash();
}
    {
        int totalMessagesSent = 0;
        totalMessagesSent++;
    }
SENT_MESSAGES.add(formatMessageDetails());
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
 
/**
* Research note: stores the message as a JSON object, appended to
* a JSON array in storedMessages.json. Built manually here with no
* external dependency; swap in org.json or Gson if your project
* already includes one of those libraries.
* Reference for manual JSON escaping approach:
* https://www.baeldung.com/java-json-escaping
     * @return 
*/
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
List<String> existing = new ArrayList<>();
if (Files.exists(path)) {
String content = Files.readString(path).trim();
if (content.length() > 2) { // more than just "[]"
content = content.substring(1, content.length() - 1).trim();
if (!content.isEmpty()) {
existing.add(content);
}
}
}
existing.add(json);
String output = "[" + String.join(",", existing) + "]";
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
 
/**
* Returns all the messages sent while the program is running.
     * @return 
*/
public static String printMessages() {
if (SENT_MESSAGES.isEmpty()) {
return "No messages have been sent.";
}
return String.join("\n\n", SENT_MESSAGES);
}
 
/**
* Returns the total number of messages sent.
* (Method name matches the exact spelling given in the brief.)
     * @return 
*/
public static int returnTotalMessagess() {
        int totalMessagesSent = 0;
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
SENT_MESSAGES.clear();
}
    
    
}
