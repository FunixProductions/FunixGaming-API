package fr.funixgaming.api.core.external.google.captcha.services;

import fr.funixgaming.api.core.exceptions.ApiBadRequestException;
import fr.funixgaming.api.core.external.google.captcha.config.GoogleCaptchaConfig;
import fr.funixgaming.api.core.utils.network.IPUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class GoogleCaptchaServiceTest {

    private static final String ACTION_CODE = "test-action-code";

    @Mock
    private GoogleCaptchaConfig mockGoogleCaptchaConfig;
    @Mock
    private IPUtils mockIPUtils;

    private GoogleCaptchaService googleCaptchaService;

    private final MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        googleCaptchaService = new GoogleCaptchaService(mockGoogleCaptchaConfig, mockIPUtils);
    }

    @Test
    void testCheckCode_whenCaptchaDisabled_doesNothing() {
        when(mockGoogleCaptchaConfig.isDisabled()).thenReturn(true);

        assertDoesNotThrow(() -> {
            googleCaptchaService.checkCode(mockHttpServletRequest, ACTION_CODE);
        });
    }

    @Test
    void testCheckCode_whenActionCodeEmpty_throwsApiBadRequestException() {
        when(mockGoogleCaptchaConfig.isDisabled()).thenReturn(false);

        ApiBadRequestException exception = assertThrows(ApiBadRequestException.class,
                () -> googleCaptchaService.checkCode(mockHttpServletRequest, " "));

        assertEquals("Action code empty.", exception.getMessage());
    }

    @Test
    void testCheckCode_whenCaptchaCodeNull_throwsApiBadRequestException() {
        when(mockGoogleCaptchaConfig.isDisabled()).thenReturn(false);

        ApiBadRequestException exception = assertThrows(ApiBadRequestException.class,
                () -> googleCaptchaService.checkCode(mockHttpServletRequest, ACTION_CODE));

        assertEquals("Le code google reCaptcha header est invalide. (code null)", exception.getMessage());
    }
}
