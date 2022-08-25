package local.project12.springbootdata_async_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class SpringBootDataAsyncDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDataAsyncDemoApplication.class, args);
    }

}
