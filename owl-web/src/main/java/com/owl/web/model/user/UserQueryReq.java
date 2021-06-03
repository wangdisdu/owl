package com.owl.web.model.user;

import com.owl.web.dao.entity.TbUser;
import com.owl.web.model.BaseQuery;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserQueryReq extends BaseQuery<TbUser> {
    private UserFilterReq filter = new UserFilterReq();
    private UserSorterReq sorter = new UserSorterReq();
}
