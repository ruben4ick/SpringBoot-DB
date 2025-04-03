package ua.ukma.warehouse.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ItemRedisTemplateService {

    private final RedisTemplate<Object,Object> redisTemplate;
    private static final String HASH_KEY = "Item";

    public void save(ItemCache item) {
        redisTemplate.opsForHash().put(HASH_KEY, item.getId(), item);
    }

    public List<ItemCache> findAll() {
        return redisTemplate.<String, ItemCache>opsForHash()
                .values(HASH_KEY)
                .stream()
                .map(item -> (ItemCache) item)
                .toList();
    }

    public void deleteById(String id) {
        redisTemplate.opsForHash().delete(HASH_KEY, id);
    }
}
