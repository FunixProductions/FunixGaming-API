package fr.funixgaming.api.funixbot.service.resources;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FunixBotCommandResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private

    @Test
    void testGetCommands() throws Exception {
        mockMvc.perform(get("/funixbot/command"))
                .andExpect(status().isOk());
    }

}
