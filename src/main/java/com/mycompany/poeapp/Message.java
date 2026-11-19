/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.poeapp;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Random;
import java.util.regex.Pattern;
/**
 *
 * @author Student
 */
// begining of part 2 for the app
public class Message {
    
 // ---------- Shared text, so Main and the methods always agree ----------
    public static final String RECIPIENT_SUCCESS = "Cell phone number successfully captured.";
    public static final String LENGTH_SUCCESS = "Message ready to send.";
    public static final String STORED_SUCCESS = "Message successfully stored.";

    // Maximum number of messages each array can hold
    public static final int MAX_MESSAGES = 100;

    // International code ('+' then digits), no spaces
    private static final String RECIPIENT_REGEX = "^\\+\\d{2,12}$";

    private static final Random RANDOM = new Random();

    // ---------- Counters ----------
    private static int messageCount = 0;      // every message created this run (used in the hash)
    private static int totalMessagesSent = 0; // only messages the user chose to "Send"

    // ---------- Part 3 arrays (filled while the program runs, nothing hard-coded) ----------
    private static Message[] sentMessages = new Message[MAX_MESSAGES];
    private static int sentCount = 0;

    private static Message[] disregardedMessages = new Message[MAX_MESSAGES];
    private static int disregardedCount = 0;

    private static Message[] storedMessages = new Message[MAX_MESSAGES];
    private static int storedCount = 0;

    // Sent + disregarded + stored can each hold MAX_MESSAGES, so these are three times bigger
    private static String[] messageHashes = new String[MAX_MESSAGES * 3];
    private static int hashCount = 0;

    private static String[] messageIDs = new String[MAX_MESSAGES * 3];
    private static int idCount = 0;

    // ---------- JSON file ----------
    private static String jsonFile = "storedMessages.json";
    private static int jsonPosition = 0; // reading position used by the JSON reader

    // ---------- Instance fields ----------
    private final String messageID;
    private final int messageNumber; // this message's position (0-indexed) among all created
    private String sender = "";
    private String recipient;
    private String messageText;
    private String messageHash;

    // ---------- Constructors ----------

    public Message(String recipient, String messageText) {
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageID = generateMessageID();
        this.messageNumber = messageCount;
        messageCount++;
    }

    // Lets a unit test (or the developer) fix the message ID.
    public Message(String recipient, String messageText, String fixedMessageID) {
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageID = fixedMessageID;
        this.messageNumber = messageCount;
        messageCount++;
    }

    // Used only when a stored message is read back from the JSON file.
    private Message(String messageID, String messageHash, String sender,
                    String recipient, String messageText) {
        this.messageID = messageID;
        this.messageHash = messageHash;
        this.sender = sender;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageNumber = 0;
    }


    private static String generateMessageID() {
        String digits = Long.toString(RANDOM.nextLong() & Long.MAX_VALUE);
        String padded = "0000000000" + digits;
        return padded.substring(padded.length() - 10);
    }

