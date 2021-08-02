package com.owl.web.model.user;

import com.owl.web.dao.entity.TbUser;
import com.owl.web.model.BaseQuery;

public class UserQueryReq extends BaseQuery<TbUser> {
    private UserFilterReq filter = new UserFilterReq();
    private UserSorterReq sorter = new UserSorterReq();

    @Override
    public UserFilterReq getFilter() {
        return filter;
    }

    public UserQueryReq setFilter(UserFilterReq filter) {
        this.filter = filter;
        return this;
    }

    @Override
    public UserSorterReq getSorter() {
        return sorter;
    }

    public UserQueryReq setSorter(UserSorterReq sorter) {
        this.sorter = sorter;
        return this;
    }
}
