package mk.ukim.finki.nbafantasy.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmailSenderService {

    private final  JavaMailSender javaMailSender;

    public void sendEmail(SimpleMailMessage email){
        javaMailSender.send(email);
    }
}
