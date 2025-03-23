package ua.ukma.warehouse.lab4;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ua.ukma.warehouse.item.Item;
import ua.ukma.warehouse.item.ItemRepository;

import java.util.List;

@Service
public class ItemIsolationService {

    private final ItemRepository itemRepo;

    public ItemIsolationService(ItemRepository itemRepo) {
        this.itemRepo = itemRepo;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<Integer> readCommitted() {
        List<Item> firstRead = itemRepo.findAll();
        sleep(2000); // чекаємо на додавання нового запису
        List<Item> secondRead = itemRepo.findAll();
        System.out.println("Read committed - first: " + firstRead.size() + ", second: " + secondRead.size());
        return List.of(firstRead.size(), secondRead.size());
    }


    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public List<Integer> repeatableRead() {
        List<Item> firstRead = itemRepo.findAll();
        sleep(2000); // чекаємо на додавання нового запису
        List<Item> secondRead = itemRepo.findAll();

        System.out.println("First read: " + firstRead.size() + ", Second read: " + secondRead.size());

        return List.of(firstRead.size(), secondRead.size());
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public List<String> repeatableReadOnUpdate() {
        Item item1 = itemRepo.findById(1L).orElseThrow();
        String name1 = item1.getName();
        sleep(2000);
        Item item2 = itemRepo.findById(1L).orElseThrow();
        String name2 = item2.getName();
        System.out.println("Repeatable read - first: " + name1 + ", second: " + name2);
        return List.of(name1, name2);
    }


    @Transactional(isolation = Isolation.SERIALIZABLE)
    public List<Integer> serializableRead() {
        List<Item> firstRead = itemRepo.findAll();
        sleep(2000); // чекаємо на додавання нового запису
        List<Item> secondRead = itemRepo.findAll();
        System.out.println("Serializable - first: " + firstRead.size() + ", second: " + secondRead.size());
        return List.of(firstRead.size(), secondRead.size());
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void insertIfNotExistsRepeatableRead(String name) {
        if (itemRepo.countByNameStartingWith(name) == 0) {
            sleep(1000);
            itemRepo.save(new Item(null, name, 1, 1.0, null));
            System.out.println("Inserted " + name + " in REPEATABLE_READ");
        } else {
            System.out.println("Skipped insert in REPEATABLE_READ");
        }
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void insertIfNotExistsSerializable(String name) {
        if (itemRepo.countByNameStartingWith(name) == 0) {
            sleep(1000); // імітуємо затримку між перевіркою і вставкою
            itemRepo.save(new Item(null, name, 1, 1.0, null));
            System.out.println("Inserted " + name + " in SERIALIZABLE");
        } else {
            System.out.println("Skipped insert in SERIALIZABLE");
        }
    }

    private void sleep(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }
}
