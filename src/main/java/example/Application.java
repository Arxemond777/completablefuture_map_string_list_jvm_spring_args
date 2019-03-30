package example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        int mb = 1024 * 1024;
        System.out.println("Total memory: " + Runtime.getRuntime().maxMemory() / mb + " mb");
        SpringApplication.run(Application.class, args);
    }
}