package com.hbhb.cw.systemcenter.enums;

import lombok.Getter;

@Getter
public enum FileBizType {
    /**
     * 预算项目签报类型文件
     */
    BUDGET_PROJECT_FILE(20),

    /**
     * 系统文件
     */
    SYSTEM_FILE(10),

    /**
     * 客户资金
     */
    FUND_FILE(30),

    /**
     * 迁改预警附件
     */
    RELOCATION_FILE(40),
    ;

    private final Integer value;

    FileBizType(Integer value) {
        this.value = value;
    }


}
