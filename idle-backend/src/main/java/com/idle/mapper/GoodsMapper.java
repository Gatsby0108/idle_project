package com.idle.mapper;

import com.idle.entity.goods.Goods;
import com.idle.entity.user.AccountUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface GoodsMapper {

    @Select("select * from db_goods where id = #{id}")
    List<Goods> findGoodsById(Long id);

    @Insert("insert into db_goods (id, publisher_id, product_name, price, category, introduce, picture, create_time)" +
            " values (#{id}, #{publisher_id}, #{product_name}, #{price}, #{category}, #{introduce}," +
            " #{picture}, #{create_time})")
    int createGoods(Long id, Long publisher_id, String product_name, BigDecimal price, String category,
                    String introduce, String picture, String create_time);

    @Update("update db_goods set product_name = #{product_name}, price = #{price}, category = #{category}, introduce = " +
            "#{introduce}, picture = #{picture} where id = #{id}")
    int updateGoods(Long id,String product_name, BigDecimal price, String category, String introduce, String picture);

    @Update("update db_goods set status = #{status} where id = #{id}")
    int updateStatus(Integer status, Long id);

    @Select("select * from db_goods where status = 1")
    List<Goods> getAllGoods();

    @Select("select * from db_goods join db_account on db_account.id = db_goods.publisher_id where" +
            " db_goods.publisher_id = #{publisher_id}")
    List<Goods> getGoodsByPublisherId(Long publisher_id);

    @Select("select * from db_account where db_account.id = #{publisher_id}")
    AccountUser getUserByPublisherId(Long publisher_id);

    @Select("select * from db_goods where category = #{category}")
    List<Goods> getGoodsByCategory(String category);

    @Select("select * from db_goods where db_goods.product_name like CONCAT('%', #{search}, '%')" +
            "OR db_goods.introduce like CONCAT('%', #{search}, '%')")
    List<Goods> getGoodsByProductNameOrIntroduce(String search);
    @Select("select price from db_goods where id = #{id}")
    String getPriceById(Long id);
}
