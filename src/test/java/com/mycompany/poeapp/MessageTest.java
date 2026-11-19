/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.poeapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Student
 */
public class MessageTest {
    private static final String SENDER = "+27838968976";
     private static final String TEXT_1 = "Did you get the cake?";
     private static final String TEXT_2 = "Where are you? You are late! I have asked you to be on time.";
     private static final String TEXT_3 = "Yohoooo, I am at your gate.";
     private static final String TEXT_4 = "It is dinner time!";
     private static final String TEXT_5 = "Ok, I am leaving without you.";
     
     
     private Message message2; // kept so the delete test can use its hash
     
     
     @BeforeEach
     public void setUp() {
         Message.resetCountersForTesting();
     }
     
     private void populateTestData() {
         Message message1 = new Message("+27834557896", TEXT_1);
         message1.setSender(SENDER);
         message1.sentMessage("send");
         
         message2 = new Message("+27838884567", TEXT_2);
         message2.setSender(SENDER);
         message2.sentMessage("store");
         
         Message message3 = new Message("+27834484567", TEXT_3);
         message3.setSender(SENDER);
         message3.sentMessage("disregard");
         
         
         Message message4 = new Message("+27831234567", TEXT_4, "0838884567");
         message4.setSender(SENDER);
         message4.sentMessage("send");
         
         Message message5 = new Message("+27838884567", TEXT_5);
         message5.setSender(SENDER);
         message5.sentMessage("store");
     }
     
     @Test
     public void testCheckMessageID() {
         Message message = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight?");
         assertTrue(message.checkMessageID());
     }
     
     @Test
     public void testCheckRecipientCell_valid() {
         assertEquals("Cell phone number successfully captured.",
                 Message.checkRecipientCell("+27718693002"));
     }
     
