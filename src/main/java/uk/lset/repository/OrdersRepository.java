package uk.lset.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.lset.entities.Orders;

public interface OrdersRepository extends JpaRepository<Orders, String> {

}
