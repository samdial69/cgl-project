package com.example.cglproject.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private String firstname;
    private String lastname;
    private Long idParrain;

    private boolean isSponsored = false;

    //TODO add the rest of the fields like the list of businesses and the id of BusinessProvider
}
