package com.hbhb.cw.systemcenter.mapper;

import org.beetl.core.GroupTemplate;
import org.beetl.core.resource.ClasspathResourceLoader;
import org.beetl.sql.core.SQLManager;
import org.beetl.sql.gen.simple.BaseTemplateSourceBuilder;

/**
 * @author dxk
 */
public class Generator {
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://mariadb.yeexun.com.cn/cw_sys";
    private static final String USERNAME = "zhcw";
    private static final String PASSWORD = "zhcw@2020";
    /**
     * 数据库表名
     */
    private static final String TABLE_NAME = "sys_file";

    protected void initGroupTemplate() {
        GroupTemplate groupTemplate = BaseTemplateSourceBuilder.getGroupTemplate();
        ClasspathResourceLoader resourceLoader = new ClasspathResourceLoader("gens/");
        groupTemplate.setResourceLoader(resourceLoader);
    }

    public static void main(String[] args) {
        SQLManager sqlManager = SQLManager.newBuilder(DRIVER, URL, USERNAME, PASSWORD).build();
//        List<SourceBuilder> sourceBuilder = new ArrayList<>();
//        sourceBuilder.add(new EntitySourceBuilder());
//        sourceBuilder.add(new MapperSourceBuilder());
//        SourceConfig config = new SourceConfig(sqlManager, sourceBuilder);
//        //如果有错误，抛出异常而不是继续运行
//        EntitySourceBuilder.getGroupTemplate().setErrorHandler(new ReThrowConsoleErrorHandler());
//        ConsoleOnlyProject project = new ConsoleOnlyProject();
////        SimpleMavenProject project = new SimpleMavenProject();
//        config.gen(TABLE_NAME, project);
    }

    String daoPkg = "com.hbhb.cw.systemcenter.mapper";
}
