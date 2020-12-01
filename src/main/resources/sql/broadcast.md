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