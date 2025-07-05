package com.example.p2p.resourceServer.repository;

import com.example.p2p.resourceServer.model.CheckAbstract;
import com.example.p2p.resourceServer.model.currency.Currency;

import java.util.ArrayList;

public interface CheckRepository {
    void saveCheck(long id, String username, Currency currency) throws Exception;
    void deleteCheckByID(long id) throws Exception;
    CheckAbstract getCheckByID(long id, CheckAbstract check) throws Exception;
    String getNameByID(long id);
    ArrayList<Long> getAllCheck(String username);
}
