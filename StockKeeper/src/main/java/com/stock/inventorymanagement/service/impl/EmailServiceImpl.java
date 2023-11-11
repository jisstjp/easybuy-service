package com.stock.inventorymanagement.service.impl;

import com.stock.inventorymanagement.service.IEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements IEmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendEmailWithAttachment(String to, String subject, String htmlBody, String attachmentFilename, byte[] attachmentData) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setFrom("your-email@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true); // Set to 'true' to enable HTML content

        ByteArrayResource resource = new ByteArrayResource(attachmentData);
        helper.addAttachment(attachmentFilename, resource);

        mailSender.send(mimeMessage);
    }
}
