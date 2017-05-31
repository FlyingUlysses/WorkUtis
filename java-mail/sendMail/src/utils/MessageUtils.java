package utils;


import java.net.URL;

import javax.mail.internet.MimeUtility;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
public class MessageUtils {
 public static void main(String[] args) {
  send();
 }
 // QQ邮箱对应的pop3和smtp服务器地址为：pop3：pop.qq.com；
 // smtp：smtp.qq.com，请参考
 // lu@yahoo.com.cn
 // 449222222@qq.com
 public static void send() {
  
  try { 
   MultiPartEmail  email = new MultiPartEmail ();
   email.setTLS(true);
   email.setHostName("smtp.163.com"); // 这里是发送服务器的名字
   email.setAuthentication("wangyongtest123@163.com", "wy940707");
   
      //产生一个附件
         EmailAttachment attachment = new EmailAttachment();
       //  attachment.setPath("‪D:\\test.jpg");
     //也可以以网络的方式
         attachment.setURL(new URL("http://www.apache.org/images/asf_logo_wide.gif"));
         attachment.setDisposition(EmailAttachment.ATTACHMENT);
      attachment.setName(MimeUtility.encodeText("test.jpg"));
   

   email.addTo("lushuaiyin@yahoo.com.cn", "rec"); // 接收方
//第2个参数是昵称，可以不要，收件人在信箱内的这个地方显示
//收件人：rec <lushuaiyin@yahoo.com.cn>

   email.setFrom("2522737133@qq.com"); // 发送方
   email.setSubject("Java发送邮件测试"); // 标题
   email.setCharset("GB2312"); // 设置Charset
   email.setMsg("这是一封Java程序发出的测试邮件,带附件。"); // 内容
   
   email.attach(attachment);//添加附件
   
   email.send();//发送
   System.out.println("邮件发送成功");
  } catch (EmailException e) {
   e.printStackTrace();
  }catch (Exception e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  }
 }
}