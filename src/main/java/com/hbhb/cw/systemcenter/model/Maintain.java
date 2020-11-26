package com.hbhb.cw.systemcenter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Maintain {

    private Long id;
    private String administrator;
    private String phone;
    private String email;
    private String softwareName;
    private String version;
    private String devLanguage;
}