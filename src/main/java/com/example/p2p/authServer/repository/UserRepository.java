package com.example.p2p.authServer.repository;

import com.example.p2p.authServer.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User findById(Long id) {
        String query = "SELECT * FROM users WHERE id = ?";
        return jdbcTemplate.queryForObject(query, new UserRowMapper(), id);
    }

    public User findByName(String name) {
        String query = "SELECT * FROM users WHERE username = ?";
        return jdbcTemplate.queryForObject(query, new UserRowMapper(), name);
    }

    public boolean existsByName(String name) {
        String sql = "SELECT EXISTS(SELECT 1 FROM users WHERE username = ?)";
        Boolean exists = jdbcTemplate.queryForObject(sql, Boolean.class, name);
        return Boolean.TRUE.equals(exists);
    }



    public int saveUser(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("users").usingColumns("username", "hashed_password", "id");;
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("id", user.getId());
        parameters.put("username", user.getUsername());
        parameters.put("hashed_password", user.getPassword());
        return simpleJdbcInsert.execute(parameters);
    }


}

