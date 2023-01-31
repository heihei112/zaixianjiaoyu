package com.atguigu.guli.service.edu.entity.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SubjectVo implements Serializable {
    private static final Long serialVersionUid = 1L;

    private String id;
    private String title;
    private Integer sort;

    private List<SubjectVo> children;
}
