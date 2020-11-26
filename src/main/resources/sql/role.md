selectUserRolesByType
===
```sql
    select distinct r.*
    from sys_role r
        left join sys_user_role ur on r.id = ur.role_id
    where r.state = 1
    -- @if(isNotEmpty(roleType)){
        and r.role_type = #{roleType}
    -- @}
    -- @if(isNotEmpty(userId)){
        and ur.user_id = #{userId}
    -- @}
```
