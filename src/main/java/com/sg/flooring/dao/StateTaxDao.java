package com.sg.flooring.dao;

import com.sg.flooring.dto.StateTax;

import java.util.List;

public interface StateTaxDao {
    StateTax getStateTax(String stateAbbreviation);
    List<StateTax> getAllStateTaxes();
}
