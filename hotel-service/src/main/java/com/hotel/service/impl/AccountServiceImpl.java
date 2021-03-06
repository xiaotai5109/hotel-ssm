package com.hotel.service.impl;

import cn.hutool.core.date.DateField;
import com.hotel.mapper.AccountMapper;
import com.hotel.mapper.AccountRoleMapper;
import com.hotel.pojo.entity.Account;
import com.hotel.pojo.po.AccountPO;
import com.hotel.pojo.vo.ResponseVO;
import com.hotel.pojo.vo.CountVO;
import com.hotel.service.AccountService;
import com.hotel.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author az
 * @description
 * @date 2022/3/24 0024
 */
@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountMapper accountMapper;
    private final AccountRoleMapper accountRoleMapper;

    /**
     * 根据用户名查询用户信息 继承SpringSecurity中的UserDetailsService重写接口
     *
     * @param username 用户名
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //角色集合对象
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        //根据用户名查询用户详情
        Account account = accountMapper.selectAccountByName(username);
        //遍历角色列表 加入角色集合中
        account.getRoleList().forEach(role ->
                authorities.add(new SimpleGrantedAuthority(role.getRoleCode()))
        );
        //判断账户是否被禁用
        boolean enabled = account.getStatus() == 1;
        return new User(account.getLoginName(),
                account.getPassword(),
                enabled,
                true,
                true,
                true,
                authorities);
    }

    /**
     * 前台用户注册
     *
     * @param params 前台用户PO
     * @return
     */
    @Override
    public ResponseVO register(AccountPO params) {
        Account account = new Account();
        MyBeanUtils.copyProperties(params, account);
        //设置用户默认状态为启用状态
        account.setStatus(1);
        //设置注册时间
        account.setRegistTime(new Date());
        //加密密码
        account.setPassword(new BCryptPasswordEncoder().encode(params.getPassword()));
        boolean insert = accountMapper.insert(account);
        //新增成功则向新注册用户设置默认角色：1代表注册用户
        if (insert) {
            accountRoleMapper.insertAccountRoleRelation(account.getId(), 1);
        }
        return CheckUtils.checkSuccess(insert, "注册");
    }

    /**
     * 检查用户名是否已存在
     *
     * @param loginName 前台用户用户名
     * @return
     */
    @Override
    public ResponseVO checkAccountNameExist(String loginName) {
        Account account = accountMapper.selectAccountByName(loginName);
        if (account != null) {
            return ResponseUtils.exists("用户名已存在！");
        }
        return ResponseUtils.success("用户名可用", null);
    }

    /**
     * 获取前台用户数量及增长数量
     *
     * @return
     */
    @Override
    public CountVO getUserCount() {
        AccountPO accountPO = new AccountPO();

        //查询用户总数
        int allUserCount = accountMapper.getAccountCount(accountPO);

        //获取昨天的开始时间 -> yyyy-MM-dd 00:00:00
        Date yesterdayBegin = MyDateUtils.getYesterdayBegin();
        accountPO.setStartTime(yesterdayBegin);
        Date yesterdayEnd = MyDateUtils.getYesterdayEnd();
        accountPO.setEndTime(yesterdayEnd);
        //查询昨日增加的用户数量
        int yesterdayAdd = accountMapper.getAccountCount(accountPO);


        //获取七天前开始时间
        Date offsetEnd = MyDateUtils.get7daysEnd();
        accountPO.setEndTime(offsetEnd);
        //查询七天增加的用户数量
        int weekAdd = accountMapper.getAccountCount(accountPO);

        //返回数据
        return CountVO.builder()
                .allCount(String.valueOf(allUserCount))
                .yesterdayAdd(String.valueOf(yesterdayAdd))
                .weekAdd(String.valueOf(weekAdd))
                .build();
    }

}
