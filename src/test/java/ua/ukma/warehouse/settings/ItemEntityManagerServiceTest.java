package ua.ukma.warehouse.settings;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ua.ukma.warehouse.item.Item;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class ItemEntityManagerServiceTest {

    @Autowired
    private ItemEntityManagerService itemService;

    private Item createSampleItem(String name) {
        Item item = new Item();
        item.setName(name);
        item.setQuantity(10);
        item.setPrice(100.0);
        return item;
    }


    @Test
    public void testPersistAndFindItem() {
        Item item = createSampleItem("Persisted Item");

        itemService.persistItem(item); // переходить у стан MANAGED
        assertNotNull(item.getId(), "ID має бути згенеровано після persist");

        Item found = itemService.findItem(item.getId());
        assertNotNull(found);
        assertEquals("Persisted Item", found.getName());
    }

    @Test
    @Transactional
    public void testRemoveItem() {
        Item item = createSampleItem("To Be Removed");
        itemService.persistItem(item);
        Long id = item.getId();

        itemService.removeItem(item); // видаляє з БД
        Item found = itemService.findItem(id);
        assertNull(found, "Об'єкт має бути видалений з бази");
    }

    @Test
    public void testMergeDetachedItem() {
        Item item = createSampleItem("To Be Merged");
        itemService.persistItem(item);

        // Імітація "detached" — зміни об'єкта поза EntityManager
        itemService.detachItem(item);
        item.setName("Merged Name");

        Item merged = itemService.mergeItem(item); // створює нову managed-копію
        assertNotSame(item, merged, "merge повертає нову керовану копію");
        assertEquals("Merged Name", merged.getName());

        Item found = itemService.findItem(merged.getId());
        assertEquals("Merged Name", found.getName());
    }

    @Test
    public void testDetachPreventsUpdates() {
        Item item = createSampleItem("To Be Detached");
        itemService.persistItem(item);

        itemService.detachItem(item); // тепер item — DETACHED
        item.setName("Detached Change"); // зміна не збережеться

        Item fromDb = itemService.findItem(item.getId());
        assertNotEquals("Detached Change", fromDb.getName(), "Detach повинен ігнорувати зміни");
    }

    @Test
    @Transactional
    public void testRefreshRestoresOriginalState() {
        Item item = createSampleItem("To Be Refreshed");
        itemService.persistItem(item);

        item.setName("Changed Locally"); // змінено в пам'яті, не в БД
        itemService.refreshItem(item); // refresh скасовує локальні зміни

        assertEquals("To Be Refreshed", item.getName(), "refresh має повернути значення з БД");
    }
}