package com.actuator.actuator1;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
@Controller
public class ActuatorController {

    @GetMapping("/add")
    public String currency() {
        return "currency";
    }




//    public String makeUrl(HttpServletRequest request) {
//        return "currency";
//        // return request.getRequestURL().toString() + "?" + request.getQueryString();
//    }
}
