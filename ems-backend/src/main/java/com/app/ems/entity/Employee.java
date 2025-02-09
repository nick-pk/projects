package com.app.ems.entity;

import com.app.ems.util.Constant;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = Constant.FIRSTNAME_BLANK)
    private String firstName;
    private String lastName;
    @Column(unique = true, nullable = false)
    @NotBlank(message = Constant.EMAIL_BLANK)
    private String email;
}
