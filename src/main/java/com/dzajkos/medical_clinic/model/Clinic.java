package com.dzajkos.medical_clinic.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Table(name = "CLINICS")
@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
@Builder
@Entity
public class Clinic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String city;
    private String postalCode;
    private String street;
    private String buildingNo;
    @ManyToMany(mappedBy = "clinics")
    private List<Doctor> doctors;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Clinic other)) return false;

        return id != null &&
                id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Clinic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", street='" + street + '\'' +
                ", buildingNo='" + buildingNo + '\'' +
                ", doctors=" + doctors.stream().map(Doctor::getId) +
                '}';
    }
}
