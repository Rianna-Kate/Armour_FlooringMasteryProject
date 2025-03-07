package com.sg.flooring.dto;

import java.math.BigDecimal;
import java.util.Objects;

public class StateTax {
    private String stateName;
    private String stateAbbreviation;
    private BigDecimal taxRate;

    public StateTax(String stateAbbreviation, String stateName, String taxRate) {
        this.stateName = stateName;
        this.stateAbbreviation = stateAbbreviation;
        this.taxRate = new BigDecimal(taxRate);
    }

    public String getStateName() {
        return stateName;
    }

    public String getStateAbbreviation() {
        return stateAbbreviation;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    @Override
    public String toString() {
        return stateAbbreviation + ", " +
                stateName;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        StateTax stateTax = (StateTax) o;
        return Objects.equals(stateName, stateTax.stateName) && Objects.equals(stateAbbreviation, stateTax.stateAbbreviation) && Objects.equals(taxRate, stateTax.taxRate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stateName, stateAbbreviation, taxRate);
    }
}
