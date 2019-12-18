package com.zl.dao;

import com.zl.pojo.Query;
import com.zl.pojo.User;
import com.zl.pojo.UsualColl;
import com.zl.pojo.uFenYe;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UsualCollDao {
    //根据收款人账号删除收款人
    public int deleteUsualColl(String accIn);
    //添加收款人
    int addUsualColl(UsualColl usualColl);
    //查询全部的收款人
    List<UsualColl> queryAllUsualColl(String accNO);
    //分页查询
    List<UsualColl> queryUsualCollByFy(uFenYe ufenye);
    //查找符合要求的总数
    int queryUsualCount(Query query);
    //查询单个收款人
    UsualColl queryUsualColl(String accIn);
    /**
     * （注册）修改用户登录密码为加密密码
     */
    int updateUserPwd(User user);

    /**
     * @param usualColl
     * 根据卡号查用户
     */
    int queryUserByAccNo(UsualColl usualColl);


}
