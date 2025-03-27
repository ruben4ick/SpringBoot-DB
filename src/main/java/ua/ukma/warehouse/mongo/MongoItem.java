package ua.ukma.warehouse.mongo;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MongoItem {
    @Id
    private String id;
    private String name;
    private int quantity;
    private double price;
    private String categoryName;
}
