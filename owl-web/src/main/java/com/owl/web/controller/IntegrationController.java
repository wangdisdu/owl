package com.owl.web.controller;

import com.owl.web.model.ResponseResult;
import com.owl.web.model.integration.IntegrationQuery;
import com.owl.web.model.integration.IntegrationReq;
import com.owl.web.service.IntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class IntegrationController extends V1Controller {

    @Autowired
    private IntegrationService integrationService;

    @GetMapping("integration/builder/list")
    public ResponseResult builders() {
        return new ResponseResult().setResult(integrationService.builders());
    }

    @GetMapping("integration")
    public ResponseResult list() {
        return new ResponseResult().setResult(integrationService.list());
    }

    @PostMapping("integration")
    public ResponseResult create(@RequestBody IntegrationReq req) {
        return new ResponseResult().setResult(integrationService.create(req));
    }

    @PutMapping("integration/{name}")
    public ResponseResult update(@PathVariable("name") String name,
                                 @RequestBody IntegrationReq req) {
        req.setName(name);
        return new ResponseResult().setResult(integrationService.update(req));
    }

    @DeleteMapping("integration/{name}")
    public ResponseResult delete(@PathVariable("name") String name) {
        return new ResponseResult().setResult(integrationService.delete(name));
    }

    @GetMapping("integration/{name}")
    public ResponseResult get(@PathVariable("name") String name) {
        return new ResponseResult().setResult(integrationService.get(name));
    }

    @GetMapping("integration/{name}/schema")
    public ResponseResult schema(@PathVariable("name") String name) {
        return new ResponseResult().setResult(integrationService.schema(name));
    }

    @PostMapping("integration/{name}/query")
    public ResponseResult query(@PathVariable("name") String name,
                                @RequestBody IntegrationQuery req) {
        req.setName(name);
        return new ResponseResult().setResult(integrationService.query(req));
    }
}
