package com.zl.pojo;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 主动收款分页工具类
 *
 * @author 徐浩杰
 * @version 1.0 2019-12-10
 */
@Component
@Scope("prototype")
public class Paging implements Serializable {
    private Integer currentPage;//当前页码
    private Integer rowsPage = 5;//每页显示多少条
    private Integer pages;//总页码
    private Integer rowsCount;//符合要求的记录数
    private Integer rowStart;//当前页码的开始条数
    private Integer rowEnd;//当前页码的结束条数
    private Query query;//当前请求的查询条件

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getRowsPage() {
        return rowsPage;
    }

    public void setRowsPage(Integer rowsPage) {
        this.rowsPage = rowsPage;
    }

    public Integer getPages() {
        pages = (int) Math.ceil(getRowsCount() / (getRowsPage() * 1.0));
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public Integer getRowsCount() {
        return rowsCount;
    }

    public void setRowsCount(Integer rowsCount) {
        this.rowsCount = rowsCount;
    }

    public Integer getRowStart() {
        rowStart = (getCurrentPage() - 1) * getRowsPage();
        return rowStart;
    }

    public void setRowStart(Integer rowStart) {
        this.rowStart = rowStart;
    }

    public Integer getRowEnd() {
        rowEnd = getCurrentPage() * getRowsPage();
        return rowEnd;
    }

    public void setRowEnd(Integer rowEnd) {
        this.rowEnd = rowEnd;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    @Override
    public String toString() {
        return "Paging{" +
                "currentPage=" + currentPage +
                ", rowsPage=" + rowsPage +
                ", pages=" + pages +
                ", rowsCount=" + rowsCount +
                ", rowStart=" + rowStart +
                ", rowEnd=" + rowEnd +
                ", query=" + query +
                '}';
    }
}