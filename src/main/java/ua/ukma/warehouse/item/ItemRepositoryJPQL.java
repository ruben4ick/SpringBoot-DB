package ua.ukma.warehouse.item;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface ItemRepositoryJPQL extends JpaRepository<Item, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Item i SET i.price = :price WHERE i.name = :name")
    int updateItemPriceByName(String name, double price);

    @Modifying
    @Transactional
    @Query("DELETE FROM Item i where i.quantity < :minQuantity")
    public int deleteItemsWithQuantityLessThan(int minQuantity);

    @Query("SELECT AVG(i.price) FROM Item i GROUP BY i.category.id")
    public Double getAverageItemPriceByCategory();

    @Query("SELECT SUM(i.quantity) FROM Item i")
    Long getTotalItemQuantity();

    @Query("SELECT COUNT(i) FROM Item i")
    public Long getItemCount();
}
