package com.hbhb.cw.systemcenter.enums;

import lombok.Getter;

@Getter
public enum BroadcastState {
    /**
     * 停用
     */
    DISABLE((byte) 0),
    /**
     * 启用
     */
    ENABLE((byte) 1),
    ;

    private final Byte value;

    BroadcastState(Byte value) {
        this.value = value;
    }

    public Byte value() {
        return this.value;
    }

}
