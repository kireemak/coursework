package by.kireenko.coursework.CarBooking.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", unique=true, nullable=false)
    private String name;
}
