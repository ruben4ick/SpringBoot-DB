package ua.ukma.warehouse.lab4;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionTemplate;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ua.ukma.warehouse.item.Item;
import ua.ukma.warehouse.item.ItemRepository;

@Component
public class ItemTransactions {
    @Autowired private ItemRepository itemRepo;
    @Autowired private TransactionTemplate transactionTemplate;
    @PersistenceContext private EntityManager em;

    @Transactional
    public void transactionalSave(boolean fail) {
        itemRepo.save(new Item(null, "TransactionalItem", 5, 10.0, null));
        if (fail) throw new RuntimeException("Rollback inside @Transactional");
    }

    public void templateSave(boolean fail) {
        transactionTemplate.executeWithoutResult(status -> {
            itemRepo.save(new Item(null, "TemplateItem", 3, 5.0, null));
            if (fail) throw new RuntimeException("Rollback inside TransactionTemplate");
        });
    }

    @Transactional
    public void entityManagerSave(boolean fail) {
        Item item = new Item(null, "EntityManagerItem", 2, 3.0, null);
        em.persist(item);
        if (fail) throw new RuntimeException("Rollback inside EntityManager");
    }
}
