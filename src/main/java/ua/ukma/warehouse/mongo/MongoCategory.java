package ua.ukma.warehouse.mongo;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MongoCategory {
    @Id
    private String id;
    private String name;
}
