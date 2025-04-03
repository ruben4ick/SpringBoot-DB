package ua.ukma.warehouse.redis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class ItemRedisTemplateServiceTest {

    @Autowired
    private ItemRedisTemplateService redisService;

    @BeforeEach
    void setup() {
    }

    @Test
    void testSaveAndFindAll() {
        ItemCache item1 = new ItemCache("1", "Hammer", 50, 12.5, new CategoryCache("1", "Tools"));
        ItemCache item2 = new ItemCache("2", "Wrench", 20, 9.99, new CategoryCache("1", "Tools"));

        redisService.save(item1);
        redisService.save(item2);

        List<ItemCache> all = redisService.findAll();
        assertThat(all).hasSize(2);
    }

    @Test
    void testDeleteById() {
        ItemCache item = new ItemCache("3", "Screwdriver", 10, 4.99, new CategoryCache("1", "Tools"));
        redisService.save(item);

        redisService.deleteById("3");

        List<ItemCache> all = redisService.findAll();
        assertThat(all).doesNotContain(item);
    }
}
