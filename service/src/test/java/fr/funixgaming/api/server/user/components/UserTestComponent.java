package fr.funixgaming.api.server.user.components;

import fr.funixgaming.api.client.user.dtos.UserDTO;
import fr.funixgaming.api.client.user.dtos.UserTokenDTO;
import fr.funixgaming.api.client.user.dtos.requests.UserCreationDTO;
import fr.funixgaming.api.client.user.dtos.requests.UserLoginDTO;
import fr.funixgaming.api.client.user.enums.UserRole;
import fr.funixgaming.api.server.user.entities.User;
import fr.funixgaming.api.server.user.repositories.UserRepository;
import fr.funixgaming.api.server.user.repositories.UserTokenRepository;
import fr.funixgaming.api.server.utils.JsonHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Component
@RequiredArgsConstructor
public class UserTestComponent {

    private final MockMvc mockMvc;
    private final UserRepository userRepository;
    private final UserTokenRepository userTokenRepository;
    private final JsonHelper jsonHelper;

    public UserDTO createAccount() throws Exception {
        cleanDb();

        final UserCreationDTO creationDTO = new UserCreationDTO();
        creationDTO.setEmail("test@gmail.com");
        creationDTO.setUsername("test");
        creationDTO.setPassword("passwordoui");
        creationDTO.setPasswordConfirmation("passwordoui");

        MvcResult mvcResult = this.mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(creationDTO)))
                .andExpect(status().isOk())
                .andReturn();

        return jsonHelper.fromJson(mvcResult.getResponse().getContentAsString(), UserDTO.class);
    }

    public User createAdminAccount() {
        cleanDb();

        final User user = new User();

        user.setUsername("admin");
        user.setPassword("oui");
        user.setEmail("admin@gmail.com");
        user.setRole(UserRole.ADMIN);
        return userRepository.save(user);
    }

    public User createModoAccount() {
        cleanDb();

        final User user = new User();

        user.setUsername("modo");
        user.setPassword("oui");
        user.setEmail("modo@gmail.com");
        user.setRole(UserRole.MODERATOR);
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

    private void cleanDb() {
        userTokenRepository.deleteAll();
        userRepository.deleteAll();
    }

}
