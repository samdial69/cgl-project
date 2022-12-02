package com.example.cglproject.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Parameter {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int levelOfSponsorship;
    private double percentageOfCommission;
    private int numberOfBusinessToBeAffiliated;
    private int numberOfMonthsAffiliation;

}
