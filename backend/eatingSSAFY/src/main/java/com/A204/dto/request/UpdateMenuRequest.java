package com.A204.dto.request;

import java.sql.Date;

public record UpdateMenuRequest(
        Date servingAt,
        String content,
        String menuCategoryName
) {
}
