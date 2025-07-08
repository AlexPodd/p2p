package com.example.p2p.resourceServer.service;


import com.example.p2p.resourceServer.dto.CheckDTO;
import com.example.p2p.resourceServer.model.CheckAbstract;

import java.util.ArrayList;


public interface CheckService {
    void createCheck(CheckDTO check) throws Exception;
    void closeCheck(long id) throws Exception;
    String getBalance(long id) throws Exception;

    void isUsersCheck(long checkID);
    ArrayList<Long> getAllCheck();
}
