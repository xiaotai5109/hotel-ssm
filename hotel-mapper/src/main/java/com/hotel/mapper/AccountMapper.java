package com.hotel.mapper;

import com.hotel.pojo.entity.Account;
import com.hotel.pojo.po.AccountPO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AccountMapper {
    /**
     * 根据id删除前台用户
     *
     * @param id 前台用户id
     * @return
     */
    boolean deleteByPrimaryKey(@Param("id") Long id);

    /**
     * 新增用户
     *
     * @param record 前台用户实体
     * @return
     */
    boolean insert(Account record);

    /**
     * 根据id查询前台用户详情
     *
     * @param id 前台用户id
     * @return
     */
    Account selectByPrimaryKey(@Param("id") Long id);

    /**
     * 查询所有前台用户
     *
     * @return
     */
    List<Account> selectAll();

    /**
     * 根据id修改前台用户信息
     *
     * @param record 前台用户实体
     * @return
     */
    boolean updateByPrimaryKey(Account record);

    /**
     * 检查用户名是否已存在
     *
     * @param loginName 用户名
     * @return
     */
    Account selectAccountByName(@Param("loginName") String loginName);

    /**
     * 根据用户名获取用户信息
     *
     * @param loginName 登录名
     * @return
     */
    @Select("SELECT * FROM t_account WHERE login_name = #{loginName}")
    Account getAccountByName(@Param("loginName") String loginName);

    /**
     * 获取所有用户数量
     *
     * @param accountPO 前台用户PO
     * @return
     */
    int getAccountCount(AccountPO accountPO);

}