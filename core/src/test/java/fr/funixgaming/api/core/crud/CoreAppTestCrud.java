package fr.funixgaming.api.core.crud;

import com.google.gson.reflect.TypeToken;
import fr.funixgaming.api.core.TestApp;
import fr.funixgaming.api.core.crud.doc.*;
import fr.funixgaming.api.core.utils.JsonHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.lang.reflect.Type;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {TestApp.class})
@AutoConfigureMockMvc
public class CoreAppTestCrud {
    public static final String ROUTE = "/test";

    private final JsonHelper gson;
    private final MockMvc mockMvc;
    private final TestRepository repository;

    @Autowired
    public CoreAppTestCrud(MockMvc mockMvc,
                           TestRepository repository,
                           JsonHelper gson) {
        this.mockMvc = mockMvc;
        this.repository = repository;
        this.gson = gson;

        repository.deleteAll();
    }

    @Test
    public void testCreation() throws Exception {
        final TestDTO testDTO = new TestDTO();
        testDTO.setData("oui");

        MvcResult mvcResult = mockMvc.perform(post(ROUTE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(testDTO)))
                .andExpect(status().isOk())
                .andReturn();

        final TestDTO result = gson.fromJson(mvcResult.getResponse().getContentAsString(), TestDTO.class);
        assertNotNull(result.getId());
        assertNotNull(result.getCreatedAt());
        assertNull(result.getUpdatedAt());
        assertEquals(testDTO.getData(), result.getData());
    }

    @Test
    public void testUpdate() throws Exception {
        final TestDTO testDTO = new TestDTO();
        testDTO.setData("oui");

        MvcResult mvcResult = mockMvc.perform(post(ROUTE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(testDTO)))
                .andExpect(status().isOk())
                .andReturn();

        final TestDTO created = gson.fromJson(mvcResult.getResponse().getContentAsString(), TestDTO.class);
        created.setData("changed");
        created.setCreatedAt(null);

        mvcResult = mockMvc.perform(patch(ROUTE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(created)))
                .andExpect(status().isOk())
                .andReturn();

        final TestDTO result = gson.fromJson(mvcResult.getResponse().getContentAsString(), TestDTO.class);
        assertNotNull(result.getCreatedAt());
        assertNotEquals(created.getUpdatedAt(), result.getUpdatedAt());
        assertEquals(created.getId(), result.getId());
        assertEquals(created.getData(), result.getData());
    }

    @Test
    public void testUpdateBatch() throws Exception {
        final Set<TestDTO> list = new HashSet<>();

        TestDTO testDTO = new TestDTO();
        testDTO.setData("oui");
        MvcResult mvcResult = mockMvc.perform(post(ROUTE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(testDTO)))
                .andExpect(status().isOk())
                .andReturn();
        TestDTO created = gson.fromJson(mvcResult.getResponse().getContentAsString(), TestDTO.class);
        list.add(created);

        testDTO = new TestDTO();
        testDTO.setData("oui2");
        mvcResult = mockMvc.perform(post(ROUTE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(testDTO)))
                .andExpect(status().isOk())
                .andReturn();
        created = gson.fromJson(mvcResult.getResponse().getContentAsString(), TestDTO.class);
        list.add(created);

        int i = 0;
        for (final TestDTO test : list) {
            test.setData("oui " + i);
        }

        mvcResult = mockMvc.perform(patch(ROUTE + "/batch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(list)))
                .andExpect(status().isOk())
                .andReturn();

        final Type type = new com.google.common.reflect.TypeToken<Set<TestDTO>>(){}.getType();
        final Set<TestDTO> result = gson.fromJson(mvcResult.getResponse().getContentAsString(), type);

        for (final TestDTO dto : result) {
            TestDTO compare = null;

            for (final TestDTO search : list) {
                if (search.equals(dto)) {
                    compare = search;
                }
            }

            if (compare != null) {
                assertNotNull(dto.getCreatedAt());
                assertNotEquals(compare.getUpdatedAt(), dto.getUpdatedAt());
                assertEquals(compare.getId(), dto.getId());
                assertEquals(compare.getData(), dto.getData());
            } else {
                fail("elem not found");
            }
        }
    }

    @Test
    public void testUpdateNoId() throws Exception {
        mockMvc.perform(patch(ROUTE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(new TestDTO())))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateEntityNotCreated() throws Exception {
        final TestDTO testDTO = new TestDTO();
        testDTO.setId(UUID.randomUUID());

        mockMvc.perform(patch(ROUTE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(testDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAll() throws Exception {
        int size = 3;

        for (int i = 0; i < size; ++i) {
            final TestEntity entity = new TestEntity();

            entity.setData(Integer.toString(i));
            this.repository.save(entity);
        }

        MvcResult mvcResult = mockMvc.perform(get(ROUTE))
                .andExpect(status().isOk())
                .andReturn();

        Type type = new TypeToken<Set<TestDTO>>() {}.getType();
        final Set<TestDTO> entities = gson.fromJson(mvcResult.getResponse().getContentAsString(), type);

        assertEquals(size, entities.size());
        for (final TestDTO entity : entities) {
            assertNotNull(entity.getData());
            assertNotNull(entity.getId());
        }
    }

    @Test
    public void testGetById() throws Exception {
        final TestDTO testDTO = new TestDTO();
        testDTO.setData("oui");

        MvcResult mvcResult = mockMvc.perform(post(ROUTE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(testDTO)))
                .andExpect(status().isOk())
                .andReturn();

        final TestDTO result = gson.fromJson(mvcResult.getResponse().getContentAsString(), TestDTO.class);
        mockMvc.perform(get(ROUTE + "/" + result.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetByIdNotCreated() throws Exception {
        mockMvc.perform(get(ROUTE + "/" + UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testRemove() throws Exception {
        Type type = new TypeToken<Set<TestDTO>>() {}.getType();

        TestEntity entity = new TestEntity();

        entity.setData("TEST");
        entity = this.repository.save(entity);

        MvcResult mvcResult = mockMvc.perform(get(ROUTE)).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
        Set<TestDTO> entities = gson.fromJson(mvcResult.getResponse().getContentAsString(), type);
        assertEquals(1, entities.size());

        mockMvc.perform(delete(ROUTE + "?id=" + entity.getUuid()))
                .andExpect(status().isOk());

        mvcResult = mockMvc.perform(get(ROUTE)).andReturn();
        entities = gson.fromJson(mvcResult.getResponse().getContentAsString(), type);
        assertEquals(0, entities.size());
    }

    @Test
    public void testRemoveNoId() throws Exception {
        mockMvc.perform(delete(ROUTE))
                .andExpect(status().isBadRequest());
    }

}
