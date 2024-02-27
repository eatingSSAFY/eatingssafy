package com.A204.code;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum RestaurantCode {
    FLOOR_10("10층", 10),
    FLOOR_20("20층", 20);

    private final String title;
    private final int code;

    static public RestaurantCode getRestaurantCodeByTitle(String title) {
        for (RestaurantCode code : values()) {
            if (code.title.equals(title)) return code;
        }
        return null;
    }
}
