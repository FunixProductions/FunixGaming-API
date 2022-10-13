package fr.funixgaming.api.client.config;

import fr.funixgaming.api.client.TestApp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureMockMvc
@SpringBootTest(classes = TestApp.class)
public class FunixApiConfigTest {

    @Autowired
    private FunixApiConfig funixApiConfig;

    @Test
    public void testConfig() {
        assertEquals("http://localhost:6643", funixApiConfig.getAppDomainUrl());
        assertEquals("funixtest", funixApiConfig.getUserApiUsername());
        assertEquals("passwordfunix", funixApiConfig.getUserApiPassword());
        assertEquals("this@gmail.com", funixApiConfig.getEmail());
        assertEquals(10, funixApiConfig.getPasswordNumbers());
        assertEquals(11, funixApiConfig.getPasswordSpecials());
        assertEquals(12, funixApiConfig.getPasswordCaps());
        assertEquals(13, funixApiConfig.getPasswordMin());
    }

}
