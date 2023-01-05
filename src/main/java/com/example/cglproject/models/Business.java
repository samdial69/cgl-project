package com.example.cglproject.models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@Table(name="business")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Business {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private String title;
    private LocalDate createdAt = LocalDate.now();

    @Column(nullable = false)
    private Long IdBusinessProvider;

    //TODO add the rest of the fields like the provider
}
