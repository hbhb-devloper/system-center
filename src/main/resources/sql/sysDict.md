selectPageByCond
===
```sql
    select 
    -- @pageTag(){
    sd.id         as id,
    sdt.dict_name as dictTypeName,
    sd.dict_label as dictLabel,
    sd.dict_value as dictValue,
    sd.dict_code  as dictCode,
    sd.remark     as remark,
    sd.sort_num   as sortNum,
    sd.state      as state
    -- @}
    from sys_dict sd
        left join sys_dict_type sdt on sd.dict_type_id = sdt.id
    -- @where(){
        -- @if(isNotEmpty(cond.dictTypeName)){
            and sdt.dict_name like concat('%', #{cond.dictTypeName}, '%')
        -- @}
        -- @if(isNotEmpty(cond.dictLabel)){
            and sd.dict_label like concat('%', #{dictLabel}, '%')
        -- @}
    -- @}
    -- @pageIgnoreTag(){
        order by sd.create_time desc
    -- @}
```

selectListByCond
===
```sql
    select sd.id,
           sd.sort_num,
           sd.dict_label,
           sd.dict_value,
           sd.dict_type_id,
           sd.dict_code,
           sd.state,
           sd.remark,
           sd.create_time,
           sd.create_by
    from sys_dict sd
        left join sys_dict_type sdt on sd.dict_type_id = sdt.id
    -- @where(){
        -- @if(isNotEmpty(dictType)){
            and sdt.dict_type = #{dictType}
        -- @}
        -- @if(isNotEmpty(dictCode)){
            and sd.dict_code = #{dictCode}
        -- @}
    -- @}
    order by sd.sort_num
```