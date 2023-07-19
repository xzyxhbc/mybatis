package pers.xz.mybatis.dao;

import org.apache.ibatis.annotations.*;
import pers.xz.mybatis.dto.GoodsDTO;
import pers.xz.mybatis.entity.Goods;

import java.util.List;

public interface GoodsDAO {
    @Select("select * from t_goods where current_price between #{min} and #{max} order by current_price limit 0,#{limit}")
    public List<Goods> selectByPriceRange(@Param("min") Float min, @Param("max") Float max, @Param("limit") Integer limit);

    @Insert("insert into t_goods(title, sub_title, original_cost, current_price, discount, is_free_delivery)\n" +
            "        VALUE (#{title}, #{subTitle}, #{originalCost}, #{currentPrice}, #{discount}, #{isFreeDelivery})")
    // <selectKey>
    @SelectKey(statement = "select last_insert_id()", before = false, keyProperty = "goodsId", resultType = Integer.class)
    public int insert(Goods goods);

    @Select("select * from t_goods")
    // <resultMap>
    @Results({
            // <id>
            @Result(column = "goods_id",property = "goodsId", id = true),
            // <result>
            @Result(column = "title",property = "title"),
            @Result(column = "current_price",property = "currentPrice")
    })
    public List<GoodsDTO> selectAll();

}
