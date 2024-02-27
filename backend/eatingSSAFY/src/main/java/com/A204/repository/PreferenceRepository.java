package com.A204.repository;

import com.A204.domain.ILikeCntByFoodId;
import com.A204.domain.IPreferenceNoti;
import com.A204.domain.Preference;
import com.A204.domain.PreferenceId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface PreferenceRepository extends JpaRepository<Preference, PreferenceId> {

    @Query(
            value = "delete\n" +
                    "from preference p\n" +
                    "where p.food_id = :foodId and p.user_id = :userId",
            nativeQuery = true
    )
    @Modifying
    void deleteById(@Param("userId") Long userId, @Param("foodId") Integer foodId);

    Page<Preference> findByUserIdAndValue(Long userId, Boolean value, Pageable pageable);

    @Query(value = "select \n" +
            "\tp.food_id as foodId, \n" +
            "\tcount(case when p.value = 1 then 1 end) as likeCnt,\n" +
            "\tcount(case when p.value = 0 then 1 end) as dislikeCnt\n" +
            "from preference p \n" +
            "where p.food_id in :foodIds group by p.food_id",
            nativeQuery = true)
    List<ILikeCntByFoodId> findCountByFoodIdIn(@Param("foodIds") List<Integer> foodIds);

    List<Preference> findByUserIdAndFoodIdIn(Long userId, List<Integer> foodIds);

    @Query(
            value = "select" +
                    "\tp.food_id as foodId,\n" +
                    "\tp.user_id as userId,\n" +
                    "\tf.content as content,\n" +
                    "\tn.app_token as appToken\n" +
                    "from preference p\n" +
                    "inner join food f on p.food_id = f.id\n" +
                    "inner join menu_food mf on mf.food_id = f.id\n" +
                    "inner join menu m on m.id = mf.menu_id\n" +
                    "inner join notification n on n.user_id = p.user_id\n" +
                    "where p.user_id in :userIds\n" +
                    "and m.serving_at = :targetDate\n",
            nativeQuery = true
    )
    List<IPreferenceNoti> findByTargetDateAndUserIdIn(@Param("targetDate") Date targetDate, @Param("userIds") List<Long> userIds);
}
