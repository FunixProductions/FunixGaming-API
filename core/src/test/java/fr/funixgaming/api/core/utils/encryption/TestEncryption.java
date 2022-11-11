package fr.funixgaming.api.core.utils.encryption;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TestEncryption {

    private final Encryption encryption;

    public TestEncryption() {
        this.encryption = new Encryption() {};
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
