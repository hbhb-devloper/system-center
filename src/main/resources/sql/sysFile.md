selectListByType
===
```sql
    select id          as id,
           file_name   as fileName,
           file_path   as filePath,
           upload_time as uploadTime
    from sys_file
    where biz_type = #{type}
```
