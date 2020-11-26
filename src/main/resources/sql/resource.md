selectAll
===
```sql
    select r.id           as `id`,
           r.name         as `name`,
           r.perm         as `perm`,
           r.api_path     as `api_path`,
           r.desc         as `desc`,
           r.create_time  as `createTime`,
           rr.role_id     as `rr.role_id`,
           rr.resource_id as `rr.resource_id`
    from resource r
             left join role_resource rr on r.id = rr.resource_id
```

selectPermsByUserId
===
```sql
    select distinct re.perm
    from resource re
        left join role_resource rr on re.id = rr.resource_id
        left join user_role ur on rr.role_id = ur.role_id
        left join role ro on ro.id = ur.role_id
    where ro.state = 1
      and re.perm != ''
      -- @if(isNotEmpty(userId)){
        and ur.user_id = #{userId}
      -- @}
```
