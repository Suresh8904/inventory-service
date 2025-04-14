package uk.lset.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;


@Entity
@Table(name = "suppliers")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
public class Supplier {

    @Id
    @UuidGenerator
    @Column(name = "supplier_id")
    private String supplierId;

    @Column(name = "supplier_name")
    @NotBlank(message = "Supplier name is required")
    private String supplierName;

    @Column(name = "supplier_address")
    @NotBlank(message = "Supplier address is required")
    private String supplierAddress;

    @Column(name = "supplier_code", unique = true)
    @NotBlank(message = "Supplier code is required")
    private String supplierCode;

    @Column(name = "supplier_email")
    @NotBlank(message = "Supplier email is required")
    @Email(message = "Incorrect email")
    private String supplierEmail;

    @Column(name = "supplier_phone_number")
    @NotBlank(message = "Supplier phone number is required")
    private String supplierPhoneNumber;

}
