package com.zl.service;

import com.zl.pojo.UsualColl;
import com.zl.pojo.uFenYe;

import java.util.List;

public interface UsualCollService {
    //根据收款人账号删除收款人
    public int deleteUsualColl(String accIn);
    //添加收款人
    int addUsualColl(UsualColl usualColl);
    //查询全部的收款人
    List<UsualColl> queryAllUsualColl(String accNo);
    //模糊分页查询收款人
    List<UsualColl> queryUsualCollByFy(uFenYe ufenye);
    //查询符合要求的总数
    //int queryUsualCount(Query query);
    UsualColl queryUsualColl(String accIn);

    /**
     * 根据卡号查用户
     */
    int queryUserByAccNo(UsualColl usualColl);
}
