package com.zl.service.impl;

import com.zl.dao.ICustomDao;
import com.zl.pojo.FenYe;
import com.zl.pojo.User;
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

    @Override
    public List<User> queryAllCustom(FenYe fenYe) {
        fenYe.setRowCount(cd.queryByLike(fenYe.getQuery()));

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
        return cd.updateCustom(user);
    }

    @Override
    public int deleteCustom(String userId) {
        return cd.deleteCustom(userId);
    }

    @Override
    public User queryCustom(String userId) {
        return cd.queryCustom(userId);
    }

    @Override
    public int updateStatus(User user) {
        return cd.updateStatus(user);
    }
}
