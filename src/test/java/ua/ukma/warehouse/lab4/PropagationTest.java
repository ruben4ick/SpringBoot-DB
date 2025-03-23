package ua.ukma.warehouse.lab4;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.UnexpectedRollbackException;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.ukma.warehouse.item.Item;
import ua.ukma.warehouse.item.ItemRepository;
import ua.ukma.warehouse.lab4.propagation.OuterService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
public class PropagationTest {
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);
        registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.PostgreSQLDialect");
    }

    @Autowired
    private OuterService outerService;
    @Autowired private ItemRepository itemRepo;

    @BeforeEach
    void clear() {
        itemRepo.deleteAll();
    }

    @Test
    void testRequiredPropagation_innerThrows() {
        assertThrows(UnexpectedRollbackException.class, () -> {
            outerService.outerWithInnerRequired_InnerThrows();
        });

        List<Item> items = itemRepo.findAll();
        assertEquals(0, items.size()); // обидва записи мають відкочуватись
    }

    @Test
    void testRequiredPropagation_outerThrows() {
        try {
            outerService.outerWithInnerRequired_OuterThrows();
        } catch (Exception ignored) {}

        List<Item> items = itemRepo.findAll();
        // Вся транзакція відкотиться → нічого не збережеться
        assertEquals(0, items.size());
    }

    @Test
    void testRequiresNewPropagation_innerThrows() {
        outerService.outerWithInnerRequiresNew_InnerThrows();

        List<Item> items = itemRepo.findAll();
        // inner зафейлилась, але в окремій транзакції → не збережеться
        assertEquals(1, items.size());
        assertEquals("OuterItem", items.get(0).getName());
    }

    @Test
    void testRequiresNewPropagation_outerThrows() {
        try {
            outerService.outerWithInnerRequiresNew_OuterThrows();
        } catch (Exception ignored) {}

        List<Item> items = itemRepo.findAll();
        // outer зафейлилась, але inner збереглась
        assertEquals(1, items.size());
        assertEquals("InnerItem", items.get(0).getName());
    }

    @Test
    void testNestedPropagation_innerThrows() {
        outerService.outerWithInnerNested_InnerThrows();

        List<Item> items = itemRepo.findAll();
        // inner відкотилась (savepoint), outer збереглась
        assertEquals(1, items.size());
        assertEquals("OuterItem", items.get(0).getName());
    }

    @Test
    void testNestedPropagation_outerThrows() {
        try {
            outerService.outerWithInnerNested_OuterThrows();
        } catch (Exception ignored) {}

        List<Item> items = itemRepo.findAll();
        // outer кидає → все відкотиться, включно з inner
        assertEquals(0, items.size());
    }
}
