package com.example.todolist;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
@Controller
public class TodoController {

    @GetMapping("/todolist")
    public String todolist() {
        return "todolist";
    }
}
