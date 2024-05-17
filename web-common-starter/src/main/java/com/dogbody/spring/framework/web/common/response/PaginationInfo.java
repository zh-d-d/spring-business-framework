package com.dogbody.spring.framework.web.common.response;

import com.dogbody.spring.framework.web.common.constant.PageConstant;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * @author liaozan
 * @since 2021/10/15
 */
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaginationInfo<T> implements Serializable {

    public static final long serialVersionUID = 1320105164315113239L;

    /**
     * 页索引
     */
    private long pageIndex = PageConstant.DEFAULT_PAGE_INDEX;
    /**
     * 每个页面大小
     */
    private long pageSize = PageConstant.DEFAULT_PAGE_SIZE;
    /**
     * 当前结果集记录数量
     */
    private long currentPageSize = PageConstant.DEFAULT_PAGE_INDEX;
    /**
     * 总页面数量
     */
    private long totalPageCount = PageConstant.DEFAULT_TOTAL_COUNT;
    /**
     * 满足条件的记录数量
     */
    private long totalCount = PageConstant.DEFAULT_TOTAL_COUNT;
    /**
     * 是否有前一页
     */
    private boolean hasPrevPage = false;
    /**
     * 是否有下一页
     */
    private boolean hasNextPage = false;
    /**
     * 结果集, Use new ArrayList() instead of collections.emptyList() to prevent errors when users edit it later
     */
    private List<T> dataList = new ArrayList<>(0);

    public PaginationInfo(long pageIndex, long pageSize, long totalCount) {
        this(pageIndex, pageSize, totalCount, new ArrayList<>(0));
    }

    public PaginationInfo(long pageIndex, long pageSize, long totalCount, List<T> dataList) {
        this.setPageIndex(pageIndex);
        this.setPageSize(pageSize);
        this.setTotalCount(totalCount);
        this.setDataList(dataList);
    }

    public void setPageIndex(long pageIndex) {
        if (pageIndex <= 0) {
            pageIndex = PageConstant.DEFAULT_PAGE_INDEX;
        }
        this.pageIndex = pageIndex;
    }

    public void setPageSize(long pageSize) {
        if (pageSize <= 0) {
            pageSize = PageConstant.DEFAULT_PAGE_SIZE;
        }
        this.pageSize = pageSize;
    }

    public void setTotalCount(long totalCount) {
        if (totalCount < 0L) {
            totalCount = PageConstant.DEFAULT_TOTAL_COUNT;
        }

        if (totalCount == 0L) {
            this.totalPageCount = PageConstant.DEFAULT_TOTAL_PAGE_COUNT;
        } else {
            this.totalPageCount = (totalCount - 1L) / this.pageSize + 1L;
        }

        this.hasPrevPage = this.pageIndex > PageConstant.DEFAULT_PAGE_INDEX;
        this.hasNextPage = this.pageIndex < totalPageCount;
        this.totalCount = totalCount;
    }

    public void setDataList(List<T> dataList) {
        if (dataList == null) {
            dataList = new ArrayList<>(0);
        }
        this.dataList = dataList;
        this.currentPageSize = dataList.size();
    }

}