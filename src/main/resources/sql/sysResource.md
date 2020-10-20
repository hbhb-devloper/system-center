selectAll
===
```sql
    select sr.id           as `id`,
           sr.name         as `name`,
           sr.perm         as `perm`,
           sr.api_path     as `api_path`,
           sr.desc         as `desc`,
           sr.create_time  as `createTime`,
           srr.role_id     as `srr.role_id`,
           srr.resource_id as `srr.resource_id`
    from sys_resource sr
             left join sys_role_resource srr on sr.id = srr.resource_id
```

selectPermsByUserId
===
```sql
    select distinct re.perm
    from sys_resource re
        left join sys_role_resource rr on re.id = rr.resource_id
        left join sys_user_role ur on rr.role_id = ur.role_id
        left join sys_role ro on ro.id = ur.role_id
    where ro.state = 1
      and re.perm != ''
      -- @if(isNotEmpty(userId)){
        and ur.user_id = #{userId}
      -- @}
```
