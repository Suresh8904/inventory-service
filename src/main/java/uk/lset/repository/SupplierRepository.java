package uk.lset.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uk.lset.entities.Supplier;

import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, String> {

    boolean existsBySupplierCode(String supplierCode);

    @Modifying
    @Transactional
    void deleteBySupplierCode(@Param("supplierCode") String supplierCode);


    Optional<Supplier> findBySupplierCode(@Param("supplierCode")String supplierCode);
}
