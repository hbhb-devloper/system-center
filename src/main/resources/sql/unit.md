selectByUserId
===
```sql
        select u.*
        from unit u
                 left join sys_role_unit sru on u.id = sru.unit_id
                 left join sys_role sr on sru.role_id = sr.id
                 left join sys_user_role sur on sr.id = sur.role_id
                 left join sys_user su on sur.user_id = su.id
        where sr.role_type = 'UN'
          and su.id = #{userId}
        order by u.parent_id, u.sort_num
```