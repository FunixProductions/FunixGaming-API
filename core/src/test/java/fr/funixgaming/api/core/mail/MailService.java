package fr.funixgaming.api.core.mail;

import fr.funixgaming.api.core.mail.services.ApiMailService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailService extends ApiMailService {
    public MailService(JavaMailSender mailSender) {
        super(mailSender);
    }
}
