package ua.ukma.warehouse.mongo;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "shipments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MongoShipment {
    @Id
    private String id;
    private String itemName;
    private int quantity;
    private LocalDate arrivalDate;
}
