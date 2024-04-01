package com.dogbody.spring.framework.mybatis.plus.support.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.dogbody.spring.framework.mybatis.plus.support.constant.MybatisConstants;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author zhangdd on 2024/3/29
 */
@Data
public class BasePo {
    /**
     * 主键 id
     */
    @TableId(value = MybatisConstants.ID, type = IdType.AUTO)
    protected Long id;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    protected LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(value = "modify_time", fill = FieldFill.INSERT_UPDATE)
    protected LocalDateTime modifyTime;
}
