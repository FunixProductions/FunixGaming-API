package fr.funixgaming.api.server.user.components;

import com.google.gson.Gson;
import fr.funixgaming.api.client.user.dtos.UserDTO;
import fr.funixgaming.api.client.user.dtos.requests.UserCreationDTO;
import fr.funixgaming.api.client.user.enums.UserRole;
import fr.funixgaming.api.server.user.entities.User;
import fr.funixgaming.api.server.user.mappers.UserMapper;
import fr.funixgaming.api.server.user.repositories.UserRepository;
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
    private final UserMapper userMapper;
    private final Gson gson = new Gson();

    public UserDTO createAccount() throws Exception {
        final UserCreationDTO creationDTO = new UserCreationDTO();
        creationDTO.setEmail("test@gmail.com");
        creationDTO.setUsername("test");
        creationDTO.setPassword("passwordoui");
        creationDTO.setPasswordConfirmation("passwordoui");

        MvcResult mvcResult = this.mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(creationDTO)))
                .andExpect(status().isOk())
                .andReturn();

        return gson.fromJson(mvcResult.getResponse().getContentAsString(), UserDTO.class);
    }

    public UserDTO createAdminAccount() {
        final User user = new User();

        user.setUsername("admin");
        user.setPassword("oui");
        user.setEmail("admin@gmail.com");
        user.setRole(UserRole.ADMIN);
        return userMapper.toDto(userRepository.save(user));
    }

    public UserDTO createModoAccount() {
        final User user = new User();

        user.setUsername("modo");
        user.setPassword("oui");
        user.setEmail("modo@gmail.com");
        user.setRole(UserRole.MODERATOR);
        return userMapper.toDto(userRepository.save(user));
    }

}
