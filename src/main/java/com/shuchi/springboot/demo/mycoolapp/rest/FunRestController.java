package com.shuchi.springboot.demo.mycoolapp.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FunRestController {

    // expose a new endpoint for "workout"

    @GetMapping("/workout")
    public String getDailyWorkout() {
        return "Run a hard 5k!";
    }

    @GetMapping(value = "/{name}")
    public String greet(@PathVariable("name") String name) {
        var greet = "Hi %s";
        return String.format(greet, name);
    }

    // expose a new endpoint for "fortune"

    @GetMapping("/fortune")
    public String getDailyFortune() {
        return "Fortune favors the brave";
    }

}