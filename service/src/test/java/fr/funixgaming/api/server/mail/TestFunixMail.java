package fr.funixgaming.api.server.mail;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import fr.funixgaming.api.client.mail.dtos.FunixMailDTO;
import fr.funixgaming.api.client.user.dtos.UserTokenDTO;
import fr.funixgaming.api.server.user.components.UserTestComponent;
import fr.funixgaming.api.server.utils.JsonHelper;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TestFunixMail {

    @RegisterExtension
    static GreenMail greenMail = new GreenMail(ServerSetupTest.SMTP).withConfiguration(GreenMailConfiguration.aConfig().withDisabledAuthentication());

    private final String route = "/mail/";
    private final JsonHelper jsonHelper;
    private final UserTokenDTO tokenDTO;
    private final MockMvc mockMvc;

    @Autowired
    public TestFunixMail(UserTestComponent userTestComponent,
                         MockMvc mockMvc,
                         JsonHelper jsonHelper) throws Exception {
        this.tokenDTO = userTestComponent.loginUser(userTestComponent.createModoAccount());
        this.mockMvc = mockMvc;
        this.jsonHelper = jsonHelper;
    }

    private FunixMailDTO sendMail(final FunixMailDTO request) throws Exception {
        final MvcResult result = mockMvc.perform(post(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDTO.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(request))
                )
                .andExpect(status().isOk())
                .andReturn();

        return jsonHelper.fromJson(result.getResponse().getContentAsString(), FunixMailDTO.class);
    }

    private FunixMailDTO getMailById(final String id) throws Exception {
        final MvcResult result = mockMvc.perform(get(route + id)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDTO.getToken()))
                .andExpect(status().isOk())
                .andReturn();

        return jsonHelper.fromJson(result.getResponse().getContentAsString(), FunixMailDTO.class);
    }

}
