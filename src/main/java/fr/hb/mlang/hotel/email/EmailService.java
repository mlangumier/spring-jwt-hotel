package fr.hb.mlang.hotel.email;

import fr.hb.mlang.hotel.user.domain.User;

public interface EmailService {

  /**
   * Formats an email using the {@link EmailDetails}'s data and sends it.
   *
   * @param email Entity containing the information necessary for the email to be sent
   */
  void sendEmail(EmailDetails email);

  /**
   * Prepares and then sends the email the {@link User} will use to verify their account.
   *
   * @param user Recipient of the email
   * @param jwt  Verification token used to display the activation link for the user to verify their
   *             account
   */
  void sendVerificationEmail(User user, String jwt);

  /**
   * Prepares the verification email's content by getting the template and injecting variables such
   * as the <code>JWT</code> in the verification link.
   *
   * @param jwt token used in the verification link
   * @return a string that contains the verification email in HTML format that the user will receive
   */
  String prepareVerificationEmailContent(String jwt);

  /**
   * Prepares and sends the email the {@link User} will use to reset their password.
   *
   * @param user Recipient of the email
   * @param jwt  Verification token used to display the reset-password link for the user to change
   *             their password
   */
  void sendResetPasswordEmail(User user, String jwt);

  /**
   * Prepares the reset-password email's content by getting the template and injecting variables
   * such as the <code>JWT</code> for the reset-password link.
   *
   * @param jwt token used in the reset-password link
   * @return a string that contains the email to reset the user's password in HTML format
   */
  String prepareResetPasswordEmailContent(String jwt);

  //TODO: String prepareSimpleEmail(String uri, String jwt)
}
