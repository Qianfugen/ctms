package com.zl.pojo;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Scope("prototype")
public class UFenYe implements Serializable {

    private Integer page;//当前的页码
    private Integer pageRows = 4;//每页显示多少行
    private Integer pageCount;//总页码数
    private Integer rowsCount;//符合要求的记录数
    private Integer rowStart;//当前页码的开始条
    private Integer rowEnd;//当前页码的结束条
    private Uquery uquery;//当前请求分页的查询条件

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageRows() {
        return pageRows;
    }

    public void setPageRows(Integer pageRows) {
        this.pageRows = pageRows;
    }

    public Integer getPageCount() {
        pageCount = (int) Math.ceil(getRowsCount() / (getPageRows() * 1.0));
        System.out.println("多少頁" + pageCount);
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public Integer getRowsCount() {
        return rowsCount;
    }

    public void setRowsCount(Integer rowsCount) {
        this.rowsCount = rowsCount;
    }

    public Integer getRowStart() {
        rowStart = (getPage() - 1) * getPageRows();
        return rowStart;
    }

    public void setRowStart(Integer rowStart) {
        this.rowStart = rowStart;
    }

    public Integer getRowEnd() {
        rowEnd = getPage() * getPageRows();
        return rowEnd;
    }

    public void setRowEnd(Integer rowEnd) {
        this.rowEnd = rowEnd;
    }

    public Uquery getUquery() {
        return uquery;
    }

    public void setUquery(Uquery uquery) {
        this.uquery = uquery;
    }

    @Override
    public String toString() {
        return "uFenYe{" +
                "page=" + page +
                ", pageRows=" + pageRows +
                ", pageCount=" + pageCount +
                ", rowsCount=" + rowsCount +
                ", rowStart=" + rowStart +
                ", rowEnd=" + rowEnd +
                ", uquery=" + uquery +
                '}';
    }
}