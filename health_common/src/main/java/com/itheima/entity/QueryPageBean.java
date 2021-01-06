package com.itheima.entity;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 封装查询条件
 */
public class QueryPageBean implements Serializable{

    @NotNull(message = "页码不能为空")
    @Min(value = 1,message = "页码不能小于1")
    private Integer currentPage;//页码

    @NotNull(message = "每页条数不能为空")
    @Min(value = 1,message = "每页条数不能小于1")
    @Max(value = 100,message = "每页条数不能大于100")
    private Integer pageSize;//每页记录数
    private String queryString;//查询条件

    @Override
    public String toString() {
        return "QueryPageBean{" +
                "currentPage=" + currentPage +
                ", pageSize=" + pageSize +
                ", queryString='" + queryString + '\'' +
                '}';
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }
}