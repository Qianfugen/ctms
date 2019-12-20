package com.zl.dao;

import com.zl.pojo.*;

import java.util.List;


public interface UsualCollDao {
    //根据收款人账号删除收款人
    public int deleteUsualColl(String accIn);
    //添加收款人
    int addUsualColl(UsualColl usualColl);
//    //查询全部的收款人
//    List<UsualColl> queryAllUsualColl(String accNO);
    //分页查询
    List<UsualColl> queryUsualCollByFy(UFenYe ufenye);
    //查找符合要求的总数
    int queryUsualCount(Uquery uquery);
    //查询单个收款人
    UsualColl queryUsualCollByaccIn(String accIn);
//    /**
//     * （注册）修改用户登录密码为加密密码
//     */
//    int updateUserPwd(User user);
//
//    /**
//     * @param usualColl
//     * 根据卡号查用户
//     */
//    int queryUserByAccNo(UsualColl usualColl);
//    List<UsualColl> queryOne(String mainAcc);
    List<UsualColl> queryUsualColl(String mainAcc);

    /**
     * 查询常用收款人是否已存在
     * @return
     */
    public int queryUsualByAccIn(UsualColl usualColl);



}
