package fr.funixgaming.api.core.beans;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class GreenMailTestServer {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public GreenMail greenMailTestServer() {
        return new GreenMail(ServerSetupTest.SMTP).withConfiguration(GreenMailConfiguration.aConfig().withDisabledAuthentication());
    }

}
