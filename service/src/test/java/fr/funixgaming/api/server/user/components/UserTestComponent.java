package fr.funixgaming.api.server.user.components;

import fr.funixgaming.api.client.user.dtos.UserTokenDTO;
import fr.funixgaming.api.client.user.dtos.requests.UserLoginDTO;
import fr.funixgaming.api.client.user.enums.UserRole;
import fr.funixgaming.api.server.user.entities.User;
import fr.funixgaming.api.server.user.repositories.UserRepository;
import fr.funixgaming.api.server.utils.JsonHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Component
@RequiredArgsConstructor
public class UserTestComponent {

    private final MockMvc mockMvc;
    private final UserRepository userRepository;
    private final JsonHelper jsonHelper;

    public User createAdminAccount() {
        final User user = new User();

        user.setUsername(UUID.randomUUID().toString());
        user.setPassword("password");
        user.setEmail("admin@gmail.com");
        user.setRole(UserRole.ADMIN);

        return userRepository.save(user);
    }

    public User createModoAccount() {
        final User user = new User();

        user.setUsername(UUID.randomUUID().toString());
        user.setPassword("oui");
        user.setEmail("modo@gmail.com");
        user.setRole(UserRole.MODERATOR);

        return userRepository.save(user);
    }

    public User createBasicUser() {
        final User user = new User();

        user.setUsername(UUID.randomUUID().toString());
        user.setPassword("oui");
        user.setEmail("user@gmail.com");

        return userRepository.save(user);
    }

    public UserTokenDTO loginUser(final User user) throws Exception {
        final UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setUsername(user.getUsername());
        userLoginDTO.setPassword(user.getPassword());

        MvcResult mvcResult = this.mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(userLoginDTO)))
                .andExpect(status().isOk())
                .andReturn();

        return jsonHelper.fromJson(mvcResult.getResponse().getContentAsString(), UserTokenDTO.class);
    }

}
