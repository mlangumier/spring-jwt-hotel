package fr.hb.mlang.hotel.auth.email;

public interface EmailService {

  void sendEmail(EmailDetails email);

  void sendMailWithAttachment(EmailDetails email);
}
