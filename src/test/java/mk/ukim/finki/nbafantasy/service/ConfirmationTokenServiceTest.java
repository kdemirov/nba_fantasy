package mk.ukim.finki.nbafantasy.service;

import mk.ukim.finki.nbafantasy.AbstractTestClass;
import mk.ukim.finki.nbafantasy.model.ConfirmationToken;
import mk.ukim.finki.nbafantasy.model.exceptions.ConfirmationTokenDoesNotExists;
import mk.ukim.finki.nbafantasy.repository.jpa.ConfirmationTokenRepository;
import mk.ukim.finki.nbafantasy.service.impl.ConfirmationTokenServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.when;

/**
 * Tests for ConfirmationTokenService.
 */
class ConfirmationTokenServiceTest extends AbstractTestClass {

    @InjectMocks
    ConfirmationTokenServiceImpl confirmationTokenService;

    @Mock
    ConfirmationTokenRepository confirmationTokenRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void should_save_confirmation_token() {
        //when
        ConfirmationToken confirmationToken = confirmationTokenService.save(USER);

        //when
        Assertions.assertEquals(USER.getUsername(), confirmationToken.getUser().getUsername());
        Assertions.assertNotNull(confirmationToken.getConfirmationToken());
    }

    @Test
    void should_find_confirmation_token_by_user() {
        //given
        ConfirmationToken expectedConfirmationToken = new ConfirmationToken(USER);
        when(confirmationTokenRepository.findByUser(USER)).thenReturn(Optional.of(expectedConfirmationToken));

        //when
        ConfirmationToken confirmationToken = confirmationTokenService.findByUser(USER);

        //then
        Assertions.assertEquals(expectedConfirmationToken.getConfirmationToken(), confirmationToken.getConfirmationToken());
        Assertions.assertEquals(USER.getUsername(), confirmationToken.getUser().getUsername());
    }

    @Test
    void should__not_find_confirmation_token_by_user() {
        //given
        when(confirmationTokenRepository.findByUser(USER)).thenReturn(Optional.empty());

        //when
        Assertions.assertThrows(ConfirmationTokenDoesNotExists.class,
                () -> confirmationTokenService.findByUser(USER));
    }
}