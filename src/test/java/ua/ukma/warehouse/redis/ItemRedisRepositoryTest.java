package ua.ukma.warehouse.redis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataRedisTest
@ActiveProfiles("test")
class ItemRedisRepositoryTest {

    @Autowired
    private ItemRedisRepository itemRedisRepository;

    private final ItemCache item1 = new ItemCache(
            "1",
            "Hammer",
            100,
            15.0,
            new CategoryCache("1", "Tools")
    );

    private final ItemCache item2 = new ItemCache(
            "2",
            "Screwdriver",
            20,
            1235.0,
            new CategoryCache("1", "Tools")
    );

    private final ItemCache item3 = new ItemCache(
            "3",
            "Hamburger",
            20,
            54123.0,
            new CategoryCache("1", "Tools")
    );

    @BeforeEach
    void setUp() {
        itemRedisRepository.save(item1);
        itemRedisRepository.save(item2);
        itemRedisRepository.save(item3);
    }

    @Test
    void testFindByName() {
        List<ItemCache> result = itemRedisRepository.findByName("Hammer");
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
    }

    @Test
    void testFindByQuantity() {
        List<ItemCache> result = itemRedisRepository.findByQuantity(20);
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
    }

    @Test
    void testFindByPrice() {
        List<ItemCache> result = itemRedisRepository.findByPrice(54123.0);
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
    }
}
