selectPageByCond
===
```sql
    select 
    -- @pageTag(){
        su.id          as id,
        su.unit_id     as unitId,
        su.user_name   as userName,
        su.nick_name   as nickName,
        su.phone       as phone,
        su.state       as state,
        su.create_time as createTime
    -- @}
    from sys_user su
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
            and su.unit_id in (#{join(cond.unitIds)})
        -- @}
    -- @}
```