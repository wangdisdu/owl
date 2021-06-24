package com.owl.web.controller;

import com.owl.web.model.ResponseResult;
import com.owl.web.model.integration.IntegrationQuery;
import com.owl.web.model.integration.IntegrationReq;
import com.owl.web.service.IntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class IntegrationController extends V1Controller {

    @Autowired
    private IntegrationService integrationService;

    @GetMapping("integration/builder/list")
    public ResponseResult builders() {
        return new ResponseResult().setResult(integrationService.builders());
    }

    @GetMapping("integration/builder/{name}/icon")
    public ResponseEntity<byte[]> icon(@PathVariable("name") String name) {
        byte[] bytes = integrationService.icon(name);
        if(bytes == null || bytes.length == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity
                .ok()
                .header("Content-Type","image/svg+xml; charset=UTF-8")
                .body(bytes);
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
