package com.ecommerce.order.service;


import com.ecommerce.order.dto.CartItemRequest;
import com.ecommerce.order.model.CartItem;
import com.ecommerce.order.repository.CartItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class CartService {
    private final CartItemRepository cartItemRepository;


    public boolean addToCart(String userId, CartItemRequest request) {
        //Checking id product exists
       /* Optional<Product> productObject = productRepository.findById(request.getProductId());
        if(productObject.isEmpty())
            return false;

        Product product = productObject.get();
        //Checking stock
        if(product.getStockQuantity() < request.getQuantity())
            return false;
        //Checking user
        Optional<User> userObject = userRepository.findById(Long.valueOf(userId));
        if(userObject.isEmpty())
            return false;

        User user = userObject.get();*/
        // Checking is user already has cart item in existing cart
        CartItem existingCartItem = cartItemRepository.findByUserIdAndProductId(userId, request.getProductId());
        if(existingCartItem != null){
            // update the quantity
            existingCartItem.setQuantity(existingCartItem.getQuantity() + request.getQuantity());
            //existingCartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(existingCartItem.getQuantity())));
            existingCartItem.setPrice(BigDecimal.valueOf(1000));
            cartItemRepository.save(existingCartItem);
        } else {
            // Create a new cartItem
            CartItem cartItem = new CartItem();
            cartItem.setProductId(request.getProductId());
            cartItem.setUserId(userId);
            cartItem.setQuantity(request.getQuantity());
            //cartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
            cartItem.setPrice(BigDecimal.valueOf(1000));
            cartItemRepository.save(cartItem);
        }
        return true;
    }

    public Boolean deleteItemFromCart(String userId, String productId) {

        //Checking id product exists
       /* Optional<Product> productObject = productRepository.findById(productId);
        if (productObject.isEmpty())
            return false;*/

        //Checking user
       /* Optional<User> userObject = userRepository.findById(Long.valueOf(userId));
        if (userObject.isEmpty())
            return false;*/
        CartItem cartItem = cartItemRepository.findByUserIdAndProductId(userId,productId);

      if(cartItem != null){
          cartItemRepository.delete(cartItem);
          return true;
      }
      return false;

    }


    public List<CartItem> getCartItems(String userId) {
        return cartItemRepository.findByUserId(userId);
    }

    public void clearCart(String userId) {
        cartItemRepository.deleteByUserId(userId);
    }
}
