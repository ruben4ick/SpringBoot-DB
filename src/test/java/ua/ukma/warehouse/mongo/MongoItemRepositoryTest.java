package ua.ukma.warehouse.mongo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class MongoItemRepositoryTest {

    @Autowired
    private MongoItemRepository repository;

    @BeforeEach
    void setup() {
        repository.deleteAll();
        repository.save(new MongoItem(null, "Chair", 10, 50.0, "Furniture"));
        repository.save(new MongoItem(null, "Table", 20, 120.0, "Furniture"));
        repository.save(new MongoItem(null, "Monitor", 5, 200.0, "Electronics"));
    }

    @Test
    void testFindByName() {
        List<MongoItem> result = repository.findByName("Chair");
        assertEquals(1, result.size());
    }

    @Test
    void testFindByCategoryName() {
        List<MongoItem> result = repository.findByCategoryName("Furniture");
        assertEquals(2, result.size());
    }

    @Test
    void testFindItemsWithQuantityGreaterThan() {
        List<MongoItem> result = repository.findItemsWithQuantityGreaterThan(6);
        assertEquals(2, result.size());
    }

    @Test
    void testFindByNameRegex() {
        List<MongoItem> result = repository.findByNameRegex(".*able");
        assertEquals(1, result.size());
        assertEquals("Table", result.get(0).getName());
    }

    @Test
    void testFindItemsByPriceRange() {
        List<MongoItem> result = repository.findItemsByPriceRange(50, 150);
        assertEquals(2, result.size());
    }

    @Test
    void testFindByNameAndCategoryName() {
        List<MongoItem> result = repository.findByNameAndCategoryName("Monitor", "Electronics");
        assertEquals(1, result.size());
    }
}
