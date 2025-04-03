package ua.ukma.warehouse.redis;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@RedisHash("Item")
public class ItemCache implements Serializable {
    @Id
    private String id;
    @Indexed
    private String name;
    @Indexed
    private int quantity;
    @Indexed
    private double price;

    private CategoryCache category;
}
