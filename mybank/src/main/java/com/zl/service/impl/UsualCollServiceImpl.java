package com.zl.service.impl;

import com.zl.dao.UsualCollDao;
import com.zl.pojo.Payee;
import com.zl.pojo.UsualColl;
import com.zl.pojo.UFenYe;
import com.zl.service.UsualCollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsualCollServiceImpl implements UsualCollService {
    @Autowired
    private UsualCollDao uc;

    @Override
    public List<UsualColl> queryUsualColl(String mainAcc) {
        return uc.queryUsualColl(mainAcc);
    }
    @Override
    public int deleteUsualColl(String accIn) {
        return uc.deleteUsualColl(accIn);
    }

    @Override
    public int addUsualColl(UsualColl usualColl) {
        UsualColl usualColl1 = uc.queryUsualCollByaccIn(usualColl.getAccIn());
            return uc.addUsualColl(usualColl);

    }

    @Override
    public UsualColl queryUsualCollByaccIn(String accIn) {
        return uc.queryUsualCollByaccIn(accIn);
    }

    @Override
    public List<UsualColl> queryUsualCollByFy(UFenYe uFenYe) {

        //设置符合要求的记录总数
        uFenYe.setRowsCount(uc.queryUsualCount(uFenYe.getUquery()));

        /**
         * 处理分页对象
         */
        //设置当前页码
        if (uFenYe.getPage() != null) {
            if (uFenYe.getPage()  <= 0) {
                uFenYe.setPage(1);
            }
            //如果大于最大页数
            if (uFenYe.getPage()  > uFenYe.getPageCount()) {
                uFenYe.setPage(uFenYe.getPageCount());
            }
        } else {
            uFenYe.setPage(1);
        }
        System.out.println("總tiao數"+uFenYe.getRowsCount());
        System.out.println("總頁ma數"+uFenYe.getPageCount());
        List<UsualColl> usualColls = uc.queryUsualCollByFy(uFenYe);
        return usualColls;
    }



    @Override
    public int queryUsualByAccIn(String mainAcc,String accIn) {
        UsualColl usualColl=new UsualColl();
        usualColl.setMainAcc(mainAcc);
        usualColl.setAccIn(accIn);
        return uc.queryUsualByAccIn(usualColl);
    }
}
