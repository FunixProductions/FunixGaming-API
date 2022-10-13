package fr.funixgaming.api.core.mail;

import com.icegreen.greenmail.util.GreenMail;
import fr.funixgaming.api.core.TestApp;
import fr.funixgaming.api.core.beans.GreenMailTestServer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest(classes = TestApp.class)
@ImportAutoConfiguration(GreenMailTestServer.class)
@AutoConfigureMockMvc
public class TestMailService {

    @Autowired
    private GreenMail greenMail;

    @Autowired
    private MailService mailService;

    @Test
    public void testSendMail() {
        try {
            final MailDTO mailTest = new MailDTO();
            mailTest.setFrom("test@localhost.fr");
            mailTest.setTo("funixgaming7@gmail.com");
            mailTest.setSubject("Un mail de test envoyé par un test unitaire");
            mailTest.setText("Body mail ! Sans HTML quel thug.");

            this.mailService.sendMail(mailTest);
            assertTrue(greenMail.waitForIncomingEmail(15000, 1));
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void testSendMailWithAttachment() {
        try {
            final MailDTO mailTest = new MailDTO();
            mailTest.setFrom("test@localhost.fr");
            mailTest.setTo("funixgaming7@gmail.com");
            mailTest.setSubject("Un mail de test envoyé par un test unitaire avec un fichier en pièce jointe");
            mailTest.setText("Body mail ! Sans HTML quel thug.");

            this.mailService.sendMail(mailTest, new File("pom.xml"));
            assertTrue(greenMail.waitForIncomingEmail(15000, 1));
        } catch (Exception e) {
            fail(e);
        }
    }

}
