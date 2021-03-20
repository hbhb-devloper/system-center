<!--
selectAll
===
新的resource表
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
-->

selectPermsByUserId
===
```sql
    select distinct re.perms
    from sys_resource re
        left join sys_role_resource rr on re.id = rr.resource_id
        left join sys_user_role ur on rr.role_id = ur.role_id
        left join sys_role ro on ro.id = ur.role_id
    where ro.state = 1
      and re.perms != ''
      and ur.user_id = #{userId}
      and re.rs_type in (#{join(list)})
```

selectMenuTreeAll
===
```sql
    select distinct *
    from sys_resource
    where rs_type in (#{join(list)})
    order by parent_id, order_num
```

selectMenuTreeByUserId
===
```sql
    select distinct re.*
    from sys_resource re
        left join sys_role_resource rr on re.id = rr.resource_id
        left join sys_user_role ur on rr.role_id = ur.role_id
        left join sys_role ro on ur.role_id = ro.id
        left join sys_user cu on ur.user_id = cu.id
    where ro.state = 1
      and cu.id = #{userId}
      and re.rs_type in (#{join(list)})
    order by parent_id, order_num
```