package fr.funixgaming.api.server.user;

import fr.funixgaming.api.client.user.dtos.UserTokenDTO;
import fr.funixgaming.api.server.user.components.UserTestComponent;
import fr.funixgaming.api.server.utils.JsonHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TestUserResourceUtils {

    private final MockMvc mockMvc;

    private final String route;
    private final String token;

    @Autowired
    public TestUserResourceUtils(MockMvc mockMvc,
                                 UserTestComponent userTestComponent) throws Exception {
        final UserTokenDTO tokenDTO = userTestComponent.loginUser(userTestComponent.createAdminAccount());

        this.mockMvc = mockMvc;
        this.route = "/user/";
        this.token = tokenDTO.getToken();
    }

    @Test
    public void getApiAccount() throws Exception {
        mockMvc.perform(get(route + "apiAccount")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk());
    }

}
