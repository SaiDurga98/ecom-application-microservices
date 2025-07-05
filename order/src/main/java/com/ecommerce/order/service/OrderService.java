package com.ecommerce.order.service;

import com.ecommerce.order.model.Order;
import com.ecommerce.order.model.OrderItem;
import com.ecommerce.order.model.OrderStatus;
import com.ecommerce.order.dto.OrderItemDTO;
import com.ecommerce.order.dto.OrderResponse;
import com.ecommerce.order.model.*;
import com.ecommerce.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartService cartService;

    private final OrderRepository orderRepository;

    public Optional<OrderResponse> createOrder(String userId) {
        // Validate for cart Items
        List<CartItem> cartItems = cartService.getCartItems(userId);
        if(cartItems.isEmpty()){
            return Optional.empty();
        }
        // Validate the user

        // Calculate total price
        BigDecimal totalPrice = cartItems.stream()
                        .map(CartItem::getPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
        // Create order
        Order order = new Order();
        order.setUserId(userId);
        order.setTotalAmount(totalPrice);
        order.setStatus(OrderStatus.CONFIRMED);
        List<OrderItem> orderItems = cartItems.stream()
                        .map( cartItem -> new OrderItem(
                                null,
                                cartItem.getProductId(),
                                cartItem.getQuantity(),
                                cartItem.getPrice(),
                                order
                        )).toList();
        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);
        // Clear the cart
        cartService.clearCart(userId);

        return Optional.of(mapToOrderResponse(savedOrder));
    }

    private OrderResponse mapToOrderResponse(Order savedOrder) {
       return new OrderResponse(
               savedOrder.getId(),
               savedOrder.getTotalAmount(),
               savedOrder.getStatus(),
               savedOrder.getItems().stream()
                       .map(orderItem -> new OrderItemDTO(
                               orderItem.getId(),
                               orderItem.getProductId(),
                               orderItem.getQuantity(),
                               orderItem.getPrice(),
                               orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity()))
                       )).toList(),
               savedOrder.getCreatedAt()
       );
    }
}
