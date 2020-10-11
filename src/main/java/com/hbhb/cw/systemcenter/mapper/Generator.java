package com.hbhb.cw.systemcenter.mapper;

import com.hbhb.beetl.gen.MapperCodeGen;

import org.beetl.sql.core.ClasspathLoader;
import org.beetl.sql.core.ConnectionSourceHelper;
import org.beetl.sql.core.Interceptor;
import org.beetl.sql.core.SQLManager;
import org.beetl.sql.core.UnderlinedNameConversion;
import org.beetl.sql.core.db.MySqlStyle;
import org.beetl.sql.ext.DebugInterceptor;
import org.beetl.sql.ext.gen.GenConfig;

public class Generator {
    public static void main(String[] args) throws Exception {
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://mariadb.yeexun.com.cn/cw_test";
        String userName = "zhcw";
        String password = "zhcw@2020";

        // 生成model对应目录
        String modelPkg = "com.hbhb.cw.systemcenter.model";
        // 生成dao对应目录
        String daoPkg = "com.hbhb.cw.systemcenter.mapper";
        // 生成sql对应目录
        String sqlPkg = "/sql";
        // 数据库表名
        String tableName = "sys_user";

        SQLManager sqlManager = new SQLManager(new MySqlStyle(),
                new ClasspathLoader(sqlPkg),
                ConnectionSourceHelper.getSimple(driver, url, userName, password),
                new UnderlinedNameConversion(),
                new Interceptor[]{new DebugInterceptor()});
        GenConfig config = new GenConfig("/gen/pojo.btl");
        config.setImplSerializable(true);
        config.setPropertyOrder(0);
        MapperCodeGen mapper = new MapperCodeGen(daoPkg);
        config.codeGens.add(mapper);

        sqlManager.genPojoCode(tableName, modelPkg, config);
    }
}
