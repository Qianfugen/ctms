package com.zl.service.impl;

import com.zl.dao.PayInfoDao;
import com.zl.pojo.Paging;
import com.zl.pojo.PayInfo;
import com.zl.service.PayInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 徐浩杰
 * @version 1.0 2019-12-10
 */
@Service
public class PayInfoServiceImpl implements PayInfoService {
    @Autowired
    private PayInfoDao pid;

    /**
     * 分页查询出催款记录信息
     *
     * @param paging
     * @return
     */
    @Override
    public List<PayInfo> queryPayInfoByPaging(Paging paging) {
        //设置符合要求的记录总数
        paging.setRowsCount(pid.queryPayInfoCount(paging.getQuery()));

        /**
         * 处理分页对象
         */
        //设置当前页码
        if (paging.getCurrentPage() != null) {
            if (paging.getCurrentPage() <= 0) {
                paging.setCurrentPage(1);
            }
            //如果大于最大页数
            if (paging.getCurrentPage() > paging.getPages()) {
                paging.setCurrentPage(paging.getPages());
            }
        } else {
            paging.setCurrentPage(1);
        }

        List<PayInfo> payInfos = pid.queryPayInfoByPaging(paging);
        return payInfos;
    }

    /**
     * 发送消息通知
     * @param payInfo
     * @return
     */
    @Override
    public int addPayInfo(PayInfo payInfo) {
        return pid.addPayInfo(payInfo);
    }



}
