package com.A204.domain;

import java.sql.Date;

public interface IMenuFoodContent {
    Integer getId();
    Integer getFoodId();
    String getContent();
    String getCategoryCode();
    Date getServingAt();
}
