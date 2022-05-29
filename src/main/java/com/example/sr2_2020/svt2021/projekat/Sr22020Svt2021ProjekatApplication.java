package com.example.sr2_2020.svt2021.projekat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class Sr22020Svt2021ProjekatApplication {

    public static void main(String[] args) {

        SpringApplication.run(Sr22020Svt2021ProjekatApplication.class, args);
    }

}
