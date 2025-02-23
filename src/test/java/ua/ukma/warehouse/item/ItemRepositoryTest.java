package ua.ukma.warehouse.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void testSaveItem() {
        Item item = new Item();
        item.setName("Laptop");
        item.setQuantity(10);
        item.setPrice(1500.0);
        Item savedItem = itemRepository.save(item);

        assertThat(savedItem).isNotNull();
        assertThat(savedItem.getId()).isNotNull();
    }

    @Test
    public void testFindAllItems() {
        Item item1 = new Item();
        item1.setName("Laptop");
        item1.setQuantity(10);
        item1.setPrice(1500.0);
        itemRepository.save(item1);

        List<Item> items = itemRepository.findAll();
        assertThat(items).hasSize(1);
    }

    @Test
    public void testDeleteItem() {
        Item item = new Item();
        item.setName("Phone");
        item.setQuantity(5);
        item.setPrice(800.0);
        itemRepository.save(item);

        itemRepository.deleteById(item.getId());
        assertThat(itemRepository.findById(item.getId())).isEmpty();
    }
}
