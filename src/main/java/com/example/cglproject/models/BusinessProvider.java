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
        if (businessesToValidate <= 0) {
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

    public double getLastCommissionsSum(int months) {
        double sum = 0;
        if (months <= 0 ) {
            return sum;
        }
        LocalDate limit = LocalDate.now().minusMonths(months);
        for (Commission commission : this.commissions) {
            if (commission.getBusiness().getCreatedAt().isBefore(limit)) {
                break;
            }
            sum += commission.getCommission();
        }
        return sum;
    }

    /**
     *
     * @param month : 0 is the current month, 1 is the previous month, etc
     * @return The sum of the commissions (direct and indirect) received during that month
     */
    public double getPastMonthCommissionsSum(int month) {
        double sum = 0;
        if (month < 0 ) {
            return sum;
        }
        LocalDate mostOldLimit = LocalDate.now().withDayOfMonth(1).minusMonths(month);
        LocalDate mostRecentLimit = mostOldLimit.withDayOfMonth(mostOldLimit.lengthOfMonth());
        for (Commission commission : this.commissions) {
            if (commission.getBusiness().getCreatedAt().isBefore(mostOldLimit)) {
                break;
            } else if (commission.getBusiness().getCreatedAt().isBefore(mostRecentLimit)) {
                sum += commission.getCommission();
            } // if it is after mostRecentLimit, it should continue to get to the corresponding date
        }
        return sum;
    }
}
