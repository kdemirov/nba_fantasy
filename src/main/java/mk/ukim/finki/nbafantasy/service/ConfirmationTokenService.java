package mk.ukim.finki.nbafantasy.service;

import mk.ukim.finki.nbafantasy.model.ConfirmationToken;
import mk.ukim.finki.nbafantasy.model.User;

public interface ConfirmationTokenService {
    ConfirmationToken save(User user);
    ConfirmationToken findByUser(User user);
}
