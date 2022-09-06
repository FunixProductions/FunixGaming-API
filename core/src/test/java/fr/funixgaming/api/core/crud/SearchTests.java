package fr.funixgaming.api.core.crud;

import fr.funixgaming.api.core.TestApp;
import fr.funixgaming.api.core.crud.doc.*;
import fr.funixgaming.api.core.crud.enums.SearchOperation;
import fr.funixgaming.api.core.utils.JsonHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(
        classes = {
                TestApp.class
        }
)
public class SearchTests {

    public static final String ROUTE = "/test";

    @Autowired
    private TestRepository repository;

    @Autowired
    private TestMapper testMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonHelper gson;

    @Autowired
    private TestService testService;

    @BeforeEach
    public void cleanDb() {
        repository.deleteAll();
    }

    @Test
    public void testSearchNoPagination() throws Exception {
        TestEntity testDTO = new TestEntity();
        testDTO.setData("ouiData");
        testDTO.setNumber(10);

        TestEntity testDTO1 = new TestEntity();
        testDTO1.setData("NonData");
        testDTO1.setNumber(11);

        this.repository.save(testDTO);
        this.repository.save(testDTO1);

        checkSearchSuccess(testMapper.toDto(testDTO), "data:" + SearchOperation.EQUALS.getOperation() + ":" + testDTO.getData());
        checkSearchSuccess(testMapper.toDto(testDTO), "number:" + SearchOperation.EQUALS.getOperation() + ":" + testDTO.getNumber());
        checkSearchSuccess(testMapper.toDto(testDTO1), "data:" + SearchOperation.EQUALS.getOperation() + ":" + testDTO1.getData());
        checkSearchSuccess(testMapper.toDto(testDTO1), "number:" + SearchOperation.EQUALS.getOperation() + ":" + testDTO1.getNumber());
        checkSearchSuccess(testMapper.toDto(testDTO1), "number:" + SearchOperation.GREATER_THAN_OR_EQUAL_TO.getOperation() + ":11");
        checkSearchSuccess(testMapper.toDto(testDTO1), "number:" + SearchOperation.LESS_THAN_OR_EQUAL_TO.getOperation() + ":10");
        checkSearchSuccess(testMapper.toDto(testDTO), "number:" + SearchOperation.LESS_THAN_OR_EQUAL_TO.getOperation() + ":10,data:" + SearchOperation.EQUALS.getOperation() + ":ouiData");
    }

    @Test
    public void testSearchMultiple() throws Exception {
        TestEntity testDTO = new TestEntity();
        testDTO.setData("ouiData");
        testDTO.setNumber(10);

        TestEntity testDTO1 = new TestEntity();
        testDTO1.setData("NonData");
        testDTO1.setNumber(11);

        TestEntity testDTO2 = new TestEntity();
        testDTO1.setData("NonData");
        testDTO1.setNumber(-1);

        this.repository.save(testDTO);
        this.repository.save(testDTO1);
        this.repository.save(testDTO2);

        checkSearchMultiple(2, String.format("number:%s:0", SearchOperation.GREATER_THAN.getOperation()));
        checkSearchMultiple(1, String.format("number:%s:5", SearchOperation.LESS_THAN.getOperation()));
        checkSearchMultiple(2, String.format("data:%s:NonData", SearchOperation.EQUALS.getOperation()));
        checkSearchMultiple(0, String.format("data:%s:NonData2", SearchOperation.EQUALS.getOperation()));
    }

    @Test
    public void testSearchErrorString() throws Exception {
        checkSearchFail("data-ouiData");
        checkSearchFail("oui:");
        checkSearchFail(":data");
        checkSearchFail("sdkjqsdhfqkjdshqgfksjdhfgkqsjdhfgkjsdqhfgkqsjdhfgsqkjdhfgksqjdhgf");
        checkSearchFail("data:slkdj:oui");
        checkSearchFail("data:slkdj:oui,oui:");
        checkSearchFail("data:slkdj:oui,oui");
        checkSearchFail("data:slkdj:oui,oui:u:a");
        checkSearchFail("data:" + SearchOperation.EQUALS);
        checkSearchFail("data:" + SearchOperation.EQUALS + ":oui,oui:d");
    }

    private void checkSearchSuccess(final TestDTO toCheck, final String search) throws Exception {
        mockMvc.perform(get(ROUTE + "?search=" + search)).andExpect(status().isOk());
        final Page<TestDTO> list = testService.getAll(null, null, search, null);

        assertEquals(1, list.getNumberOfElements());
        final TestDTO check = list.getContent().get(0);
        assertNotNull(check.getId());
        assertNotNull(check.getCreatedAt());
        assertEquals(toCheck.getData(), check.getData());
        assertEquals(toCheck.getNumber(), check.getNumber());
    }

    private void checkSearchMultiple(final int nbrToGet, final String search) throws Exception {
        mockMvc.perform(get(ROUTE + "?search=" + search)).andExpect(status().isOk());

        final Page<TestDTO> list = testService.getAll(null, null, search, null);
        assertEquals(nbrToGet, list.getNumberOfElements());
    }

    private void checkSearchFail(final String search) throws Exception {
        mockMvc.perform(get(ROUTE + "?search=" + search)).andExpect(status().isBadRequest());
    }

}
