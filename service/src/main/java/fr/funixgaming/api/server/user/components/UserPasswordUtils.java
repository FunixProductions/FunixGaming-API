package fr.funixgaming.api.server.user.components;

import com.google.common.base.Strings;
import fr.funixgaming.api.core.exceptions.ApiBadRequestException;
import org.springframework.stereotype.Component;

@Component
public class UserPasswordUtils {

    private final int minimumLength = 8;
    private final int minimumNumbers = 2;
    private final int minimumUppers = 2;

    /**
     * Check if the password is strong enough for app usage
     * @param password password string
     * @throws ApiBadRequestException if the password is not valid
     */
    public void checkPassword(final String password) throws ApiBadRequestException {
        if (Strings.isNullOrEmpty(password)) {
            throw new ApiBadRequestException("Votre mot de passe est vide.");
        }

        if (password.length() <= minimumLength) {
            throw new ApiBadRequestException("Votre mot de passe doit au minimum avoir " + minimumLength + " caractères.");
        } else if (!isPasswordContainsNumbers(password)) {
            throw new ApiBadRequestException("Votre mot de passe doit contenir " + minimumNumbers + " chiffres");
        } else if (!isPasswordContainsUpperCases(password)) {
            throw new ApiBadRequestException("Votre mot de passe doit contenir " + minimumUppers + " caractères en majuscules.");
        }
    }

    private boolean isPasswordContainsNumbers(final String password) {
        int numbersCount = 0;

        for (char c : password.toCharArray()) {
            if (c >= '0' && c <= '9') {
                ++numbersCount;
            }
        }
        return numbersCount >= minimumNumbers;
    }

    private boolean isPasswordContainsUpperCases(final String password) {
        int upperCaseCount = 0;

        for (char c : password.toCharArray()) {
            if (c >= 'A' && c <= 'Z') {
                ++upperCaseCount;
            }
        }
        return upperCaseCount >= minimumUppers;
    }

}
