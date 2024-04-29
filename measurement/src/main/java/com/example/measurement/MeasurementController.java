package com.example.measurement;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MeasurementController {
    @GetMapping("/measurement")
    public String measurement() {
        return "measurement";
    }
}
