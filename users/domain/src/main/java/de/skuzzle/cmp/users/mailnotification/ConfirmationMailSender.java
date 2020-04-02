package de.skuzzle.cmp.users.mailnotification;

import org.springframework.context.event.EventListener;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import de.skuzzle.cmp.users.registration.ResetPasswordRequestEvent;
import de.skuzzle.cmp.users.registration.UserRegisteredEvent;

@Component
public class ConfirmationMailSender {

    private final MailSender mailSender;

    ConfirmationMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    @EventListener
    public void sendRegistrationConfirmationMail(UserRegisteredEvent e) {
        final SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("CountMyPizza");
        mailMessage.setTo(e.email());
        mailMessage.setSubject("Your countmy.pizza registration");
        mailMessage.setText(String.format("Hello %s,\r\n" +
                "\r\n" +
                "we have received your registration linked to this E-Mail address. Please visit the link below to\r\n" +
                "confirm the registration.\r\n" +
                "\r\n" +
                "https://auth.countmy.pizza/registration/confirm/%s\r\n" +
                "\r\n" +
                "If you haven't registered yourself we kindly ask you to ignore this mail.\r\n" +
                "\r\n" +
                "Sincerely\r\n" +
                "The CMP team",
                e.name(), e.confirmationToken()));
        mailSender.send(mailMessage);
    }

    @EventListener
    public void sendPasswordResetConfirmationMail(ResetPasswordRequestEvent e) {
        final SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("CountMyPizza");
        mailMessage.setTo(e.email());
        mailMessage.setSubject("countmy.pizza password reset request");
        mailMessage.setText(String.format("Hello %s,\r\n" +
                "\r\n" +
                "we have received the request to reset your password. Please visit the link below to\r\n" +
                "reset the password and chose a new onw.\r\n" +
                "\r\n" +
                "https://auth.countmy.pizza/resetpw/confirm/%s\r\n" +
                "\r\n" +
                "If you haven't requested the reset yourself we kindly ask you to ignore this mail.\r\n" +
                "\r\n" +
                "Sincerely\r\n" +
                "The CMP team",
                e.name(), e.confirmationToken()));
    }
}
