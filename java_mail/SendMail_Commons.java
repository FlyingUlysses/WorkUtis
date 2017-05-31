package utils;

import java.io.File;

import javax.mail.internet.MimeUtility;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.HtmlEmail;


/**
 * 使用commonsEmail发送附件邮件，可附带多个附件
 * @author ulysses
 *
 */
public class SendMail_Commons {
	
	public static void sendEamil() {
			try {
				HtmlEmail email = new HtmlEmail();
				email.addTo("to@qq.com");
				email.setHostName("");
				email.setAuthentication("from@163.com",
					"formPWD");
				email.setSSLOnConnect(false);
				email.setFrom("from@163.com", "desc");
				email.setSubject("title");
				email.setCharset("UTF-8");
				email.setMsg("content");
				
				//以文件夹添加附件
				File file = new File("D:\\temp");
				String[] list = file.list();
				String path ="";
				for (String name : list) {
					 path="D:\\temp"+"\\"+name;
					 EmailAttachment attachment = new EmailAttachment();
					 attachment.setPath(path); 
					 attachment.setDescription(EmailAttachment.ATTACHMENT);
					 attachment.setDescription("测试");
					 attachment.setName(MimeUtility.encodeText(name)); 
					 email.attach(attachment);
				}
		        
				
				email.send();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	//main方法测试
	public static void main(String[] args) {
		SendMail_Commons.sendEamil();
	}
}
