package com.zl.pojo;

import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author junqi
 */
@Component
public class FenYe implements Serializable {
    private Integer page;
    private Integer rows = 10;
    private Integer pageCount;
    private Integer rowCount;
    private Integer rowStart;
    private Integer rowEnd;
    private Query query;

    @Override
    public String toString() {
        return "FenYe{" +
                "page=" + page +
                ", rows=" + rows +
                ", pageCount=" + pageCount +
                ", rowCount=" + rowCount +
                ", rowStart=" + rowStart +
                ", rowEnd=" + rowEnd +
                ", query=" + query +
                '}';
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getPageCount() {
        pageCount = (int) Math.ceil(getRowCount() / (getRows() * 1.0));
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public Integer getRowCount() {
        return rowCount;
    }

    public void setRowCount(Integer rowCount) {
        this.rowCount = rowCount;
    }

    public Integer getRowStart() {
        rowStart = (getPage() - 1) * 10;
        return rowStart;
    }

    public void setRowStart(Integer rowStart) {
        this.rowStart = rowStart;
    }

    public Integer getRowEnd() {
        rowEnd = (getPage() * 10) + 1;
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
}
