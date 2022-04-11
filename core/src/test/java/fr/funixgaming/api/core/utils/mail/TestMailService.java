package fr.funixgaming.api.core.utils.mail;

import fr.funixgaming.api.core.utils.mail.dtos.MailDTO;
import fr.funixgaming.api.core.utils.mail.services.MailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TestMailService {

    private final MailService mailService;
    private final MailDTO mailTest;

    @Autowired
    public TestMailService(MailService mailService) throws Exception {
        this.mailService = mailService;
        this.mailTest = new MailDTO();

        this.mailTest.setFrom("test@localhost.fr");
        this.mailTest.setTo("funixgaming7@gmail.com");
        this.mailTest.setSubject("Un mail de test envoyé par un test unitaire");
        this.mailTest.setText("Body mail ! Sans HTML quel thug.");
    }

    @Test
    public void testSendMail() {
        try {
            this.mailService.sendMail(mailTest);
        } catch (Exception e) {
            fail(e);
        }
    }

}
