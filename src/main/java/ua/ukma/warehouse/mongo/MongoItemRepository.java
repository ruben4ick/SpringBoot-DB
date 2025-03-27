package ua.ukma.warehouse.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface MongoItemRepository extends MongoRepository<MongoItem, String> {

    List<MongoItem> findByName(String name);
    List<MongoItem> findByCategoryName(String categoryName);
    List<MongoItem> findByNameAndCategoryName(String name, String categoryName);

    @Query("{ 'quantity' : { $gt: ?0 } }")
    List<MongoItem> findItemsWithQuantityGreaterThan(int quantity);

    @Query("{ 'name' : { $regex: ?0 } }")
    List<MongoItem> findByNameRegex(String pattern);

    @Query("{ $and: [ { 'price' : { $gte: ?0 } }, { 'price' : { $lte: ?1 } } ] }")
    List<MongoItem> findItemsByPriceRange(double min, double max);
}
