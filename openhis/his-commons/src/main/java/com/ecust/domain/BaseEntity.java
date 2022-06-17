package com.ecust.domain;

import java.io.Serializable;

/**
 * 实体基础父类
 * 由于所有的对象在将来都需要进行网路传输，故需要序列化
 * 该实体类为将来所有的实体类的父类
 */
public class BaseEntity implements Serializable {
    private static final long serialVersionUID=1L;
}
