package ua.ukma.warehouse.settings;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;
import ua.ukma.warehouse.item.Item;

@Component
public class ItemEntityManagerService {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Створює новий Item (переводить у стан MANAGED)
     */
    @Transactional
    public void persistItem(Item item) {
        entityManager.persist(item);
    }

    /**
     * Пошук Item за його ідентифікатором
     */
    public Item findItem(Long id) {
        return entityManager.find(Item.class, id);
    }

    /**
     * Видалення Item
     */
    @Transactional
    public void removeItem(Item item) {
        entityManager.remove(item);
    }

    /**
     * Оновлення (merge) Item, який є DETACHED
     */
    @Transactional
    public Item mergeItem(Item item) {
        return entityManager.merge(item);
    }

    /**
     * Переведення Item у стан DETACHED
     */
    @Transactional
    public void detachItem(Item item) {
        entityManager.detach(item);
    }

    /**
     * Оновлення (refresh) даних Item з БД
     */
    @Transactional
    public void refreshItem(Item item) {
        entityManager.refresh(item);
    }
}
