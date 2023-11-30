package project.smoothsaver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.smoothsaver.entity.ShoppingCart;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Integer> {

}
