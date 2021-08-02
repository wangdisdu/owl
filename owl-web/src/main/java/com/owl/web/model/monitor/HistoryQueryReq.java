package com.owl.web.model.monitor;

import com.owl.web.dao.entity.TbHistory;
import com.owl.web.model.BaseQuery;

public class HistoryQueryReq extends BaseQuery<TbHistory> {
    private HistoryFilterReq filter = new HistoryFilterReq();
    private HistorySortReq sorter = new HistorySortReq();

    @Override
    public HistoryFilterReq getFilter() {
        return filter;
    }

    public HistoryQueryReq setFilter(HistoryFilterReq filter) {
        this.filter = filter;
        return this;
    }

    @Override
    public HistorySortReq getSorter() {
        return sorter;
    }

    public HistoryQueryReq setSorter(HistorySortReq sorter) {
        this.sorter = sorter;
        return this;
    }
}
