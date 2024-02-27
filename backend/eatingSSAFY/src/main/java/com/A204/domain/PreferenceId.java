package com.A204.domain;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PreferenceId implements Serializable {
    @EqualsAndHashCode.Include
    private Long userId;

    @EqualsAndHashCode.Include
    private Integer foodId;
}
