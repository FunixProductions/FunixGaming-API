package fr.funixgaming.api.core.utils;

import fr.funixgaming.api.core.TestApp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest(classes = TestApp.class)
@AutoConfigureMockMvc
public class TestEncryption {

    private final Encryption encryption;

    @Autowired
    public TestEncryption(Encryption encryption) {
        this.encryption = encryption;
    }

    @Test
    public void testEncrypt() throws Exception {
        final String str = "Bonjour je suis encrypté";
        final String encoded = encryption.convertToDatabase(str);
        final String decoded = encryption.convertToEntity(encoded);

        System.out.printf("String encoded : %s\n", encoded);
        System.out.printf("String decoded : %s\n", decoded);
        assertNotEquals(str, encoded);
        assertEquals(str, decoded);
    }

}
