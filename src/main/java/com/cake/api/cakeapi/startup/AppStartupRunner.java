package com.cake.api.cakeapi.startup;

import com.cake.api.cakeapi.service.ICakeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AppStartupRunner implements CommandLineRunner {

    @Autowired
    ICakeService employeeService;

    @Override
    public void run(String...args) throws Exception {
        log.info("Run method is executed");
        employeeService.loadEmployeeData();
        log.info("Run method is executed - completed");
    }
}