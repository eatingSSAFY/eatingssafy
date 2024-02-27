package com.A204.repository;

import com.A204.domain.IMenuFoodContent;
import com.A204.domain.MenuFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface MenuFoodRepository extends JpaRepository<MenuFood, Integer> {
    @Query(
            value = "select \n" +
                    "\tmf.id id,\n" +
                    "\tmf.food_id foodId,\n" +
                    "\tf.content content,\n" +
                    "\tm.serving_at servingAt,\n" +
                    "\tm.category_code categoryCode\n" +
                    "from menu_food mf \n" +
                    "inner join food f on f.id = mf.food_id\n" +
                    "inner join menu m on m.id = mf.menu_id \n" +
                    "where mf.id in (\n" +
                    "\tselect \t\n" +
                    "\t\tmin(mf.id)\n" +
                    "\tfrom menu_food mf\n" +
                    "\tgroup by mf.menu_id\n" +
                    ") and m.serving_at between :start and :end",
            nativeQuery = true
    )
    List<IMenuFoodContent> findTopFoodContent(@Param("start") Date start, @Param("end") Date end);
}
