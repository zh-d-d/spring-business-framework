package com.dogbody.spring.framework.web.common.request;

import com.dogbody.spring.framework.web.common.constant.PageConstant;
import lombok.Data;

import java.io.Serializable;

/**
 * @author liaozan
 * @since 2022/1/7
 */
@Data
public class PageParam implements Serializable {

    private static final long serialVersionUID = 4760680296146863368L;

    private long pageIndex = PageConstant.DEFAULT_PAGE_INDEX;

    private long pageSize = PageConstant.DEFAULT_PAGE_SIZE;

}