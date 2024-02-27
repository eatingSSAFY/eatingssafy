package com.A204.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public enum CategoryCode {

    SERVED_AT("배식일", 0, null),
    LUNCH_BOX("도시락", 1, RestaurantCode.FLOOR_10),
    SANDWICH("샌드위치", 2, RestaurantCode.FLOOR_10),
    SALAD("샐러드", 3, RestaurantCode.FLOOR_10),
    A("한식", 4, RestaurantCode.FLOOR_20),
    B("일품", 5, RestaurantCode.FLOOR_20);

    private final String title;
    private final int code;
    private final RestaurantCode restaurantCode;

    static public List<CategoryCode> getFloor10List() {
        List<CategoryCode> list = new ArrayList<>();
        for (CategoryCode cc : values())
            if (cc.getRestaurantCode() != null && cc.getRestaurantCode().equals(RestaurantCode.FLOOR_10))
                list.add(cc);
        return list;
    }

    static public CategoryCode getByCode(int code) {
        for (CategoryCode cc : values()) {
            if (cc.code == code) return cc;
        }
        return null;
    }

    static public CategoryCode getByTitle(String title) {
        for (CategoryCode cc : values()) {
            if (cc.title.equals(title)) return cc;
        }
        return null;
    }
}
