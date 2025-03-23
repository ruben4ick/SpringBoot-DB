package ua.ukma.warehouse.item;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ua.ukma.warehouse.category.Category;

import java.util.List;

@Repository
public class ItemCriteriaRepository {
    private final EntityManager entityManager;

    @Autowired
    public ItemCriteriaRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Item> findByExactName(String name) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Item> query = cb.createQuery(Item.class);
        Root<Item> root = query.from(Item.class);

        query.select(root).where(cb.equal(root.get("name"), name));
        return entityManager.createQuery(query).getResultList();
    }

    public List<Item> findByCategoryName(String categoryName) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Item> query = cb.createQuery(Item.class);
        Root<Item> root = query.from(Item.class);
        Join<Item, Category> categoryJoin = root.join("category");

        query.select(root).where(cb.equal(categoryJoin.get("name"), categoryName));
        return entityManager.createQuery(query).getResultList();
    }

    public List<Item> findAllSortedByPrice() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Item> query = cb.createQuery(Item.class);
        Root<Item> root = query.from(Item.class);

        query.select(root).orderBy(cb.asc(root.get("price")));
        return entityManager.createQuery(query).getResultList();
    }

    public List<Item> findByNameAndMinQuantity(String name, int minQuantity) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Item> query = cb.createQuery(Item.class);
        Root<Item> root = query.from(Item.class);

        Predicate namePredicate = cb.equal(root.get("name"), name);
        Predicate quantityPredicate = cb.greaterThanOrEqualTo(root.get("quantity"), minQuantity);

        query.select(root).where(cb.and(namePredicate, quantityPredicate));
        return entityManager.createQuery(query).getResultList();
    }
}

