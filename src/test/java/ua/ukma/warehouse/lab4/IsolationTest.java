package ua.ukma.warehouse.lab4;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.ukma.warehouse.item.Item;
import ua.ukma.warehouse.item.ItemRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
public class IsolationTest {

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

    @Autowired private ItemIsolationService isolationService;
    @Autowired private ItemRepository itemRepo;

    @BeforeEach
    void setup() {
        itemRepo.deleteAll();
    }

    @Test
    void testReadCommittedIsolation() throws InterruptedException {
        itemRepo.save(new Item(null, "StartItem", 1, 1.0, null));

        List<Integer> result = new ArrayList<>();

        Thread reader = new Thread(() -> {
            List<Integer> sizes = isolationService.readCommitted();
            result.addAll(sizes);
        });
        reader.start();

        Thread.sleep(1000);
        itemRepo.save(new Item(null, "NewItem", 1, 1.0, null));
        reader.join();

        assertEquals(Integer.valueOf(1), result.get(0)); // перше читання
        assertEquals(Integer.valueOf(2), result.get(1)); // друге бачить новий рядок
    }

    @Test
    void testRepeatableReadIsolation() throws InterruptedException {
        itemRepo.save(new Item(null, "StartItem", 1, 1.0, null));

        List<Integer> result = new ArrayList<>();

        Thread reader = new Thread(() -> {
            List<Integer> sizes = isolationService.repeatableRead();
            result.addAll(sizes);
        });
        reader.start();

        Thread.sleep(1000);
        itemRepo.save(new Item(null, "InsertedMidTx", 1, 1.0, null));

        reader.join();

        assertEquals(Integer.valueOf(1), result.get(0));
        assertEquals(Integer.valueOf(1), result.get(1)); // новий запис не видно
    }

    @Test
    void testSerializableIsolation() throws InterruptedException {
        itemRepo.save(new Item(null, "StartItem", 1, 1.0, null));

        List<Integer> result = new ArrayList<>();

        Thread reader = new Thread(() -> {
            List<Integer> sizes = isolationService.serializableRead();
            result.addAll(sizes);
        });
        reader.start();

        Thread.sleep(1000);
        itemRepo.save(new Item(null, "InsertedDuringSerializable", 1, 1.0, null));
        reader.join();

        assertEquals(Integer.valueOf(1), result.get(0));
        assertEquals(Integer.valueOf(1), result.get(1)); // новий запис не видно
    }

      //this test does not work at this commit because of the index on the name column
//    @Test
//    void testRepeatableReadAllowsDuplicateInsert() throws InterruptedException {
//        Runnable task = () -> {
//            isolationService.insertIfNotExistsRepeatableRead("UniqueRepeatable");
//        };
//
//        Thread t1 = new Thread(task);
//        Thread t2 = new Thread(task);
//
//        t1.start();
//        t2.start();
//
//        t1.join();
//        t2.join();
//
//        long count = itemRepo.countByNameStartingWith("UniqueRepeatable");
//        // обидві транзакції побачили count = 0 → вставили
//        assertEquals(2, count); // дублікати
//    }

    @Test
    void testSerializableDetectsRaceCondition() throws InterruptedException {
        Runnable task = () -> {
            try {
                isolationService.insertIfNotExistsSerializable("UniqueItem");
            } catch (Exception e) {
                System.out.println("Serializable failed: " + e.getMessage());
            }
        };

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        long count = itemRepo.countByNameStartingWith("UniqueItem");
        assertEquals(1, count); // друга транзакція не вставила, бо перша вже вставила
    }

    private void sleep(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }
}
