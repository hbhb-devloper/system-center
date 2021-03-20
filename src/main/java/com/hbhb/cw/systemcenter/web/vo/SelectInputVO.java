package com.hbhb.cw.systemcenter.web.vo;


import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SelectInputVO implements Serializable {

    private static final long serialVersionUID = 921109951808175243L;
    private Long id;
    private String label;
    private String input = "";
}
