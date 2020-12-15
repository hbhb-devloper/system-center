selectPageByCond
===
```sql
    select 
    -- @pageTag(){
        su.id          as id,
        u.unit_name    as unitName,
        su.user_name   as userName,
        su.nick_name   as nickName,
        su.phone       as phone,
        su.state       as state,
        su.create_time as createTime
    -- @}
    from sys_user su
    left join unit u on su.unit_id = u.id
    -- @where(){
        -- @if(isNotEmpty(cond.userName)){
            and su.user_name like concat('%', #{cond.userName}, '%')
        -- @}
        -- @if(isNotEmpty(cond.nickName)){
            and su.nick_name like concat('%', #{cond.nickName}, '%')
        -- @}
        -- @if(isNotEmpty(cond.phone)){
            and su.phone = #{cond.phone}
        -- @}
        -- @if(isNotEmpty(cond.state)){
            and su.state = #{cond.state}
        -- @}
        -- @if(isNotEmpty(cond.unitIds)){
            and u.id in (#{join(cond.unitIds)})
        -- @}
    -- @}
```