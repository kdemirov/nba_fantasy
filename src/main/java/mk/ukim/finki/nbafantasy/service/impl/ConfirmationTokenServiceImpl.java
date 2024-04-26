package mk.ukim.finki.nbafantasy.service.impl;

import mk.ukim.finki.nbafantasy.model.ConfirmationToken;
import mk.ukim.finki.nbafantasy.model.User;
import mk.ukim.finki.nbafantasy.model.exceptions.ConfirmationTokenDoesNotExists;
import mk.ukim.finki.nbafantasy.repository.jpa.ConfirmationTokenRepository;
import mk.ukim.finki.nbafantasy.service.ConfirmationTokenService;
import org.springframework.stereotype.Service;

@Service
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;


    public ConfirmationTokenServiceImpl(ConfirmationTokenRepository confirmationTokenRepository) {
        this.confirmationTokenRepository = confirmationTokenRepository;

    }

    @Override
    public ConfirmationToken save(User user) {
        ConfirmationToken confirmationToken = new ConfirmationToken(user);
        this.confirmationTokenRepository.save(confirmationToken);
        return confirmationToken;
    }

    @Override
    public ConfirmationToken findByUser(User user) {
        return this.confirmationTokenRepository.findByUser(user)
                .orElseThrow(() -> new ConfirmationTokenDoesNotExists(user.getUsername()));
    }
}
