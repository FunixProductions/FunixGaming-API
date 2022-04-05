package fr.funixgaming.api.core.utils.network;

import fr.funixgaming.api.core.TestApp;
import fr.funixgaming.api.core.config.ApiConfig;
import fr.funixgaming.api.core.exceptions.ApiException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TestApp.class)
@AutoConfigureMockMvc
public class IPUtilsTests {

    private final IPUtils ipUtils;

    @Autowired
    public IPUtilsTests(IPUtils ipUtils) {
        this.ipUtils = ipUtils;
    }

    @Test
    public void testLocalhostIpValid() throws ApiException {
        assertTrue(ipUtils.canAccess("127.0.0.1"));
    }

    @Test
    public void testWhitelistIp() throws ApiException {
        assertTrue(ipUtils.canAccess("8.8.8.8"));
        assertTrue(ipUtils.canAccess("120.10.5.3"));
        assertFalse(ipUtils.canAccess("9.9.9.9"));
    }

    @Test
    public void testInvalidIp() {
        try {
            assertTrue(ipUtils.canAccess("8.8.8.8.1.1.1.1.1."));
            fail("The ip should not wwork.");
        } catch (ApiException ignored) {
        }
    }

}
