selectUserPermsByType
===
    select distinct re.perms
    from sys_resource re
        left join sys_role_resource rr on re.id = rr.resource_id
        left join sys_user_role ur on rr.role_id = ur.role_id
        left join sys_role ro on ro.id = ur.role_id
    where ro.state = 1
      and re.perms != ''
      @if(!isEmpty(userId)){
        and ur.user_id = #userId#
      @}
      @if(!isEmpty(list)){
        and re.rs_type in (#join(list)#)
      @}
      
    
