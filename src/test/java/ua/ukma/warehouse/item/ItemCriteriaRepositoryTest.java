package ua.ukma.warehouse.item;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import ua.ukma.warehouse.category.Category;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@Import(ItemCriteriaRepository.class)
@EntityScan(basePackages = "ua.ukma.warehouse")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ItemCriteriaRepositoryTest {

    @Autowired
    private ItemCriteriaRepository criteriaRepository;

    @Autowired
    private EntityManager entityManager;

    private Category electronics;

    @BeforeEach
    void setup() {
        electronics = new Category(null, "Electronics");
        entityManager.persist(electronics);

        entityManager.persist(new Item(null, "Laptop", 10, 1000.0, electronics));
        entityManager.persist(new Item(null, "Phone", 3, 500.0, electronics));
        entityManager.persist(new Item(null, "Headphones", 7, 150.0, null));
        entityManager.flush();
    }

    @Test
    void testFindByExactName() {
        List<Item> items = criteriaRepository.findByExactName("Laptop");
        assertEquals(1, items.size());
        assertEquals("Laptop", items.get(0).getName());
    }

    @Test
    void testFindAllSortedByPrice() {
        List<Item> items = criteriaRepository.findAllSortedByPrice();
        assertEquals(3, items.size());
        assertTrue(items.get(0).getPrice() <= items.get(1).getPrice());
        assertTrue(items.get(1).getPrice() <= items.get(2).getPrice());
    }

    @Test
    void testFindByNameAndMinQuantity() {
        List<Item> items = criteriaRepository.findByNameAndMinQuantity("Laptop", 5);
        assertEquals(1, items.size());
    }

    @Test
    void testFindByCategoryName() {
        List<Item> items = criteriaRepository.findByCategoryName("Electronics");
        assertEquals(2, items.size());
        assertTrue(items.stream().allMatch(item -> item.getCategory() != null && "Electronics".equals(item.getCategory().getName())));
    }

    @Test
    void testFindByCategoryName_NoMatch() {
        List<Item> items = criteriaRepository.findByCategoryName("Food");
        assertTrue(items.isEmpty());
    }
}