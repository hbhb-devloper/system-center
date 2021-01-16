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


