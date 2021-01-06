package com.itheima.pojo;

import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * 检查组
 */
public class CheckGroup implements Serializable {
    @Override
    public String toString() {
        return "CheckGroup{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", helpCode='" + helpCode + '\'' +
                ", sex='" + sex + '\'' +
                ", remark='" + remark + '\'' +
                ", attention='" + attention + '\'' +
                ", checkItems=" + checkItems +
                ", checkItemsId=" + checkItemsId +
                '}';
    }

    private Integer id;//主键
    @NotBlank(message="分组编码不能为空字符")
    private String code;//编码
    @NotBlank(message="分组名称不能为空字符")
    private String name;//名称
    private String helpCode;//助记
    private String sex;//适用性别
    private String remark;//介绍
    private String attention;//注意事项
    private List<CheckItem> checkItems;//一个检查组合包含多个检查项
    private List<Integer> checkItemsId;//id[]

    public List<Integer> getCheckItemsId() {
        return checkItemsId;
    }

    public void setCheckItemsId(List<Integer> checkItemsId) {
        this.checkItemsId = checkItemsId;
    }

    public List<CheckItem> getCheckItems() {
        return checkItems;
    }

    public void setCheckItems(List<CheckItem> checkItems) {
        this.checkItems = checkItems;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHelpCode() {
        return helpCode;
    }

    public void setHelpCode(String helpCode) {
        this.helpCode = helpCode;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAttention() {
        return attention;
    }

    public void setAttention(String attention) {
        this.attention = attention;
    }
}
