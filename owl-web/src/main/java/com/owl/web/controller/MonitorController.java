package com.owl.web.controller;

import com.owl.web.model.ResponseResult;
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
        return new ResponseResult().setResult(monitorService.metric(name));
    }

    @PostMapping("monitor/{name}/history")
    public ResponseResult history(@PathVariable("name") String name,
                                  @RequestBody HistoryQueryReq req) {
        req.getFilter().setIntegrationName(name);
        return new ResponseResult().setResult(monitorService.history(req));
    }
}
