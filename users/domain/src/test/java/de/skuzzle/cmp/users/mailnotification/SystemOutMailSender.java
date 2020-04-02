package de.skuzzle.cmp.users.mailnotification;

import java.io.InputStream;
import java.util.Arrays;

import javax.mail.internet.MimeMessage;

import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;

@Primary
@TestComponent
public class SystemOutMailSender implements JavaMailSender {

    @Override
    public void send(SimpleMailMessage simpleMessage) {
        System.out.println("To: " + Arrays.toString(simpleMessage.getTo()));
        System.out.println("Subject: " + simpleMessage.getSubject());
        System.out.println(simpleMessage.getText());
    }

    @Override
    public void send(SimpleMailMessage... simpleMessages) {
        Arrays.asList(simpleMessages).forEach(this::send);
    }

    @Override
    public MimeMessage createMimeMessage() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MimeMessage createMimeMessage(InputStream contentStream) throws MailException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void send(MimeMessage mimeMessage) throws MailException {
        // TODO Auto-generated method stub

    }

    @Override
    public void send(MimeMessage... mimeMessages) throws MailException {
        // TODO Auto-generated method stub

    }

    @Override
    public void send(MimeMessagePreparator mimeMessagePreparator) throws MailException {
        // TODO Auto-generated method stub

    }

    @Override
    public void send(MimeMessagePreparator... mimeMessagePreparators) throws MailException {
        // TODO Auto-generated method stub

    }

}
