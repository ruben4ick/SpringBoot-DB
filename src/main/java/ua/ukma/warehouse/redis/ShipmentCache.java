package ua.ukma.warehouse.redis;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("Shipment")
public class ShipmentCache {
    @Id
    private String id;
    private String itemId;
    private int quantity;
    private LocalDate shipmentDate;
}
