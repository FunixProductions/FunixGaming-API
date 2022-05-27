package fr.funixgaming.api.core.mail;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import fr.funixgaming.api.core.TestApp;
import fr.funixgaming.api.core.mail.dtos.ApiMailDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(
        classes = {
                TestApp.class
        }
)
@AutoConfigureMockMvc
public class TestMailService {

    @RegisterExtension
    static GreenMail greenMail = new GreenMail(ServerSetupTest.SMTP).withConfiguration(GreenMailConfiguration.aConfig().withDisabledAuthentication());

    private final MailService mailService;
    private final ApiMailDTO mailTest;

    @Autowired
    public TestMailService(MailService mailService) {
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
