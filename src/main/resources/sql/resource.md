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

selectAll
===
```sql
    select * 
    from resource
    order by parent_id, order_num
```

selectPermsByUserId
===
```sql
    select distinct re.perms
    from resource re
        left join role_resource rr on re.id = rr.resource_id
        left join user_role ur on rr.role_id = ur.role_id
        left join role ro on ro.id = ur.role_id
    where ro.state = 1
      and re.perms != ''
      and ur.user_id = #{userId}
      -- @if(isNotEmpty(list)){
          and re.rs_type in (
          -- @for(item in list){
             #{item}
          -- @}
          )
      -- @}
```

selectMenuTreeAll
===
```sql
    select distinct *
    from resource
    where rs_type in (
    -- @for(item in list){
        #{item}
    -- @}
    )
    order by parent_id, order_num
```

selectMenuTreeByUserId
===
```sql
    select distinct re.*
    from resource re
        left join role_resource rr on re.id = rr.resource_id
        left join user_role ur on rr.role_id = ur.role_id
        left join role ro on ur.role_id = ro.id
        left join user cu on ur.user_id = cu.id
    where ro.state = 1
      and cu.id = #{userId}
      and re.rs_type in (
        -- @for(item in list){
            #{item}
        -- @}
        )
    order by parent_id, order_num
```