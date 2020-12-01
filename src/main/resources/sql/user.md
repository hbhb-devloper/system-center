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
    from user su
    left join unit u on su.unit_id = u.id
    -- @where(){
        -- @if(isNotEmpty(userName)){
            and su.user_name like concat('%', #{userName}, '%')
        -- @}
        -- @if(isNotEmpty(nickName)){
            and su.nick_name like concat('%', #{cond.nickName}, '%')
        -- @}
        -- @if(isNotEmpty(phone)){
            and su.phone = #{phone}
        -- @}
        -- @if(isNotEmpty(state)){
            and su.state = #{state}
        -- @}
        -- @if(isNotEmpty(unitIds)){
          and u.id in (
          -- @for(item in unitIds){
             #{item}
          -- @}
          )
      -- @}
    -- @}
```