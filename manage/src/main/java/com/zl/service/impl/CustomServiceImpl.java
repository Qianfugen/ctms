package com.zl.service.impl;

import com.zl.api.MyBankApi;
import com.zl.dao.ICustomDao;
import com.zl.pojo.*;
import com.zl.service.ICustomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author junqi
 */
@Service
public class CustomServiceImpl implements ICustomService {

    @Autowired
    private ICustomDao cd;
    @Autowired
    private MyBankApi ma;

    @Override
    public List<User> queryAllCustom(FenYe fenYe) {
        int flag=cd.queryByLike(fenYe.getQuery());
        if (flag<2){
            flag=1;
        }
        fenYe.setRowCount(flag);
        if (fenYe.getPage()!=null){
            if (fenYe.getPage()<1){
                fenYe.setPage(1);
            }else if (fenYe.getPage()>fenYe.getPageCount()){
                fenYe.setPage(fenYe.getPageCount());
            }
        }else {
            fenYe.setPage(1);
        }
        return cd.queryAllCustom(fenYe);
    }

    @Override
    public int updateCustom(User user) {
        int flag=0;
        int a=cd.updateUser(user);
        int b=cd.updateAccount(user);
        if (a==1&&b==1){
            flag=1;
        }
        return flag;
    }

    @Override
    public int updateCustomPwd(User user) {
        user.setUserPwd(ma.regUserPwd(user));

        return cd.updateCustomPwd(user);
    }

    @Override
    public int deleteCustom(String userId) {
        cd.deleteCustom(userId);
        cd.deleteCard(userId);
        return 1;
    }

    @Override
    public User queryCustom(String userId) {
        return cd.queryCustom(userId);
    }

    @Override
    public int updateStatus(User user) {
        return cd.updateStatus(user);
    }

    @Override
    public List<Transfer> queryAllTransfer(FenYe fenYe) {
        fenYe.setRowCount(cd.queryTransByLike(fenYe.getQuery()));
        if (fenYe.getRowCount()==0){
            return null;
        }
        if (fenYe.getPage()!=null){
            if (fenYe.getPage()<1){
                fenYe.setPage(1);
            }else if (fenYe.getPage()>fenYe.getPageCount()){
                fenYe.setPage(fenYe.getPageCount());
            }
        }else {
            fenYe.setPage(1);
        }

        List<Transfer> transfers = cd.queryAllTransfer(fenYe);
        return transfers;
    }

    @Override
    public List<Transfer> queryTransferByAccNo(String accNo) {
        return cd.queryTransferByAccNo(accNo);
    }

    @Override
    public List<Login> queryLoginByAccNo(FenYe fenYe) {
        fenYe.setRowCount(cd.queryLoginByLike(fenYe.getQuery()));
        if (fenYe.getRowCount()==0){
            return null;
        }
        if (fenYe.getPage()!=null){
            if (fenYe.getPage()<1){
                fenYe.setPage(1);
            }else if (fenYe.getPage()>fenYe.getPageCount()){
                fenYe.setPage(fenYe.getPageCount());
            }
        }else {
            fenYe.setPage(1);
        }
        return cd.queryLoginByAccNo(fenYe);
    }

    @Override
    public List<Login> queryExLoginByAccNo(String accNo) {
        return cd.queryExLoginByAccNo(accNo);
    }

    @Override
    public List<Transfer> queryExTransferByAccNo(String accNo) {
        return cd.queryExTransferByAccNo(accNo);
    }
}
