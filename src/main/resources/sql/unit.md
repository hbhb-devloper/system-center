selectByUserId
===
```sql
    select un.id        as id,
           un.unit_name as unitName,
           un.parent_id as parentId
    from unit un
         left join sys_user_uint_hall ru on un.id = ru.uint_id
         left join sys_user us on ru.user_id = us.id
    where us.id = #{userId}
    order by un.parent_id, un.sort_num 
```

<!-- 
  select un.id        as id,
           un.unit_name as unitName,
           un.parent_id as parentId
    from unit un
         left join sys_role_unit ru on un.id = ru.unit_id
         left join sys_role r on ru.role_id = r.id
         left join sys_user_role ur on r.id = ur.role_id
         left join sys_user us on ur.user_id = us.id
    where r.role_type = 'UN'
      -- 去掉半选节点
      and ru.is_half = 0
      and us.id = #{userId}
    order by un.parent_id, un.sort_num
-->
