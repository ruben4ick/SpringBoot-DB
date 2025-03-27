package ua.ukma.warehouse.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
public class ItemNameIndexTest {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void shouldNotAllowDuplicateNames() {
        Item item1 = new Item(null, "Apple", 10, 2.5, null);
        itemRepository.save(item1);

        Item item2 = new Item(null, "Apple", 5, 3.0, null);

        assertThrows(DataIntegrityViolationException.class, () -> {
            itemRepository.save(item2);
        });
    }
}
