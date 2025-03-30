package org.juanrobledo.tp2mongo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
public class Tp2MongoApplication {

    public static void main(String[] args) {
        SpringApplication.run(Tp2MongoApplication.class, args);
    }

}
