package com.how2java.tmall.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class testTmall {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("驱动加载成功");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try(Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/tmall_ssm?useUnicode=true&characterEncoding=utf8",
                "root", "admin");
            Statement s = c.createStatement();
            ){
            for (int i = 0; i < 10; i++) {
                String sqlFormat = "insert into category values (null,'测试分类%d')";
                String sql = String .format(sqlFormat,i);
                s.execute(sql);
            }
            System.out.println("创建10个数据");
        }
        catch (SQLException e){
                e.printStackTrace();
        }


    }
}
