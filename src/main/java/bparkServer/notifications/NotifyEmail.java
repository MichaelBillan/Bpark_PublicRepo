package bparkServer.notifications;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.util.Properties;

/**
 * Real implementation for sending email notifications to users using SMTP.
 */
public class NotifyEmail implements NotifyDelays {

    // Gmail credentials used to send emails
    private static final String USERNAME = "bparkproject7@gmail.com";
    private static final String PASSWORD = "yxaf mrgl zpyd ixmb";
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final int SMTP_PORT = 587;

    /**
     * Sends an email notification to the specified recipient.
     *
     * @param recipientAddress the recipient's email address
     * @param subject the subject of the email
     * @param message the body/content of the email
     * @return true if the email was sent successfully, false otherwise
     */
    @Override
    public boolean sendNotification(String recipientAddress, String subject, String message) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });

        try {
            Message mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(USERNAME));
            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientAddress));
            mimeMessage.setSubject(subject);
            mimeMessage.setText(message);

            Transport.send(mimeMessage);
            System.out.println("Email sent to " + recipientAddress);
            return true;
        } catch (MessagingException e) {
            System.err.println("Failed to send email: " + e.getMessage());
            return false;
        }
    }
}
