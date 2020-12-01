package com.hbhb.cw.systemcenter.web.vo;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaokang
 * @since 2020-06-26
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleVO implements Serializable {
    private static final long serialVersionUID = 3268871575637207171L;

    private Integer id;
    private String roleType;
    private String roleName;
    private Byte state;
    private Date createTime;
}
