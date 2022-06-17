package com.ecust;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
public class Main1 {
    public static void main(String[] args) {
//        ApplicationContext applicationContext=new ClassPathXmlApplicationContext("xxx");
//        applicationContext.getBean("");
//        int i = minSub(7, "1000101");
//        System.out.println(i);
//        String s="asasd";
//        s.substring(0,1)
        SpringApplication.run(Main1.class,args);

    }

    public static String longestPalindrome(String s) {
        int len=s.length();
        int maxLen=1;
        int start=0;
        if(s.length()<2)return s;
        boolean[][] dp=new boolean[len][len];
        for(int i=0;i<len;i++){
            dp[i][i]=true;
            if(i<len-1&&s.charAt(i)==s.charAt(i+1)){
                dp[i][i+1]=true;
                maxLen=2;
                start=i;
            }
        }
        for(int i=3;i<=len;i++){//长度
            for(int j=0;j<=len-i;j++){
                int k=j+i-1;//子串的结尾
                if(dp[j+1][k-1]&&s.charAt(j)==s.charAt(k)){
                    dp[j][k]=true;
                    maxLen=i;
                    start=j;
                }
            }
        }
        return s.substring(start,maxLen+start);
    }

    public static int minSub(int n, String s) {
        int pos = 1;
        int sum = 0;
        //初始时计算1的个数；1：防守，0：进攻
        for (int i = 0; i < n; i++) {
            if (s.charAt(i) == '1') {
                sum=sum+i+1;
            }
        }

        int left=0,right=sum;
        int min=Math.abs(left-right);
        int res=0;
        while (pos <= n) {
            if(s.charAt(pos-1)=='0'){
                left+=pos;
            }
            if(s.charAt(pos-1)=='1'){
                right-=pos;
            }
            if(Math.abs(left-right)<min){
                res=pos;
                min=Math.abs(left-right);
            }
            pos++;
        }
        return min;
    }

}
