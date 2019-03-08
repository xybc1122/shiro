package com.dt.user.dto;

import com.dt.user.model.TableHead;

import java.util.List;

/**
 * @ClassName TableHeadDto
 * Description TODO
 * @Author 陈恩惠
 * @Date 2019/3/8 16:47
 **/
public class TableHeadDto extends TableHead {
    /**
     * 页面菜单传来的单个ID
     */
    private Integer mId;
    /**
     * 排序
     */
    private List<TableHead> sort;

    public Integer getmId() {
        return mId;
    }

    public void setmId(Integer mId) {
        this.mId = mId;
    }

    public List<TableHead> getSort() {
        return sort;
    }

    public void setSort(List<TableHead> sort) {
        this.sort = sort;
    }
}
