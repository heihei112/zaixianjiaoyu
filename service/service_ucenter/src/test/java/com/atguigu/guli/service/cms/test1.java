package com.atguigu.guli.service.cms;

import org.junit.jupiter.api.Test;


public class test1 {

    @Test
    public void test(){
        for (int i = 1; i< 10 ; i++){
            for (int j = 1 ; j < i +1 ;j++) {
                System.out.print(j+"*"+i+"="+j*i+"\t");
            }
            System.out.println(" ");
        }
    }
}
