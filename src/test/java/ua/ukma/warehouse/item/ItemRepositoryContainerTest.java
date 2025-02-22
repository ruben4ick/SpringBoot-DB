package ua.ukma.warehouse.item;

import ua.ukma.warehouse.Item;
import ua.ukma.warehouse.ItemRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ItemRepositoryContainerTest {

    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("warehouse")
            .withUsername("user")
            .withPassword("password");

    static {
        postgres.start();
    }

    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    void setup() {
        itemRepository.deleteAll();
    }

    @Test
    public void testCreateItem() {
        Item item = new Item();
        item.setName("Tablet");
        item.setQuantity(7);
        item.setPrice(600.0);
        Item savedItem = itemRepository.save(item);

        assertThat(savedItem).isNotNull();
        assertThat(savedItem.getId()).isNotNull();
    }

    @Test
    public void testFindItemsInContainer() {
        Item item1 = new Item();
        item1.setName("Monitor");
        item1.setQuantity(3);
        item1.setPrice(300.0);
        itemRepository.save(item1);

        List<Item> items = itemRepository.findAll();
        assertThat(items).hasSize(1);
    }
}
