package utils;


import java.io.File;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.HtmlEmail;

public class MyUtils {
	public static void send(){
		try {
			HtmlEmail email = new HtmlEmail();
			email.addTo("to@qq.com");
			email.setHostName(ConstantItems.EMAIL_HOST);
			email.setAuthentication("from@163.com",
					"fromPWD");
			email.setSSLOnConnect(true);
			email.setFrom("from@163.com", "desc");
			email.setSubject("title");
			email.setCharset("UTF-8");
			email.setMsg("content");
			
	        EmailAttachment attachment = new EmailAttachment();   
	        attachment.setPath("D:/test.txt");    
	        attachment.setDisposition(EmailAttachment.ATTACHMENT);    
	        attachment.setDescription("测试");
	        attachment.setName(MimeUtility.encodeText("测试.txt")); 
	        email.attach(attachment);
	        
			email.send();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void sendNativeEmail(){
	        try {  
	  
	            String smtpFromMail = "wangyongtest123@163.com";  
	            String pwd = "wy940707"; 
	            int port = 25; 
	            String host = "smtp.163.com";   
	  
	            Properties props = new Properties();  
	            props.put("mail.smtp.host", host);  
	            props.put("mail.smtp.auth", "true");  
	            Session session = Session.getDefaultInstance(props);  
	            session.setDebug(false);  
	  
	            MimeMessage message = new MimeMessage(session);  
	            try {  
	                message.setFrom(new InternetAddress(smtpFromMail, "邮件测试"));  
	                message.addRecipient(Message.RecipientType.TO,  
	                        new InternetAddress("2522737133@qq.com"));  
	                message.setSubject("哈哈");  
	                message.addHeader("charset", "UTF-8");  
	                  
	                /*添加正文内容*/  
	                Multipart multipart = new MimeMultipart();  
	                BodyPart contentPart = new MimeBodyPart();  
	                contentPart.setText("收到了吗");  
	  
	                contentPart.setHeader("Content-Type", "text/html; charset=GBK");  
	                multipart.addBodyPart(contentPart);  
	                  
	                    File usFile = new File("D:/test.txt");  
	                    MimeBodyPart fileBody = new MimeBodyPart();  
	                    DataSource source = new FileDataSource("D:/test.txt");  
	                    fileBody.setDataHandler(new DataHandler(source));  
	                    sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();  
	                    fileBody.setFileName("=?GBK?B?"  
	                            + enc.encode(usFile.getName().getBytes()) + "?=");  
	                    multipart.addBodyPart(fileBody); 
	                    
	                message.setContent(multipart);  
	                message.setSentDate(new Date());  
	                message.saveChanges();  
	                Transport transport = session.getTransport("smtp");  
	  
	                transport.connect(host, port, smtpFromMail, pwd);  
	                transport.sendMessage(message, message.getAllRecipients());  
	                transport.close();  
	            } catch (Exception e) {  
	            }  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	}
	
	public static void main(String[] args) {
		MyUtils.send();
	}
}
