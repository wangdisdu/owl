package com.owl.web.controller;

import com.owl.web.model.Paged;
import com.owl.web.model.ResponseResult;
import com.owl.web.model.monitor.History;
import com.owl.web.model.monitor.HistoryQueryReq;
import com.owl.web.service.MonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MonitorController extends V1Controller {

    @Autowired
    private MonitorService monitorService;

    @GetMapping("monitor/{name}/metric")
    public ResponseResult metric(@PathVariable("name") String name) {
        HistoryQueryReq req = new HistoryQueryReq();
        req.getFilter().setIntegrationName(name);
        Paged<History> paged = monitorService.metricLast(req);
        return new ResponseResult().setResult(paged.getRecords()).setTotal(paged.getTotal());
    }

    @PostMapping("monitor/{name}/history")
    public ResponseResult history(@PathVariable("name") String name,
                                  @RequestBody HistoryQueryReq req) {
        req.getFilter().setIntegrationName(name);
        Paged<History> paged = monitorService.history(req);
        return new ResponseResult().setResult(paged.getRecords()).setTotal(paged.getTotal());
    }
}
