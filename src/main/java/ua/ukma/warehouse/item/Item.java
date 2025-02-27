package ua.ukma.warehouse.item;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name="items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int quantity;
    private double price;
}