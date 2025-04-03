package ua.ukma.warehouse.redis;

import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface ItemRedisRepository extends CrudRepository<ItemCache, String> {
    List<ItemCache> findByName(String name);
    List<ItemCache> findByQuantity(int quantity);
    List<ItemCache> findByPrice(double price);

}
