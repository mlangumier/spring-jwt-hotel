package fr.hb.mlang.hotel.email;

public interface EmailService {

  void sendEmail(EmailDetails email);

  String getVerificationMailContent();
}
