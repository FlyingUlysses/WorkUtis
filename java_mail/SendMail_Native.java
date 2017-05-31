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

/**
 * 使用原生javaMail发送邮件
 * @author ulysses
 *
 */
public class SendMail_Native {
	
	public static void sendEmail(){
	        try {  
	            String smtpFromMail = "from@163.com";  
	            String pwd = "PWD"; 
	            int port = 25; 
	            String host = "smtp.163.com";   
	            Properties props = new Properties();  
	            props.put("mail.smtp.host", host);  
	            props.put("mail.smtp.auth", "true");  
	            Session session = Session.getDefaultInstance(props);  
	            session.setDebug(false);  
	            MimeMessage message = new MimeMessage(session);  
	            message.setFrom(new InternetAddress(smtpFromMail, "邮件测试"));  
	            message.addRecipient(Message.RecipientType.TO,  new InternetAddress("to@qq.com"));  
	            message.setSubject("哈哈");  
	            message.addHeader("charset", "UTF-8");  
	              
	            /*添加正文内容*/  
	            Multipart multipart = new MimeMultipart();  
	            BodyPart contentPart = new MimeBodyPart();  
	            contentPart.setText("收到了吗");  
	            contentPart.setHeader("Content-Type", "text/html; charset=GBK");  
	           	multipart.addBodyPart(contentPart);  
	              
	           	//添加附件
		        File usFile = new File("D:/test.txt");  
		        MimeBodyPart fileBody = new MimeBodyPart();  
		        DataSource source = new FileDataSource("D:/test.txt");  
		        fileBody.setDataHandler(new DataHandler(source));  
		        sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();  
		        fileBody.setFileName("=?GBK?B?"  + enc.encode(usFile.getName().getBytes()) + "?=");  
		        multipart.addBodyPart(fileBody); 
	            message.setContent(multipart);  
	            
	            message.setSentDate(new Date());  
	            message.saveChanges();  
	            Transport transport = session.getTransport("smtp");  
	            transport.connect(host, port, smtpFromMail, pwd);  
	            transport.sendMessage(message, message.getAllRecipients());  
	            transport.close();  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	}
	
	//main方法测试
	public static void main(String[] args) {
		SendMail_Native.sendEmail();
	}
}
