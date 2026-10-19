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
    @BeforeEach
        public void setUp() {
            Message.resetCountersForTesting();
        }
        @Test
                public void testCheckMessageID() {
                    Message message = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight?");
                    assertTrue(message.checkMessageID());
                }
                @Test
                        public void testCheckRecipientCell_valid() {
                            Message message = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight?");
                            assertEquals("Cell phone number successfully captured.",
                                    message.checkRecipientCell("+27718693002"));
                            }
                        @Test
                        public void testCheckRecipientCell_invalid() {
                            Message message = new Message("08575975889", "Hi Keegan, did you receive the payment?");
                            assertEquals("Cell phone number is incorrectly formatted or does not contain an "
                                    + "international code. Please correct the number and try again.",
                                    message.checkRecipientCell("08575975889"));
                            }
                        @Test
                                public void testCheckMessageLength_withinLimit() {
                                    Message message = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight?");
                                    assertEquals("Message ready to send.", message.checkMessageLength(message.getMessageText()));
                                    }
                                @Test
                                        public void testCheckMessageLength_tooLong() {
                                            Message message = new Message("+27718693002", "x".repeat(260));
                                            assertEquals("Message exceeds 250 characters by 10; please reduce the size.",
                                                    message.checkMessageLength(message.getMessageText()));
                                        }
                                        @Test
                                                public void testCreateMessageHash_testCase1() {
                                                    // Fixed message ID so the hash prefix is deterministic: "00"
                                                    Message message = new Message("+27718693002",
                                                            "Hi Mike, can you join us for dinner tonight?", "0012345678");
                                                    String hash = message.createMessageHash();
                                                    assertEquals("00:0:HITONIGHT", hash);
                                                }
                                                @Test
                                                        public void testSentMessage_send() {
                                                            Message message = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight?");
                                                            assertEquals("Message successfully sent.", message.sentMessage("send"));
                                                        }
                                                        @Test
                                                        public void testSentMessage_disregard() {
                                                            Message message = new Message("08575975889", "Hi Keegan, did you receive the payment?");
                                                            assertEquals("Press 0 to delete the message.", message.sentMessage("disregard"));
                                                        }
                                                        @Test
                                                                public void testSentMessage_store() {
                                                                    Message message = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight?");
                                                                    assertEquals("Message successfully stored.", message.sentMessage("store"));
                                                                }
                                                                @Test
                                                                        public void testReturnTotalMessagess() {
                                                                            Message message1 = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight?");
                                                                            message1.sentMessage("send");
                                                                            Message message2 = new Message("08575975889", "Hi Keegan, did you receive the payment?");
                                                                            message2.sentMessage("disregard"); // should NOT count toward total sent
                                                                            assertEquals(1, Message.returnTotalMessagess());
                                                                        }
}
