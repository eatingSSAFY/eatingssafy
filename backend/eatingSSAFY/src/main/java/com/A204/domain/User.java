package com.A204.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User {
    @Id
    @Column(name = "kakao_id", updatable = false, unique = true)
    private Long id;

    @Column(name = "person_nickname", nullable = false)
    private String personNickname;

    @OneToMany(
            mappedBy = "user",
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    private List<Preference> preferenceList = new ArrayList<>();

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    public static User generate(User user, Optional<User> saved) {
        if (saved.isPresent()) {
            User newUser = saved.get();
            newUser.setCreatedAt(user.getCreatedAt());
            if (user.getPersonNickname() != null)
                newUser.setPersonNickname(user.getPersonNickname());
            return newUser;
        }
        return user;
    }
}
