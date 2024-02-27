package com.A204.domain;


import com.A204.code.CategoryCode;
import lombok.*;

import java.io.Serializable;
import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AmountNotificationId implements Serializable {
    @EqualsAndHashCode.Include
    private Date servingAt;
    @EqualsAndHashCode.Include
    private Enum<CategoryCode> categoryCode;
}
