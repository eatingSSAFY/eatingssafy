package com.A204.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "preference")
@IdClass(PreferenceId.class)
public class Preference {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false, insertable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id", updatable = false, insertable = false)
    private Food food;

    @Column(name = "value")
    private Boolean value;

    // insert 위해 추가적인 select 쿼리가 필요 없도록
    @Id
    @Column(name = "user_id")
    private Long userId;

    // insert 위해 추가적인 select 쿼리가 필요 없도록
    @Id
    @Column(name = "food_id")
    private Integer foodId;
}
