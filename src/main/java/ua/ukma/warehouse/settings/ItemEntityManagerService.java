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

    @Transactional
    public void persistItem(Item item) {
        entityManager.persist(item); // Item переходить у стан MANAGED
    }

    public Item findItem(Long id) {
        return entityManager.find(Item.class, id); // Отримує Item з БД
    }

    @Transactional
    public void removeItem(Item item) {
        entityManager.remove(item); // Видаляє Item
    }

    @Transactional
    public Item mergeItem(Item item) {
        return entityManager.merge(item); // Оновлює від'єднаний Item
    }

    @Transactional
    public void detachItem(Item item) {
        entityManager.detach(item); // Робить Item DETACHED
    }

    @Transactional
    public void refreshItem(Item item) {
        entityManager.refresh(item); // Оновлює дані Item з бази
    }
}
