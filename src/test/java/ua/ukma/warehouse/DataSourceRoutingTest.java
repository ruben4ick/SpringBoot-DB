package ua.ukma.warehouse;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ua.ukma.warehouse.datasource.DataSourceContextHolder;
import ua.ukma.warehouse.datasource.DataSourceEnum;
import ua.ukma.warehouse.item.Item;
import ua.ukma.warehouse.item.ItemRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("testRouting")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DataSourceRoutingTest {

    @Autowired
    private DataSourceContextHolder contextHolder;

    @Autowired
    private ItemRepository itemRepository;

    @AfterEach
    void cleanUp() {
        for (DataSourceEnum dataSource : DataSourceEnum.values()) {
            contextHolder.setBranchContext(dataSource);
            itemRepository.deleteAll();
        }
        contextHolder.clearBranchContext();
    }

    @Test
    @Order(1)
    void insertIntoPrimary() {
        contextHolder.setBranchContext(DataSourceEnum.PRIMARY);

        Item item = new Item(null, "PrimaryItem", 10, 25.0, null);
        itemRepository.save(item);

        List<Item> items = itemRepository.findAll();
        assertEquals(1, items.size());
        assertEquals("PrimaryItem", items.get(0).getName());

        contextHolder.clearBranchContext();
    }

    @Test
    @Order(2)
    void insertIntoSecondary() {
        contextHolder.setBranchContext(DataSourceEnum.SECONDARY);

        Item item = new Item(null, "SecondaryItem", 5, 30.0, null);
        itemRepository.save(item);

        List<Item> items = itemRepository.findAll();
        assertEquals(1, items.size());
        assertEquals("SecondaryItem", items.get(0).getName());

        contextHolder.clearBranchContext();
    }

    @Test
    @Order(3)
    void testIsolationBetweenDatabases() {
        contextHolder.setBranchContext(DataSourceEnum.PRIMARY);
        itemRepository.save(new Item(null, "PrimaryItem", 10, 25.0, null));

        contextHolder.setBranchContext(DataSourceEnum.SECONDARY);
        itemRepository.save(new Item(null, "SecondaryItem", 5, 30.0, null));

        contextHolder.setBranchContext(DataSourceEnum.PRIMARY);
        List<Item> primaryItems = itemRepository.findAll();
        assertTrue(primaryItems.stream().anyMatch(i -> i.getName().equals("PrimaryItem")));

        contextHolder.setBranchContext(DataSourceEnum.SECONDARY);
        List<Item> secondaryItems = itemRepository.findAll();
        assertTrue(secondaryItems.stream().anyMatch(i -> i.getName().equals("SecondaryItem")));

        contextHolder.clearBranchContext();
    }
}
