package fr.funixgaming.api.server.mail;

import com.icegreen.greenmail.util.GreenMail;
import fr.funixgaming.api.client.mail.dtos.FunixMailDTO;
import fr.funixgaming.api.client.user.dtos.UserTokenDTO;
import fr.funixgaming.api.server.beans.GreenMailTestServer;
import fr.funixgaming.api.server.beans.JsonHelper;
import fr.funixgaming.api.server.user.components.UserTestComponent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ImportAutoConfiguration(GreenMailTestServer.class)
public class TestFunixMail {

    @Autowired
    private GreenMail greenMail;

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

    @Test
    public void testSendMail() throws Exception {
        final FunixMailDTO funixMailDTO = new FunixMailDTO();
        funixMailDTO.setText("test");
        funixMailDTO.setSubject("test subject");
        funixMailDTO.setTo("test@gmail.com");
        funixMailDTO.setFrom("test@test.fr");

        final FunixMailDTO response = sendMail(funixMailDTO);
        assertTrue(greenMail.waitForIncomingEmail(15000, 2));

        assertNotNull(response.getCreatedAt());
        assertNotNull(response.getId());
        assertFalse(response.isSend());
        assertEquals(funixMailDTO.getText(), response.getText());
        assertEquals(funixMailDTO.getSubject(), response.getSubject());
        assertEquals(funixMailDTO.getTo(), response.getTo());
        assertEquals(funixMailDTO.getFrom(), response.getFrom());

        Thread.sleep(1000);

        final FunixMailDTO data = getMailById(response.getId().toString());
        assertTrue(data.isSend());
        assertNotNull(data.getCreatedAt());
        assertNotNull(data.getUpdatedAt());
        assertNotNull(data.getId());
        assertEquals(funixMailDTO.getText(), data.getText());
        assertEquals(funixMailDTO.getSubject(), data.getSubject());
        assertEquals(funixMailDTO.getTo(), data.getTo());
        assertEquals(funixMailDTO.getFrom(), data.getFrom());
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
