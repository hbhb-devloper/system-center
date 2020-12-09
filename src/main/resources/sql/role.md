selectPageByCond
===
```sql
    select
    -- @pageTag(){
        id          as id,
        role_type   as roleType,
        role_name   as roleName,
        state       as state,
        create_time as createTime
    -- @}
    from role 
    -- @where(){
        -- @if(isNotEmpty(cond.roleType)){
            and role_type = #{cond.roleType}
        -- @}
        -- @if(isNotEmpty(cond.roleName)){
            and role_name like concat('%', #{cond.roleName}, '%')
        -- @}
        -- @if(isNotEmpty(cond.state)){
            and state = #{cond.state}
        -- @}
    -- @}
```

selectUserRolesByType
===
```sql
    select distinct r.*
    from role r
        left join user_role ur on r.id = ur.role_id
    where r.state = 1
    -- @if(isNotEmpty(roleType)){
        and r.role_type = #{roleType}
    -- @}
    -- @if(isNotEmpty(userId)){
        and ur.user_id = #{userId}
    -- @}
```
