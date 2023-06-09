package com.idle.mapper;

import com.idle.entity.auth.Account;
import com.idle.entity.user.AccountUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {
    @Select("select * from db_account where username = #{text} or nickname = #{text} or email = #{text}")
    Account findAccountByNameOrEmail(String text);

    @Select("select * from db_account where username = #{text} or nickname = #{text} or email = #{text}")
    AccountUser findAccountUserByNameOrEmail(String text);
    @Insert("insert into db_account (email, username, password) values (#{email}, #{username}, #{password})")
    int createAccount(String username, String password, String email);

    @Update("update db_account set nickname = #{nickname}, email = #{email}, address = #{address} where username = #{username}")
    int upgradeInfo(String nickname, String email, String address, String username);

    @Update("update db_account set avatar = #{avatar} where id = #{id}")
    int upgradeAvatar(String avatar, Long id);

    @Update("update db_account set address = #{address} where id = #{id}")
    int upgradeAddress(String address, Long id);

    @Update("update db_account set password = #{password} where email = #{email}")
    int resetPasswordByEmail(String password, String email);
}
