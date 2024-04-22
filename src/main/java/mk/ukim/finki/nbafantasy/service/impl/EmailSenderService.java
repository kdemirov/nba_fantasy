package mk.ukim.finki.nbafantasy.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Service for sending an email with confirmation code.
 */
@RequiredArgsConstructor
@Service
public class EmailSenderService {

    private final JavaMailSender javaMailSender;

    /**
     * Sends email message with given simple  mail message
     *
     * @param email given simple mail message.
     */
    public void sendEmail(SimpleMailMessage email) {
        javaMailSender.send(email);
    }
}
