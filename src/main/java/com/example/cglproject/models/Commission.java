package com.example.cglproject.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Commission {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private double commission;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name= "business_id")
    private Business business;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name= "recipient")
    private BusinessProvider recipient;
}
