package com.zl.dao;

import com.zl.pojo.Paging;
import com.zl.pojo.PayInfo;
import com.zl.pojo.Payee;
import com.zl.pojo.Query;

import java.util.List;

/**
 * @author 徐浩杰
 * @version 1.0 2019-12-10
 */
public interface PayInfoDao {
    /**
     * 分页查询出催款消息通知
     */
    List<PayInfo> queryPayInfoByPaging(Paging paging);

    /**
     * 查询符合条件的催款记录数
     */
    int queryPayInfoCount(Query query);

    /**
     * 发送催款消息通知
     */
    int addPayInfo(PayInfo payInfo);


}
