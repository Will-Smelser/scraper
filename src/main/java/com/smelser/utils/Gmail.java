package com.smelser.utils;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Gmail {
	static final Logger LOG = LoggerFactory.getLogger(Gmail.class);
	
	private static final String username = "willsmelser@gmail.com";
	private static final String password = "redlokcdztyfiaxj";
	
	private static final String toEmail = "willsmelser@gmail.com";
	private static final String fromEmail = "willsmelser@gmail.com";
	private static final String title = "Lenovo Item(s) Added";
	
	public static void send(String msg){
 
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
 
		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });
 
		try {
 
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromEmail));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(toEmail));
			message.setSubject(title);
			message.setText(msg);
 
			Transport.send(message);
 
			LOG.info("Sent Email");
 
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	
	}
	
	public static void sendAttached(String cookies){

      Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

      // Get the Session object.
      Session session = Session.getInstance(props,
         new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
               return new PasswordAuthentication(username, password);
            }
         });

      try {
         // Create a default MimeMessage object.
         Message message = new MimeMessage(session);

         // Set From: header field of the header.
         message.setFrom(new InternetAddress(fromEmail));

         // Set To: header field of the header.
         message.setRecipients(Message.RecipientType.TO,
            InternetAddress.parse(toEmail));

         // Set Subject: header field
         message.setSubject(title);

         // Create the message part
         BodyPart messageBodyPart = new MimeBodyPart();

         // Now set the actual message
         messageBodyPart.setText("Cookie File Attached");

         // Create a multipar message
         Multipart multipart = new MimeMultipart();

         // Set text message part
         multipart.addBodyPart(messageBodyPart);
         
         // Part two is attachment
         messageBodyPart = new MimeBodyPart();
         String filename = "cookies.txt";
         DataSource source = null;
		try {
			source = new ByteArrayDataSource(cookies.getBytes("UTF-8"), "application/octet-stream");
		} catch (UnsupportedEncodingException e) {
			LOG.warn("Failed to attache file.  Sending in message body",e);
		}
		
		if(source == null){
			send("Failed to attache file.\n\n"+cookies);
			return;
		}
		
         messageBodyPart.setDataHandler(new DataHandler(source));
         messageBodyPart.setFileName(filename);
         multipart.addBodyPart(messageBodyPart);

         // Send the complete message parts
         message.setContent(multipart);

         // Send message
         Transport.send(message);

         LOG.info("Sent message successfully....");
  
      } catch (MessagingException e) {
         throw new RuntimeException(e);
      }
   }
}
