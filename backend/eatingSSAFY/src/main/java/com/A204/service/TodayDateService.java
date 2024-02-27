package com.A204.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;

@Service
public class TodayDateService {

    /**
     * Today Date 정보 전송
     */
    @Transactional
    public String findTodayDate() {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
        return timeStamp;
    }
}
