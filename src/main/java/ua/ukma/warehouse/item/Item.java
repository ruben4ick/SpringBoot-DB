package ua.ukma.warehouse.item;

import jakarta.persistence.*;
import lombok.*;
import ua.ukma.warehouse.category.Category;

@Entity
@Data
@Table(
        name = "items",
        indexes = @Index(name = "itemNameIndex", columnList = "name", unique = true)
)
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int quantity;
    private double price;
    @ManyToOne
    private Category category;
}