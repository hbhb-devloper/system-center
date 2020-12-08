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
        -- @if(isNotEmpty(roleType)){
            and role_type = #{roleType}
        -- @}
        -- @if(isNotEmpty(roleName)){
            and role_name like concat('%', #{roleName}, '%')
        -- @}
        -- @if(isNotEmpty(state)){
            and state = #{state}
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

selectUnitRoleByUserId
===
```sql
    select ru.unit_id
    from user u
        left join user_role ur on u.id = ur.user_id
        left join role r on ur.role_id = r.id
        left join role_unit ru on ur.role_id = ru.role_id
    where r.role_type = 'UN'
      and ru.is_half = 0
      and u.id = #{userId}
```