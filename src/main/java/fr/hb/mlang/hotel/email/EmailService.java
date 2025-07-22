package fr.hb.mlang.hotel.email;

public interface EmailService {

  void sendEmail(EmailDetails email);

  void sendMailWithAttachment(EmailDetails email);
}
