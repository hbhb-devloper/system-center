package com.hbhb.cw.systemcenter.web.vo;

import com.hbhb.cw.systemcenter.model.RoleResource;

import org.beetl.sql.annotation.entity.ResultProvider;
import org.beetl.sql.core.mapping.join.AutoJsonMapper;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * @author xiaokang
 * @since 2020-10-20
 */
@Data
@ResultProvider(AutoJsonMapper.class)
public class ResourceVO implements Serializable {
    private static final long serialVersionUID = -302574163088501692L;

    private Integer id;
    private String name;
    private String perm;
    private String apiPath;
    private String desc;
    private Date createTime;
    private List<RoleResource> srr;
}
