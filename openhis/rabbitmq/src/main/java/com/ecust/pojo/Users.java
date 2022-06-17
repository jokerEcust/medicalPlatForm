package com.ecust.pojo;


import lombok.Data;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContextAware;

@Data
public class Users implements BeanNameAware {
    private String name;
    @Override
    public void setBeanName(String s) {
        this.name=s;
    }

    public String getName() {
        return name;
    }


}
