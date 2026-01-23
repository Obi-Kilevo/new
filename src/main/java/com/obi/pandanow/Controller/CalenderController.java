package com.obi.pandanow.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("cal")
public class CalenderController {

    @GetMapping
    public String call() {
        return "calender/booking";
    }
}
