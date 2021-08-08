package t5.bookhive.backendapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import t5.bookhive.backendapi.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Integer> {
}
