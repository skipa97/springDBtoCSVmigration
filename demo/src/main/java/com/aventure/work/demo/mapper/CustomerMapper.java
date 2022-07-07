package com.aventure.work.demo.mapper;

import com.aventure.work.demo.model.Customer;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;


@Component
public class CustomerMapper implements RowMapper<Customer> {

    @Override
    public Customer mapRow(ResultSet rs, int i) throws SQLException{
        Customer c = new Customer();

        c.setCustomerID(rs.getInt("CustomerID"));
        c.setAccountNumber(rs.getString("AccountNumber"));
        c.setModifiedDate(rs.getDate("ModifiedDate"));
        c.setRowguid(rs.getString("rowguid"));
        c.setPersonID(rs.getInt("PersonID"));
        c.setStoreID(rs.getInt("StoreID"));
        c.setTerritoryID(rs.getInt("TerritoryID"));

        return c;
    }

}
