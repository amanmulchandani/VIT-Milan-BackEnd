package com.vit.community.springapplication.service;

import com.vit.community.springapplication.exceptions.SpringCommunityException;
import com.vit.community.springapplication.model.NotificationEmail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/*
* The MailService class uses the MimeMessageHelper class along with
* an instance of MimeMessagePreparator class to set the email metadata
* like sender, receiver, subject, and body.
* The sender is a fake email address since we are using a fake SMTP server.
* The JavaMailSender class is used to send the email.
*
* The Lombok library generates the all arguments constructor, and the fields
* are automatically autowired (initialised) by Spring at runtime.
*
* The Slf4j (Simple Logging Facade for Java) annotation is used for logging
* the data.
* */

@Service
@AllArgsConstructor
@Slf4j
class MailService {

    private final JavaMailSender mailSender;
//    private final MailContentBuilder mailContentBuilder;

    /*
    * When the user clicks on signup button, an activation email is sent to the user.
    * Since this is a heavy process and takes time, we use the @Async annotation to
    * process this request asynchronously (process it on another thread)
    * and thereby reduce the time taken to send the email.
    *
    * For large-scale use, use Message Queues like RabbitMQ or ActiveMQ
    * since they provide reliability
    * */

    @Async
    void sendMail(NotificationEmail notificationEmail) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("springvit@email.com");
            messageHelper.setTo(notificationEmail.getRecipient());
            messageHelper.setSubject(notificationEmail.getSubject());
            messageHelper.setText(notificationEmail.getBody());
        };
        try {
            mailSender.send(messagePreparator);
            log.info("Activation email sent!!");
        } catch (MailException e) {
            log.error("Exception occurred when sending mail", e);
            throw new SpringCommunityException("Exception occurred when sending mail to "
                    + notificationEmail.getRecipient(), e);
        }
    }

}