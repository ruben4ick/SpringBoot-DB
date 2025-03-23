package ua.ukma.warehouse.lab4;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ua.ukma.warehouse.item.ItemRepository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
public class ItemTransactionsTest {
     @Autowired
     private ItemTransactions itemTransactions;
     @Autowired
     private ItemRepository itemRepo;

    @BeforeEach
    void clear() {
        itemRepo.deleteAll();
    }

     @Test
     public void transactionalSave() {
         itemTransactions.transactionalSave(false);
         assertEquals(1, itemRepo.count());
     }

     @Test
     public void transactionalSaveWithError() {
         assertThrows(RuntimeException.class, () -> itemTransactions.transactionalSave(true));
         assertEquals(0, itemRepo.count());
     }

     @Test
     public void templateSave() {
         itemTransactions.templateSave(false);
         assertEquals(1, itemRepo.count());
     }

     @Test
     public void templateSaveWithError() {
         assertThrows(RuntimeException.class, () -> itemTransactions.templateSave(true));
         assertEquals(0, itemRepo.count());
     }

     @Test
    public void entityManagerSave() {
        itemTransactions.entityManagerSave(false);
        assertEquals(1, itemRepo.count());
    }

     @Test
     public void entityManagerSaveWithError() {
         assertThrows(RuntimeException.class, () -> itemTransactions.entityManagerSave(true));
         assertEquals(0, itemRepo.count());
     }
}