    // ---------- Validation methods ----------


    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }


    public static String checkRecipientCell(String recipientNumber) {
        if (recipientNumber != null && Pattern.matches(RECIPIENT_REGEX, recipientNumber)) {
            return RECIPIENT_SUCCESS;
        }
        return "Cell phone number is incorrectly formatted or does not contain an "
                + "international code. Please correct the number and try again.";
    }


    public static String checkMessageLength(String message) {
        if (message != null && message.length() <= 250) {
            return LENGTH_SUCCESS;
        }
        int over = (message == null ? 0 : message.length()) - 250;
        return "Message exceeds 250 characters by " + over + "; please reduce the size.";
    }

    // ---------- Hash creation ----------


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

    // ---------- Send / store / disregard ----------


    public String sentMessage(String choice) {
        String action = (choice == null) ? "" : choice.trim().toLowerCase();
        switch (action) {
            case "send":
                if (sentCount >= MAX_MESSAGES) {
                    return "The sent messages list is full.";
                }
                if (messageHash == null) {
                    createMessageHash();
                }
                sentMessages[sentCount] = this;
                sentCount++;
                totalMessagesSent++;
                recordIdAndHash();
                return "Message successfully sent.";
            case "store":
                if (messageHash == null) {
                    createMessageHash();
                }
                String result = storeMessage();
                if (result.equals(STORED_SUCCESS)) {
                    recordIdAndHash();
                }
                return result;
            case "disregard":
                if (disregardedCount >= MAX_MESSAGES) {
                    return "The disregarded messages list is full.";
                }
                if (messageHash == null) {
                    createMessageHash();
                }
                disregardedMessages[disregardedCount] = this;
                disregardedCount++;
                recordIdAndHash();
                return "Press 0 to delete the message.";
            default:
                return "Invalid choice. Please choose Send, Store or Disregard.";
        }
    }


    private void recordIdAndHash() {
        messageIDs[idCount] = messageID;
        idCount++;
        messageHashes[hashCount] = messageHash;
        hashCount++;
    }


    private static void removeIdAndHash(Message message) {
        for (int i = 0; i < idCount; i++) {
            if (messageIDs[i].equals(message.messageID)) {
                for (int j = i; j < idCount - 1; j++) {
                    messageIDs[j] = messageIDs[j + 1];
                }
                idCount--;
                messageIDs[idCount] = null;
                break;
            }
        }
        for (int i = 0; i < hashCount; i++) {
            if (messageHashes[i].equals(message.messageHash)) {
                for (int j = i; j < hashCount - 1; j++) {
                    messageHashes[j] = messageHashes[j + 1];
                }
                hashCount--;
                messageHashes[hashCount] = null;
                break;
            }
        }
    }


    public String storeMessage() {
        if (storedCount >= MAX_MESSAGES) {
            return "The stored messages list is full.";
        }
        if (messageHash == null) {
            createMessageHash();
        }
        storedMessages[storedCount] = this;
        storedCount++;
        saveStoredMessagesToJson();
        return STORED_SUCCESS;
    }

    // ---------- JSON file: save and load ----------


    private static void saveStoredMessagesToJson() {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < storedCount; i++) {
            Message m = storedMessages[i];
            if (i > 0) {
                json.append(",");
            }
            json.append("\n  {")
                    .append("\"messageID\":\"").append(escapeJson(m.messageID)).append("\",")
                    .append("\"messageHash\":\"").append(escapeJson(m.messageHash)).append("\",")
                    .append("\"sender\":\"").append(escapeJson(m.sender)).append("\",")
                    .append("\"recipient\":\"").append(escapeJson(m.recipient)).append("\",")
                    .append("\"message\":\"").append(escapeJson(m.messageText)).append("\"")
                    .append("}");
        }
        json.append(storedCount > 0 ? "\n]" : "]");
        try {
            Files.writeString(Path.of(jsonFile), json.toString());
        } catch (IOException e) {
            System.out.println("Could not write to the JSON file: " + e.getMessage());
        }
    }


    public static void loadStoredMessagesFromJson() {
        Path path = Path.of(jsonFile);
        if (!Files.exists(path)) {
            return;
        }
        String content;
        try {
            content = Files.readString(path);
        } catch (IOException e) {
            System.out.println("Could not read the JSON file: " + e.getMessage());
            return;
        }

        // Start from an empty stored array so nothing is loaded twice
        for (int i = 0; i < storedCount; i++) {
            removeIdAndHash(storedMessages[i]);
            storedMessages[i] = null;
        }
        storedCount = 0;

        jsonPosition = 0;
        while (jsonPosition < content.length()) {
            if (content.charAt(jsonPosition) == '{') {
                jsonPosition++;
                String id = "";
                String hash = "";
                String sender = "";
                String recipient = "";
                String text = "";

                // Read "key":"value" pairs until the closing brace
                while (jsonPosition < content.length() && content.charAt(jsonPosition) != '}') {
                    if (content.charAt(jsonPosition) == '"') {
                        String key = readJsonString(content);
                        jsonPosition = content.indexOf('"', jsonPosition); // opening quote of the value
                        if (jsonPosition == -1) {
                            return;
                        }
                        String value = readJsonString(content);
                        switch (key) {
                            case "messageID":
                                id = value;
                                break;
                            case "messageHash":
                                hash = value;
                                break;
                            case "sender":
                                sender = value;
                                break;
                            case "recipient":
                                recipient = value;
                                break;
                            case "message":
                                text = value;
                                break;
                            default:
                                break;
                        }
                    } else {
                        jsonPosition++;
                    }
                }

                if (storedCount < MAX_MESSAGES) {
                    Message loaded = new Message(id, hash, sender, recipient, text);
                    storedMessages[storedCount] = loaded;
                    storedCount++;
                    loaded.recordIdAndHash();
                }
            }
            jsonPosition++;
        }
    }


    private static String readJsonString(String text) {
        StringBuilder value = new StringBuilder();
        jsonPosition++; // skip the opening quote
        while (jsonPosition < text.length() && text.charAt(jsonPosition) != '"') {
            char c = text.charAt(jsonPosition);
            if (c == '\\' && jsonPosition + 1 < text.length()) {
                jsonPosition++;
                char next = text.charAt(jsonPosition);
                switch (next) {
                    case 'n':
                        value.append('\n');
                        break;
                    case 'r':
                        value.append('\r');
                        break;
                    case 't':
                        value.append('\t');
                        break;
                    default:
                        value.append(next); // covers \" and \\
                        break;
                }
            } else {
                value.append(c);
            }
            jsonPosition++;
        }
        jsonPosition++; // skip the closing quote
        return value.toString();
    }

    private static String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    // ---------- Part 3: reading the arrays ----------


    public static String[] getSentMessageTexts() {
        return textsOf(sentMessages, sentCount);
    }


    public static String[] getDisregardedMessageTexts() {
        return textsOf(disregardedMessages, disregardedCount);
    }


    public static String[] getStoredMessageTexts() {
        return textsOf(storedMessages, storedCount);
    }


    public static String[] getMessageHashList() {
        return Arrays.copyOf(messageHashes, hashCount);
    }

   
    public static String[] getMessageIDList() {
        return Arrays.copyOf(messageIDs, idCount);
    }

    private static String[] textsOf(Message[] list, int count) {
        String[] texts = new String[count];
        for (int i = 0; i < count; i++) {
            texts[i] = list[i].messageText;
        }
        return texts;
    }

    // ---------- Part 3: stored messages menu features ----------


    public static String displaySendersAndRecipients() {
        if (storedCount == 0) {
            return "There are no stored messages.";
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < storedCount; i++) {
            if (i > 0) {
                result.append("\n");
            }
            result.append("Sender: ").append(storedMessages[i].sender)
                    .append(" | Recipient: ").append(storedMessages[i].recipient);
        }
        return result.toString();
    }


    public static String findLongestStoredMessage() {
        if (storedCount == 0) {
            return "There are no stored messages.";
        }
        Message longest = storedMessages[0];
        for (int i = 1; i < storedCount; i++) {
            if (storedMessages[i].messageText.length() > longest.messageText.length()) {
                longest = storedMessages[i];
            }
        }
        return longest.messageText;
    }

    public static Message searchByMessageID(String id) {
        if (id == null) {
            return null;
        }
        for (int i = 0; i < sentCount; i++) {
            if (sentMessages[i].messageID.equals(id)) {
                return sentMessages[i];
            }
        }
        for (int i = 0; i < storedCount; i++) {
            if (storedMessages[i].messageID.equals(id)) {
                return storedMessages[i];
            }
        }
        for (int i = 0; i < disregardedCount; i++) {
            if (disregardedMessages[i].messageID.equals(id)) {
                return disregardedMessages[i];
            }
        }
        return null;
    }


    public static String[] searchByRecipient(String recipient) {
        int matches = 0;
        for (int i = 0; i < sentCount; i++) {
            if (sentMessages[i].recipient.equals(recipient)) {
                matches++;
            }
        }
        for (int i = 0; i < storedCount; i++) {
            if (storedMessages[i].recipient.equals(recipient)) {
                matches++;
            }
        }
        String[] found = new String[matches];
        int next = 0;
        for (int i = 0; i < sentCount; i++) {
            if (sentMessages[i].recipient.equals(recipient)) {
                found[next] = sentMessages[i].messageText;
                next++;
            }
        }
        for (int i = 0; i < storedCount; i++) {
            if (storedMessages[i].recipient.equals(recipient)) {
                found[next] = storedMessages[i].messageText;
                next++;
            }
        }
        return found;
    }


    public static String deleteMessageByHash(String hash) {
        if (hash == null) {
            return "Message hash not found.";
        }
        for (int i = 0; i < sentCount; i++) {
            if (hash.equals(sentMessages[i].messageHash)) {
                Message deleted = sentMessages[i];
                for (int j = i; j < sentCount - 1; j++) {
                    sentMessages[j] = sentMessages[j + 1];
                }
                sentCount--;
                sentMessages[sentCount] = null;
                removeIdAndHash(deleted);
                return deletedText(deleted);
            }
        }
        for (int i = 0; i < storedCount; i++) {
            if (hash.equals(storedMessages[i].messageHash)) {
                Message deleted = storedMessages[i];
                for (int j = i; j < storedCount - 1; j++) {
                    storedMessages[j] = storedMessages[j + 1];
                }
                storedCount--;
                storedMessages[storedCount] = null;
                removeIdAndHash(deleted);
                saveStoredMessagesToJson(); // keep the JSON file in step with the array
                return deletedText(deleted);
            }
        }
        for (int i = 0; i < disregardedCount; i++) {
            if (hash.equals(disregardedMessages[i].messageHash)) {
                Message deleted = disregardedMessages[i];
                for (int j = i; j < disregardedCount - 1; j++) {
                    disregardedMessages[j] = disregardedMessages[j + 1];
                }
                disregardedCount--;
                disregardedMessages[disregardedCount] = null;
                removeIdAndHash(deleted);
                return deletedText(deleted);
            }
        }
        return "Message hash not found.";
    }

    private static String deletedText(Message deleted) {
        String shown = deleted.messageText;
        if (shown.endsWith(".")) {
            shown = shown.substring(0, shown.length() - 1); // matches the wording in the brief
        }
        return "Message: \"" + shown + "\" successfully deleted.";
    }


    public static String reportStoredMessages() {
        if (storedCount == 0) {
            return "There are no stored messages.";
        }
        StringBuilder report = new StringBuilder();
        for (int i = 0; i < storedCount; i++) {
            if (i > 0) {
                report.append("\n\n");
            }
            Message m = storedMessages[i];
            report.append("Message ID: ").append(m.messageID)
                    .append("\nMessage Hash: ").append(m.messageHash)
                    .append("\nSender: ").append(m.sender)
                    .append("\nRecipient: ").append(m.recipient)
                    .append("\nMessage: ").append(m.messageText);
        }
        return report.toString();
    }

    // ---------- Reporting on sent messages ----------


    public String displayMessageDetails() {
        return "Message ID: " + messageID
                + "\nMessage Hash: " + messageHash
                + "\nRecipient: " + recipient
                + "\nMessage: " + messageText;
    }


    public static String printMessages() {
        if (sentCount == 0) {
            return "No messages have been sent.";
        }
        StringBuilder all = new StringBuilder();
        for (int i = 0; i < sentCount; i++) {
            if (i > 0) {
                all.append("\n\n");
            }
            all.append(sentMessages[i].displayMessageDetails());
        }
        return all.toString();
    }


    public static int returnTotalMessagess() {
        return totalMessagesSent;
    }

    // ---------- Getters and setters ----------

    public String getMessageID() {
        return messageID;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
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

    // ---------- Helpers for unit tests ----------


    static void resetCountersForTesting() {
        clearInMemoryDataForTesting();
        jsonFile = System.getProperty("java.io.tmpdir") + java.io.File.separator
                + "poeapp-test-storedMessages.json";
        try {
            Files.deleteIfExists(Path.of(jsonFile));
        } catch (IOException e) {
            System.out.println("Could not delete the test JSON file: " + e.getMessage());
        }
    }


    static void clearInMemoryDataForTesting() {
        messageCount = 0;
        totalMessagesSent = 0;
        sentMessages = new Message[MAX_MESSAGES];
        sentCount = 0;
        disregardedMessages = new Message[MAX_MESSAGES];
        disregardedCount = 0;
        storedMessages = new Message[MAX_MESSAGES];
        storedCount = 0;
        messageHashes = new String[MAX_MESSAGES * 3];
        hashCount = 0;
        messageIDs = new String[MAX_MESSAGES * 3];
        idCount = 0;
    }
}
    
  
    
    

