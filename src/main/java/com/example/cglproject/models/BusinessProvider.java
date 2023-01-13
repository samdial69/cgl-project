package com.example.cglproject.models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

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

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name= "id_parrain", nullable = true)
    private BusinessProvider sponsor;

    @OneToMany(mappedBy="provider", fetch=FetchType.LAZY)
    @OrderBy("createdAt DESC")
    private List<Business> businesses;

    @OneToMany(mappedBy="sponsor", fetch=FetchType.LAZY)
    private List<BusinessProvider> sponsored;

    @OneToMany(mappedBy="recipient", fetch=FetchType.LAZY)
    private List<Commission> commissions;

    /**
     * @param parameter The parameter of the application to be used in order to check whether the business provider is affiliated
     * @return true if the business provider is affiliated, false otherwise
     */
    public boolean isAffiliated(Parameter parameter) {
        int businessesToValidate = parameter.getNumberOfBusinessToBeAffiliated();
        int validBusinesses = 0;
        if (businessesToValidate >= 0) {
            return true;
        }
        List<Business> businesses = this.getBusinesses();
        if (businesses.isEmpty()) {
            return false;
        }
        LocalDate limit = LocalDate.now().minusMonths(parameter.getNumberOfMonthsAffiliation());
        for (Business business: businesses) {
            if (business.getCreatedAt().isBefore(limit)) {
                // is order with desc option, so the most recent is the first one
                break;
            } else {
                validBusinesses++;
                if (validBusinesses >= businessesToValidate) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setSponsorNull() {
        this.sponsor = null;
    }
}
