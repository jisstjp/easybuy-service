package com.stock.inventorymanagement.service;

import javax.mail.MessagingException;

public interface IEmailService {

    void sendEmailWithAttachment(String to, String subject, String text, String attachmentName, byte[] attachment) throws MessagingException;

}
