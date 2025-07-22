package fr.hb.mlang.hotel.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

  private final JavaMailSender mailSender;

  @Value("${spring.mail.username}")
  private String sender;

  public EmailServiceImpl(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  @Override
  public void sendEmail(EmailDetails email) {
    try {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

      helper.setFrom(sender);
      helper.setTo(email.getRecipient());
      helper.setSubject(email.getSubject());
      helper.setText(email.getMessageBody());

      mailSender.send(message);
    } catch (MessagingException e) {
      throw new RuntimeException("Email couldn't be sent: " + e.getMessage());
    }
  }

  @Override
  public void sendMailWithAttachment(EmailDetails email) {
    // Setup later if needed
  }
}
