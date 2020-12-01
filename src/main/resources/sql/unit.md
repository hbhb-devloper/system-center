selectByUserId
===
```sql
    select un.*
    from unit un
         left join role_unit ru on un.id = ru.unit_id
         left join role r on ru.role_id = r.id
         left join user_role ur on r.id = ur.role_id
         left join user us on ur.user_id = us.id
    where r.role_type = 'UN'
      and us.id = #{userId}
    order by un.parent_id, un.sort_num
```