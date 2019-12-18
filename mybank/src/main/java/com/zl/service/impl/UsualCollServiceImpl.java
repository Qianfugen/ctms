package com.zl.service.impl;

import com.zl.dao.UsualCollDao;
import com.zl.pojo.UsualColl;
import com.zl.pojo.uFenYe;
import com.zl.service.UsualCollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsualCollServiceImpl implements UsualCollService {
    @Autowired
    private UsualCollDao uc;
    @Override
    public int deleteUsualColl(String accIn) {
        return uc.deleteUsualColl(accIn);
    }

    @Override
    public int addUsualColl(UsualColl usualColl) {
        return uc.addUsualColl(usualColl);
    }

    @Override
    public List<UsualColl> queryAllUsualColl(String accNo) {
        return uc.queryAllUsualColl(accNo);
    }

    @Override
    public List<UsualColl> queryUsualCollByFy(uFenYe ufenye) {
        if(ufenye!=null){
            if(ufenye.getPage()<=1){
                ufenye.setPage(1);
            }
            if(ufenye.getRowStart()!=null&&ufenye.getRowStart()<=0){
                ufenye.setRowStart(0);
            }
            if(ufenye.getRowEnd()!=null&&ufenye.getRowEnd()<=1){
                ufenye.setRowEnd(4);
            }
        }else{
            ufenye =new uFenYe();
        }
        return uc.queryUsualCollByFy(ufenye);
    }

    @Override
    public UsualColl queryUsualColl(String accIn) {
        return  uc.queryUsualColl(accIn);
    }

    @Override
    public int queryUserByAccNo(UsualColl usualColl) {
        return uc.queryUserByAccNo(usualColl);
    }
}
