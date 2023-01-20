package com.example.cglproject.models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

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

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name= "id_business_provider")
    private BusinessProvider provider;

    @OneToMany(mappedBy="business", fetch=FetchType.LAZY)
    private List<Commission> commissions;

    public double getGlobalCommission() {
        double result = 0;
        for (Commission commission : this.commissions) {
            result += commission.getCommission();
        }
        return result;
    }

    public double getProviderCommission() {
        return this.getCommissionOf(this.provider);
    }

    public double getCommissionOf(BusinessProvider recipient) {
        for (Commission commission : this.commissions) {
            if (commission.getRecipient() == recipient) { // same object
                return commission.getCommission();
            }
        }
        return 0;
    }
}
