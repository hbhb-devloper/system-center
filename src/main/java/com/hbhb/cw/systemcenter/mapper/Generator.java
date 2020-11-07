//package com.hbhb.cw.systemcenter.mapper;
//
//import org.beetl.core.GroupTemplate;
//import org.beetl.core.ReThrowConsoleErrorHandler;
//import org.beetl.core.resource.ClasspathResourceLoader;
//import org.beetl.sql.core.ConnectionSource;
//import org.beetl.sql.core.ConnectionSourceHelper;
//import org.beetl.sql.core.SQLManager;
//import org.beetl.sql.core.SQLManagerBuilder;
//import org.beetl.sql.core.UnderlinedNameConversion;
//import org.beetl.sql.core.db.MySqlStyle;
//import org.beetl.sql.gen.SourceBuilder;
//import org.beetl.sql.gen.SourceConfig;
//import org.beetl.sql.gen.simple.BaseTemplateSourceBuilder;
//import org.beetl.sql.gen.simple.ConsoleOnlyProject;
//import org.beetl.sql.gen.simple.EntitySourceBuilder;
//import org.beetl.sql.gen.simple.MapperSourceBuilder;
//import org.beetl.sql.gen.simple.SimpleMavenProject;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author dxk
// */
//public class Generator {
//    private static final String DRIVER = "com.mysql.jdbc.Driver";
//    private static final String URL = "jdbc:mysql://mariadb.yeexun.com.cn/cw_sys";
//    private static final String USERNAME = "zhcw";
//    private static final String PASSWORD = "zhcw@2020";
//    /**
//     * 数据库表名
//     */
//    private static final String TABLE_NAME = "sys_file";
//
//    public static void main(String[] args) {
//        // 设置自定义模版路径
//        GroupTemplate groupTemplate = BaseTemplateSourceBuilder.getGroupTemplate();
//        ClasspathResourceLoader resourceLoader = new ClasspathResourceLoader("template/");
//        groupTemplate.setResourceLoader(resourceLoader);
//
//        // 创建sqlManager
//        ConnectionSource source = ConnectionSourceHelper.getSimple(DRIVER, URL, USERNAME, PASSWORD);
//        SQLManagerBuilder builder = new SQLManagerBuilder(source);
//        builder.setNc(new UnderlinedNameConversion());
//        builder.setDbStyle(new MySqlStyle());
//        SQLManager sqlManager = builder.build();
//
//        // 配置生成参数
//        List<SourceBuilder> sourceBuilder = new ArrayList<>();
//        sourceBuilder.add(new EntitySourceBuilder());
//        sourceBuilder.add(new MapperSourceBuilder());
////        sourceBuilder.add(new MDSourceBuilder());
//        SourceConfig config = new SourceConfig(sqlManager, sourceBuilder);
//
//        // 如果有错误，抛出异常而不是继续运行
//        EntitySourceBuilder.getGroupTemplate().setErrorHandler(new ReThrowConsoleErrorHandler());
//        // 输出到控制台
//        ConsoleOnlyProject project = new ConsoleOnlyProject();
//        // 输出到文件
////        SimpleMavenProject project = new SimpleMavenProject();
//        config.gen(TABLE_NAME, project);
//    }
//}