package uk.lset.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import uk.lset.entities.Supplier;
import uk.lset.service.SupplierService;

import java.util.List;
import java.util.Optional;

@RestController
public class SupplierController {

    private final SupplierService supplierService ;

    @Autowired
    public SupplierController(SupplierService supplierService) {

        this.supplierService = supplierService;
    }

    //Add new supplier
    @PostMapping(path = "/supplier/addNewSupplier")
    public ResponseEntity<?> addNewSupplier(@RequestBody Supplier supplier) {
        try{
            return ResponseEntity.ok().body(supplierService.addNewSupplier(supplier));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error" + e.getMessage());
        }
    }


    //Get all suppliers
    @GetMapping(path = "/supplier/all")
    public @ResponseBody List<Supplier> getAllSuppliers() {
        return supplierService.getAllSuppliers();
    }

    //Get supplier by supplierCode
    @GetMapping(path = "/supplier/supplierBySupplierCode/{supplierCode}")
    public  Optional<Supplier> getSupplierBySupplierCode(@PathVariable String supplierCode) {
        Optional<Supplier> optionalSupplier = supplierService.getSupplierBySupplierCode(supplierCode);
        if(optionalSupplier.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Supplier with supplier code " + supplierCode + " not found.");
        }
        return supplierService.getSupplierBySupplierCode(supplierCode);
    }




    //Delete supplier by supplier code
    @DeleteMapping(path = "/supplier/deleteBySupplierCode/{supplierCode}")
    public ResponseEntity<?> deletesSupplier(@PathVariable String supplierCode) {
        try {
            supplierService.deleteSupplierByCode(supplierCode);
            return ResponseEntity.ok("Deleted Supplier with supplier code " + supplierCode);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error delete supplier with code " + supplierCode + " : " + e.getMessage());
        }
    }

    //Delete supplier by id
    @DeleteMapping(path = "/supplier/deleteBySupplierId/{id}")
    public ResponseEntity<?> deleteSupplierById(@PathVariable String id) {
        try{
            supplierService.deleteSupplier(id);
            return ResponseEntity.ok("Deleted Supplier with id " + id);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error delete supplier with id " + id + " : " + e.getMessage());
        }
    }

}
