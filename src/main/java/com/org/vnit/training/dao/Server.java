package com.org.vnit.training.dao;

import com.org.vnit.training.bean.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

@Repository("server")
public class Server {
    DataSource datasource;
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public void setDatasource(DataSource datasource) {
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(datasource);
    }

    public Integer getBalance(Integer mobileNumber) {
        String qry = "Select balance from person where mobile_Number = :unumber  ";
        SqlParameterSource paramSource = new MapSqlParameterSource("unumber", mobileNumber);
        return namedParameterJdbcTemplate.queryForObject(qry, paramSource, Integer.class);
    }

    public Person addPerson() {
        Scanner scan = new Scanner(System.in);
        Integer mobileNumber = null;
        while (mobileNumber == null) {
            System.out.println("Mobile Number (Mandatory)");
            mobileNumber = scan.nextInt();
        }
        System.out.println(mobileNumber);

        scan = new Scanner(System.in);
        System.out.println("Enter Name");
        String name = scan.nextLine();
        System.out.println(name);
        String qry = "Insert into person (Mobile_Number, Name) values (:unumber , :uname)";
        SqlParameterSource paramSource = new MapSqlParameterSource("unumber", mobileNumber)
                .addValue("uname", name);
        namedParameterJdbcTemplate.update(qry, paramSource);
        return new Person(mobileNumber);
    }

    public List<Transaction> transactionHistory(Integer mobileNumber) {
        String qry = "Select * from Transactions where mobile_number = :unumber ";
        SqlParameterSource paramSource = new MapSqlParameterSource("unumber", mobileNumber);
        return namedParameterJdbcTemplate.query(qry, paramSource, new TransactionMapper());
    }

    public void addTransaction(Integer mobileNumber, String details, Integer balance) {
        System.out.println("Inside addTransaction");
        String qry = "Insert into transactions ( Mobile_Number, Details, Balance) values(:unumber, :udetails, :ubalance) ";
        SqlParameterSource paramSource = new MapSqlParameterSource("unumber", mobileNumber)
                .addValue("udetails", details)
                .addValue("ubalance", balance);
        namedParameterJdbcTemplate.update(qry, paramSource);
    }

    public Boolean verifyUser(Integer mobileNumber) {
        String qry = "Select balance from person where mobile_Number = :unumber  ";
        SqlParameterSource paramSource = new MapSqlParameterSource("unumber", mobileNumber);
        return namedParameterJdbcTemplate.query(qry, paramSource, new ResultSetExtractor<Boolean>() {
            @Override
            public Boolean extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                if (resultSet.next()) {
                    return true;
                }
                return false;
            }
        });
    }

    public void addMoney(Integer mobileNumber, Integer amount) {
        String qry = "Update Person Set Balance = Balance + :uamount where mobile_number = :unumber ";

        SqlParameterSource parameterSource = new MapSqlParameterSource("uamount", amount)
                .addValue("unumber", mobileNumber);

        namedParameterJdbcTemplate.update(qry, parameterSource);
        addTransaction(mobileNumber, "Money added", getBalance(mobileNumber));
    }

    public Person viewDetails(Integer mobileNumber) {
        String qry = "select * from person where mobile_number = :unumber";
        SqlParameterSource parameterSource = new MapSqlParameterSource("unumber", mobileNumber);
        return namedParameterJdbcTemplate.queryForObject(qry, parameterSource, new PersonMapper());

    }

    public void withdrawMoney(Integer mobileNumber, Integer amount) {
        Integer balance = getBalance(mobileNumber);
        if (balance >= amount) {
            String qry = "Update Person Set Balance = Balance - :uamount where mobile_number = :unumber";
            SqlParameterSource parameterSource = new MapSqlParameterSource("unumber", mobileNumber)
                    .addValue("uamount", amount);
            namedParameterJdbcTemplate.update(qry, parameterSource);
            addTransaction(mobileNumber, "Money Withdrawn", balance - amount);
        } else {
            System.out.println("Balance is lees than" + amount);
        }
    }

    public void changeName(Integer mobileNumber, String name) {
        String qry = "Update Person Set name = :uname where mobile_number = :unumber";
        SqlParameterSource parameterSource = new MapSqlParameterSource("uname", name)
                .addValue("unumber", mobileNumber);
        namedParameterJdbcTemplate.update(qry, parameterSource);
        addTransaction(mobileNumber, "Name Changed", getBalance(mobileNumber));
        System.out.println("Name changed");
    }
}

class TransactionMapper implements RowMapper<Transaction> {

    @Override
    public Transaction mapRow(ResultSet rs, int i) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setDetails(rs.getString("details"));
        transaction.setBalance(rs.getInt("balance"));
        transaction.setDateTime(rs.getObject("date_time", LocalDateTime.class));
        return transaction;
    }
}

class PersonMapper implements RowMapper<Person> {

    @Override
    public Person mapRow(ResultSet rs, int i) throws SQLException {
        Person person = new Person();
        person.setName(rs.getString("name"));
        person.setBalance(rs.getInt("balance"));
        person.setMobileNumber(rs.getInt("mobile_number"));
        return person;
    }
}
