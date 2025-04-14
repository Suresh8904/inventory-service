package uk.lset.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import uk.lset.entities.Supplier;
import uk.lset.repository.ProductRepository;
import uk.lset.repository.SupplierRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SupplierService {


    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;

    @Autowired
    public SupplierService(SupplierRepository supplierRepository, ProductRepository productRepository) {
        this.supplierRepository = supplierRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Supplier addNewSupplier(Supplier supplier) {
        if(supplierRepository.existsBySupplierCode(supplier.getSupplierCode())){
            throw new RuntimeException("Supplier code already exists.");
        }
        return supplierRepository.save(supplier);
    }


    public List<Supplier> getAllSuppliers(){
        return supplierRepository.findAll();
    }

    //
    public Optional<Supplier> getSupplierBySupplierCode(String supplierCode) {
        if(!supplierRepository.existsBySupplierCode(supplierCode)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Supplier code " + supplierCode + " not found.");
        }
        return supplierRepository.findBySupplierCode(supplierCode);
    }

    //Delete supplier by supplierConde
    @Transactional
    public void deleteSupplierByCode(String supplierCode ) {
        if(!supplierRepository.existsBySupplierCode(supplierCode)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Supplier with supplier code " + supplierCode + " not found.");
        }
        supplierRepository.deleteBySupplierCode((supplierCode));
    }

    //Delete supplier by id
    @Transactional
    public void deleteSupplier(String id){
        if(!supplierRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Supplier with id " + id + "not found.");
        }
        productRepository.deleteById(id);
    }
}
