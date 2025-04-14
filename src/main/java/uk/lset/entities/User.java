package uk.lset.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.sql.Date;

@Entity
@Table(name = "users")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter @Setter
public class User {

    @Id
    @UuidGenerator
    @Column(name = "user_id")
    private String userId;

    @Column(name = "username", unique = true)
    @NotBlank(message = "Username is required")
    private String username;

    @Column(name = "first_name")
    @NotBlank(message = "First name is required")
    private String firstName;

    @Column(name = "last_name")
    @NotBlank(message = "Last name is required")
    private String lastName;

    @Column(name = "date_of_birth", unique = true)
    @NotBlank(message = "Date of birth is required")
    private Date dateOfBirth;

    @Column(name = "email", unique = true)
    @NotBlank(message = "E-mail is required")
    @Email
    private String email;

    @Column(name = "password")
    @NotBlank(message = "Password is required")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    @NotBlank(message = "Role is required")
    private Role role;


    public User() {

    }
}



