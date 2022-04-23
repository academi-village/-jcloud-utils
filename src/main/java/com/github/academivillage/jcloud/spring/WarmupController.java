package com.github.academivillage.jcloud.spring;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Must be imported by all microservices except those on App Engine Standard Java 8.
 */
@RestController
@RequestMapping(path = "/_ah")
public class WarmupController {

    @GetMapping("/warmup")
    public String warmup() {
        return "Warmup App";
    }
}