     @Test
     public void testCheckRecipientCell_invalid() {
         assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.",
                 Message.checkRecipientCell("08575975889"));
     }
     
     @Test
     public void testCheckMessageLength_withinLimit() {
         assertEquals("Message ready to send.",
                 Message.checkMessageLength("Hi Mike, can you join us for dinner tonight?"));
     }
     
     @Test
     public void testCheckMessageLength_tooLong() {
         assertEquals("Message exceeds 250 characters by 10; please reduce the size.",
                 Message.checkMessageLength("x".repeat(260)));
     }
     
     @Test
     public void testCreateMessageHash_testCase1() {
         Message message = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight?", "0012345678");
         assertEquals("00:0:HITONIGHT", message.createMessageHash());
     }
     
     @Test
     public void testCreateMessageHash_loop() {
         String[] messages = {
             "Hi Mike, can you join us for dinner tonight?",
             "Hi Keegan, did you receive the payment?",
             TEXT_1, TEXT_2, TEXT_3, TEXT_4, TEXT_5
                 };
         
         String[] expectedHashes = {
             "00:0:HITONIGHT",
             "00:1:HIPAYMENT",
             "00:2:DIDCAKE",
             "00:3:WHERETIME",
             "00:4:YOHOOOOGATE",
             "00:5:ITTIME",
             "00:6:OKYOU"
         };
         
         for (int i = 0; i < messages.length; i++) {
             String id = "00" + String.format("%08d", i + 1);
             Message message = new Message("+27718693002", messages[i], id);
             assertEquals(expectedHashes[i], message.createMessageHash(),
                     "Hash for message " + i + " is wrong");
         }
     }
     
     @Test
     public void testSentMessage_send() {
         Message message = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight?");
         assertEquals("Message successfully sent.", message.sentMessage("send"));
     }
     
     @Test
     public void testSentMessage_disregard() {
         Message message = new Message("+27718693002", "Hi Keegan, did you receive the payment?");
         assertEquals("Press 0 to delete the message.", message.sentMessage("disregard"));
     }
     
     @Test
     public void testSentMessage_store() {
         Message message = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight?");
         assertEquals("Message successfully stored.", message.sentMessage("store"));
     }
     
     @Test
     public void testSentMessage_invalidChoice() {
         Message message = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight?");
         assertEquals("Invalid choice. Please choose Send, Store or Disregard.",
                 message.sentMessage("maybe"));
     }
     
     @Test
     public void testReturnTotalMessagess() {
         Message first = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight?");
         first.sentMessage("send");
         Message second = new Message("+27718693002", "Hi Keegan, did you receive the payment?");
         second.sentMessage("disregard");
         assertEquals(1, Message.returnTotalMessagess());
     }
     
     @Test
     public void testMessageIDIsTenDigits() {
         for (int i = 0; i < 50; i++) {
             Message message = new Message("+27718693002", "Hi there");
             assertEquals(10, message.getMessageID().length());
             assertTrue(message.getMessageID().matches("\\d{10}"));
         }
     }
     
     @Test
     public void testSentMessagesArrayCorrectlyPopulated() {
         populateTestData();
         assertArrayEquals(new String[]{TEXT_1, TEXT_4}, Message.getSentMessageTexts());
     }
                 
    @Test
    public void testDisregardedMessagesArrayCorrectlyPopulated() {
         populateTestData();
         assertArrayEquals(new String[]{TEXT_3}, Message.getDisregardedMessageTexts());
     }
                 
    @Test
    public void testStoredMessagesArrayCorrectlyPopulated() {
         populateTestData();
         assertArrayEquals(new String[]{TEXT_2, TEXT_5}, Message.getStoredMessageTexts());
     }
                 
    @Test
    public void testMessageHashAndIDArraysPopulated() {
         populateTestData();
         assertEquals(5, Message.getMessageHashList().length);
         assertEquals(5, Message.getMessageIDList().length);
         assertEquals("0838884567", Message.getMessageIDList()[3]);
         
     }
                 
    @Test
    public void testDisplayLongestMessage() {
         populateTestData();
         assertEquals(TEXT_2, Message.findLongestStoredMessage());
         
     }
                 
    @Test
    public void testSearchForMessageID() {
         populateTestData();
         Message found = Message.searchByMessageID("0838884567");
         assertNotNull(found);
         assertEquals("It is dinner time!", found.getMessageText());
     }
                 
    @Test
    public void testSearchForMessageID_notFound() {
         populateTestData();
         assertNull(Message.searchByMessageID("9999999999"));
         
     }
                 
    @Test
    public void testSearchMessagesForRecipient() {
         populateTestData();
         assertArrayEquals(new String[]{TEXT_2, TEXT_5}, Message.searchByRecipient("+27838884567"));
         
     }
                 
    @Test
    public void testSearchMessagesForRecipient_noMatches() {
         populateTestData();
         assertEquals(0, Message.searchByRecipient("+27000000000").length);
     }
                 
    @Test
    public void testDeleteMessageUsingHash() {
         populateTestData();
         String result = Message.deleteMessageByHash(message2.getMessageHash());
         assertEquals("Message: \"Where are you? You are late! I have asked you to be on time\" "
                 + "successfully deleted.", result);
     
                 
        assertArrayEquals(new String[]{TEXT_5}, Message.getStoredMessageTexts());
        assertEquals(4, Message.getMessageHashList().length);
        assertEquals(4, Message.getMessageIDList().length);
     }
     
     @Test
     public void testDeleteMessageUsingHash_notFound() {
         populateTestData();
         assertEquals("Message hash not found.", Message.deleteMessageByHash("00:0:NOTHERE"));
     }
     
     @Test
     public void testDisplayReport_sentMessages() {
         populateTestData();
         String report = Message.printMessages();
         assertTrue(report.contains(TEXT_1));
         assertTrue(report.contains("+27834557896"));
         assertTrue(report.contains(TEXT_4));
         assertTrue(report.contains("Message Hash: "));
         assertFalse(report.contains(TEXT_2));
     }
     
     @Test
     public void testStoredReportAndSenderRecipientList() {
         populateTestData();
         String report = Message.reportStoredMessages();
         assertTrue(report.contains(TEXT_2));
         assertTrue(report.contains(TEXT_5));
         assertTrue(report.contains(SENDER));
         assertEquals("Sender: " + SENDER + " | Recipient: +27838884567\n"
                 + "Sender: " + SENDER + " | Recipient: +27838884567",
                 Message.displaySendersAndRecipients());
     }
     
     @Test
     public void testStoredMessagesAreReadBackFromJson() {
         populateTestData();
         Message.clearInMemoryDataForTesting();
         assertEquals(0, Message.getStoredMessageTexts().length);
         Message.loadStoredMessagesFromJson();
         assertArrayEquals(new String[]{TEXT_2, TEXT_5}, Message.getStoredMessageTexts());
         assertEquals(2, Message.getMessageHashList().length);
     }
     
     @Test
     public void testJsonHandlesQuotesAndBackslashes() {
         String tricky = "She said \"hi\" and left C:\\temp {ok}";
         Message message = new Message("+27838884567", tricky);
         message.setSender(SENDER);
         message.sentMessage("store");
         
         Message.clearInMemoryDataForTesting();
         Message.loadStoredMessagesFromJson();
         assertArrayEquals(new String[]{tricky}, Message.getStoredMessageTexts());
     }
     
     @Test
     public void testDeleteUpdatesJsonFile() {
         populateTestData();
         Message.deleteMessageByHash(message2.getMessageHash());
         Message.clearInMemoryDataForTesting();
         Message.loadStoredMessagesFromJson();
         assertArrayEquals(new String[]{TEXT_5}, Message.getStoredMessageTexts());
     }
}
