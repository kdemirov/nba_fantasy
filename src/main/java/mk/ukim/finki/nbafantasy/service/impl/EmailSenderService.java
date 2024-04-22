package mk.ukim.finki.nbafantasy.service.impl;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.nbafantasy.model.ConfirmationToken;
import mk.ukim.finki.nbafantasy.model.User;
import mk.ukim.finki.nbafantasy.service.ConfirmationTokenService;
import org.springframework.beans.factory.annotation.Value;
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
    private final ConfirmationTokenService confirmationTokenService;
    @Value("${spring.mail.username}")
    private String email;

    /**
     * Sends an email with confirmation code token for given user to it's provided email
     * in order to verify his account.
     *
     * @param user given user
     */
    public void sendEmail(User user) {
        ConfirmationToken ct = this.confirmationTokenService.findByUser(user);
        ct = ct != null ? ct : this.confirmationTokenService.save(user);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Complete registration!");
        mailMessage.setFrom(email);
        mailMessage.setText("Here is the code to confirm your account code:" + ct.getConfirmationToken());
        javaMailSender.send(mailMessage);
    }
}
