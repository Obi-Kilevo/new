package com.obi.pandanow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.obi.pandanow.Repository")  // ADD THIS LINE
@EntityScan("com.obi.pandanow.Entity")  // ADD THIS LINE
public class PandaNowApplication {

    public static void main(String[] args) {
        SpringApplication.run(PandaNowApplication.class, args);
    }

}


//package com.obi.pandanow;
//
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//
//@SpringBootApplication
//public class PandaNowApplication {
//
//    public static void main(String[] args) {
//        SpringApplication.run(PandaNowApplication.class, args);
//    }
//
//}
