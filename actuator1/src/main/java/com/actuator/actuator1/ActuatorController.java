package com.actuator.actuator1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
@RestController
@RequestMapping("/key")
public class ActuatorController {
 
    @GetMapping("/add")
    public String makeUrl(HttpServletRequest request) {
        return "well" ;
        // return request.getRequestURL().toString() + "?" + request.getQueryString();
    }
}
