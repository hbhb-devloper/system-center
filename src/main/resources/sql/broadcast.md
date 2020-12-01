selectPageByCond
===
```sql
    select 
    -- @pageTag(){
    *
    -- @}
    from broadcast
    -- @where(){
        -- @if(isNotEmpty(content)){
            and content like concat('%', #{content}, '%')
        -- @}
        -- @if(isNotEmpty(state)){
            and state = #{state}
        -- @}
    -- @}
    -- @pageIgnoreTag(){
        order by create_time desc
    -- @}
```

selectListByState
===
```sql
    select content from broadcast
    where state = #{state}
    order by sort_num
```