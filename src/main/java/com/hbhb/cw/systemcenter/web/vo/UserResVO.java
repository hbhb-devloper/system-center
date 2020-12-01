package com.hbhb.cw.systemcenter.web.vo;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @author dxk
 */
@Data
public class UserResVO implements Serializable {
    private static final long serialVersionUID = 2435590882415803810L;

    private Integer id;
    private String unitName;
    private String userName;
    private String nickName;
    private String phone;
    private String jobName;
    private Byte state;
    private Date createTime;
}