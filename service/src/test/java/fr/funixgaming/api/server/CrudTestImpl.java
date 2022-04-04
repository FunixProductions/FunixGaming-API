package fr.funixgaming.api.server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fr.funixgaming.api.core.crud.dtos.ApiDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.lang.reflect.Type;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
public class CrudTestImpl<DTO extends ApiDTO> {

    private final MockMvc mockMvc;
    private final String route;
    private final String bearerToken;

    private final Gson gson = new Gson();

    public Set<DTO> testGet() throws Exception {
        final MvcResult result = mockMvc.perform(get(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken))
                .andExpect(status().isOk())
                .andReturn();

        final Type type = new TypeToken<Set<DTO>>() {}.getType();
        return gson.fromJson(result.getResponse().getContentAsString(), type);
    }

    public DTO create(final DTO request) throws Exception {
        final MvcResult result = mockMvc.perform(post(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(request)))
                .andExpect(status().isOk())
                .andReturn();

        return getResponse(result);
    }

    public DTO update(final DTO request) throws Exception {
        final MvcResult result = mockMvc.perform(patch(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(request)))
                .andExpect(status().isOk())
                .andReturn();

        return getResponse(result);
    }

    public DTO findById(final String id) throws Exception {
        final MvcResult result = mockMvc.perform(get(route + "/" + id)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken))
                .andExpect(status().isOk())
                .andReturn();

        return getResponse(result);
    }

    public void deleteObj(final String id) throws Exception {
        mockMvc.perform(delete(route + "/" + id)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken))
                .andExpect(status().isOk());
    }

    private DTO getResponse(final MvcResult result) throws Exception {
        return gson.fromJson(result.getResponse().getContentAsString(), getType());
    }

    private Type getType() {
        return new TypeToken<DTO>() {}.getType();
    }
}
