package com.zl.service;

import com.zl.pojo.Paging;
import com.zl.pojo.PayInfo;

import java.util.List;

/**
 * @author 徐浩杰
 * @version 1.0 2019-12-10
 */
public interface PayInfoService {
    /**
     * 分页查询出催款记录信息
     * @param paging
     * @return
     */
    List<PayInfo> queryPayInfoByPaging(Paging paging);

    /**
     * 发送消息通知
     */
    int addPayInfo(PayInfo payInfo);

    /**
     * 删除消息通知
     */
    int deletePayInfo(PayInfo payInfo);

    /**
     * 查询催款消息通知
     */
    PayInfo queryPayInfo(PayInfo payInfo);

}
