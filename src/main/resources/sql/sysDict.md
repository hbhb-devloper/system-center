selectPageByCond
===
```sql
    select 
    -- @pageTag(){
        d.id         as id,
        dt.type_name as dictTypeName,
        d.dict_label as dictLabel,
        d.dict_value as dictValue,
        d.dict_code  as dictCode,
        d.remark     as remark,
        d.sort_num   as sortNum,
        d.state      as state
    -- @}
    from sys_dict d
        left join sys_dict_type dt on d.dict_type_id = dt.id
    -- @where(){
        -- @if(isNotEmpty(dictTypeId)){
            and dt.id = #{dictTypeId}
        -- @}
        -- @if(isNotEmpty(dictTypeName)){
            and dt.type_name like concat('%', #{dictTypeName}, '%')
        -- @}
        -- @if(isNotEmpty(dictLabel)){
            and d.dict_label like concat('%', #{dictLabel}, '%')
        -- @}
    -- @}
    -- @pageIgnoreTag(){
        order by d.dict_code
    -- @}
```

selectListByCond
===
```sql
    select d.id,
           d.sort_num,
           d.dict_label,
           d.dict_value,
           d.dict_type_id,
           d.dict_code,
           d.state,
           d.remark,
           d.create_time,
           d.create_by
    from sys_dict d
        left join sys_dict_type dt on d.dict_type_id = dt.id
    -- @where(){
        -- @if(isNotEmpty(dictType)){
            and dt.type_code = #{dictType}
        -- @}
        -- @if(isNotEmpty(dictCode)){
            and d.dict_code = #{dictCode}
        -- @}
    -- @}
    order by d.sort_num
```