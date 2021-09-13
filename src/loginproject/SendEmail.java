package loginproject;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmail{
	public void sendEmailToUser(String userEmailAddr, String subject, String text) {
		String host = "smtp.naver.com";
		String user = "email";
		String password = "emailPw";

		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", 587);
		props.put("mail.smtp.auth", true);

		Session session = Session.getDefaultInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, password);
			}
		});

		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(user));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(userEmailAddr));
			//¸ÞÀÏÁ¦¸ñ
			message.setSubject(subject);
			//¸ÞÀÏ³»¿ë
			message.setText(text);
			//send the message
			Transport.send(message);
			System.out.println("¼º°ø");

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
