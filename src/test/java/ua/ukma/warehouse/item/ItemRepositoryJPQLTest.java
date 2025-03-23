package ua.ukma.warehouse.item;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemRepositoryJPQLTest {

    @Autowired
    private ItemRepositoryJPQL itemRepository;

    @BeforeEach
    void setUp() {
        itemRepository.save(new Item(null, "Laptop", 10, 1000.0, null));
        itemRepository.save(new Item(null, "Phone", 3, 500.0, null));
    }

    @Test
    void testUpdateItemPriceByName() {
        int updated = itemRepository.updateItemPriceByName("Laptop", 1200.0);
        assertEquals(1, updated);
    }

    @Test
    void testDeleteItemsWithQuantityLessThan() {
        int deleted = itemRepository.deleteItemsWithQuantityLessThan(5);
        assertEquals(1, deleted);
    }

    @Test
    void testGetAverageItemPriceByCategory() {
        // Поки що всі без категорій, тому null => 1 група
        Double avg = itemRepository.getAverageItemPriceByCategory();
        assertNotNull(avg);
        assertTrue(avg > 0);
    }

    @Test
    void testGetTotalItemQuantity() {
        Long total = itemRepository.getTotalItemQuantity();
        assertEquals(13, total);
    }

    @Test
    void testGetItemCount() {
        Long count = itemRepository.getItemCount();
        assertEquals(2, count);
    }
}
