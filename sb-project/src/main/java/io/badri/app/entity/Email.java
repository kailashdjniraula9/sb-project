package io.badri.app.entity;

import java.io.IOException;
import java.util.Properties;

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

import org.springframework.stereotype.Component;

@Component
public class Email {
	public void sendEmail() {
		String to = "badri@ncit.edu.np"; // to address. It can be any like gmail, yahoo etc.
		String from = "kailashdjniraula9@gmail.com"; // from address. As this is using Gmail SMTP your from address
														// should be
		String password = "Password#123"; // password for from gmail address that you have used in above line.

		Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", "465");
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.socketFactory.port", "465");
		prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

		Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(from, password);
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			message.setSubject("Test Mail From Gmail Accocunt");
			message.setText("Mail From Java Code Using Gmail...");
			
			Multipart emailContent = new MimeMultipart();

			// Text body part
			MimeBodyPart textBodyPart = new MimeBodyPart();
			textBodyPart.setText("Hey Download this file..");

			// Attachment body part.
			MimeBodyPart attachment = new MimeBodyPart();
			attachment.attachFile("D:\\Wallpapers\\383271.jpg");

			// Attach body parts
			emailContent.addBodyPart(textBodyPart);
			emailContent.addBodyPart(attachment);

			// Attach multipart to message
			message.setContent(emailContent);

			Transport.send(message);

			System.out.println("Mail Sent...");

		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
