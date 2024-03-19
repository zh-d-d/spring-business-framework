package com.dogbody.spring.framework.oss.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author liaozan
 * @since 2021/12/19
 */
@Data
public class OssOperationResult implements Serializable {

    private static final long serialVersionUID = 3651584115463313214L;


    protected String bucket;
    protected String objectKey;



}