package fr.funixgaming.api.core;

import com.google.gson.Gson;
import fr.funixgaming.api.core.dtos.TestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = CoreAppTest.class)
@AutoConfigureMockMvc
public class CoreAppTest {
    public static final String ROUTE = "/test";

    private final Gson gson;
    private final MockMvc mockMvc;
    private final TestDTO testDTO = new TestDTO(
            null,
            "hey :"
    );

    @Autowired
    public CoreAppTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
        this.gson = new Gson();
    }

    @Test
    public void testCreation() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post(ROUTE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(testDTO)))
                .andExpect(status().isOk())
                .andReturn();

        final TestDTO result = gson.fromJson(mvcResult.getResponse().getContentAsString(), TestDTO.class);
        assertNotNull(result.getId());
        assertEquals(testDTO.getData(), result.getData());
    }

    @Test
    public void testUpdate() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post(ROUTE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(testDTO)))
                .andReturn();

        final TestDTO created = gson.fromJson(mvcResult.getResponse().getContentAsString(), TestDTO.class);
        created.setData("changed");

        mvcResult = mockMvc.perform(patch(ROUTE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(created)))
                .andExpect(status().isOk())
                .andReturn();
        final TestDTO result = gson.fromJson(mvcResult.getResponse().getContentAsString(), TestDTO.class);
        assertEquals(created.getId(), result.getId());
        assertEquals(created.getData(), result.getData());
    }

}
