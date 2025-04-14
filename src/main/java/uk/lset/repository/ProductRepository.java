package uk.lset.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.lset.entities.Products;

import java.util.Optional;


@Repository
public interface ProductRepository extends JpaRepository<Products, String> {

    boolean existsByUpc(String upc);
    boolean existsByProductId(String productId);
    Page<Products> findByCategory(String category, Pageable pageable);

    Optional<Products> findByProductId(String productId);
    Optional<Products> findByUpc(String upc);


}
