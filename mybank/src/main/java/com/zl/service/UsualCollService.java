package com.zl.service;

import com.zl.pojo.UsualColl;
import com.zl.pojo.UFenYe;

import java.util.List;

public interface UsualCollService {
    //根据收款人账号删除收款人
    public int deleteUsualColl(String accIn);
    //添加收款人
    int addUsualColl(UsualColl usualColl);
//    //查询全部的收款人
//    List<UsualColl> queryAllUsualColl(String accNo);
    UsualColl queryUsualCollByaccIn(String accIn);
    //模糊分页查询收款人
    List<UsualColl> queryUsualCollByFy(UFenYe ufenye);
    //查询符合要求的总数
    //int queryUsualCount(Uquery uquery);
//    UsualColl queryUsualColl(String accIn);
//
//    /**
//     * 根据卡号查用户
//     */
//    int queryUserByAccNo(UsualColl usualColl);

    List<UsualColl> queryUsualColl(String mainAcc);

    /**
     * 查询常用收款人是否已存在
     * @return
     */
    public int queryUsualByAccIn(String mainAcc,String accIn);
}
