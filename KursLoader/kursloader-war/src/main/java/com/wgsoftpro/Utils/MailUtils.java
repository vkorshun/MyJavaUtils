package com.wgsoftpro.Utils;

/**
 * Created by vkorshun on 07.01.2016.
 */


import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Java Program to send text mail using default SMTP server and without authentication.
 * You need mail.jar, smtp.jar and activation.jar to run this program.
 *
 * @author Javin Paul
 */

public class MailUtils{
  public static void sendMail(String from, String to, String host, String subject, String textMessage ){

    //String to = "receive@abc.om";         // sender email
    //String from = "sender@abc.com";       // receiver email
    //String host = "127.0.0.1";            // mail server host

    Properties properties = System.getProperties();
    properties.setProperty("mail.smtp.host", host);

    Session session = Session.getDefaultInstance(properties); // default session

    try {
      MimeMessage message = new MimeMessage(session); // email message

      message.setFrom(new InternetAddress(from)); // setting header fields

      message.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));

      message.setSubject(subject); // subject line

      // actual mail body
      message.setText(textMessage);

      // Send message
      Transport.send(message);
      System.out.println("Email Sent successfully....");
    } catch (MessagingException mex){ mex.printStackTrace(); }

  }

  public static void  sendGmail(String host, String port, String from, String to,final String username, final String password, String subject, String text){
    Properties prop=new Properties();
    prop.put("mail.smtp.auth", "true");
    prop.put("mail.smtp.host", host); //"smtp.gmail.com"
    prop.put("mail.smtp.port", port);//"587"
    prop.put("mail.smtp.starttls.enable", "true");

    Session session = Session.getDefaultInstance(prop,
        new javax.mail.Authenticator() {
          protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
          }
        });
    try {
      String body="Dear Renish Khunt Welcome";
      String htmlBody = "<strong>"+text+"</strong>";
      String textBody = text;
      MimeMessage message = new MimeMessage(session);
      message.setFrom(new InternetAddress(from));
      message.setRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
      message.setSubject(subject);
      MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
      mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
      mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
      mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
      mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
      mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
      CommandMap.setDefaultCommandMap(mc);
      message.setText(htmlBody);
      message.setContent(textBody, "text/html");
      Transport.send(message);

      System.out.println("Done");

    } catch (MessagingException e) {
      e.printStackTrace();
    }

  }

}

