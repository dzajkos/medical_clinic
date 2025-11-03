package com.dzajkos.medical_clinic.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.dialect.PostgreSQLDialect;

import java.util.List;

@Table(name = "DOCTORS")
@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
@Builder
@Entity
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String specialization;
    @ManyToMany
    @JoinTable(
            name = "CLINICS_DOCTORS",
            joinColumns = @JoinColumn(name = "doctor_id"),
            inverseJoinColumns = @JoinColumn(name = "clinic_id"))
    private List<Clinic> clinics;
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Visit> visits;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Doctor other)) return false;

        return id != null &&
                id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", clinics=" + clinics.stream().map(Clinic::getId) +
                '}';
    }
}
