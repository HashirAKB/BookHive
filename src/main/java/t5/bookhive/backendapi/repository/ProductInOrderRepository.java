package t5.bookhive.backendapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import t5.bookhive.backendapi.entity.ProductInOrder;


public interface ProductInOrderRepository extends JpaRepository<ProductInOrder, Long> {

}
