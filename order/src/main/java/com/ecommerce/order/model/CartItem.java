package com.ecommerce.order.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity(name = "CART_TABLE")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*@ManyToOne // cart can have many cartItems
    @JoinColumn(name = "user_id" , nullable = false)
    private User user;
    @ManyToOne // cart can have many products
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;*/

    private String userId;
    private String productId;

    private Integer quantity;
    private BigDecimal price;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;



}
