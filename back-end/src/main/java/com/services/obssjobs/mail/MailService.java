package com.services.obssjobs.mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailService {

    private JavaMailSender javaMailSender;

    @Autowired
    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendStatusChangeEmail(String to, String jobTitle, String status) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(to);

        mail.setSubject("Your application status is changed");
        mail.setText("Your application status for " + jobTitle + " role" + " is now at " + status + " stage");

        javaMailSender.send(mail);
    }
}
