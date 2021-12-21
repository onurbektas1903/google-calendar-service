package tr.com.obss.googlecalendarservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@EnableEurekaClient
@ComponentScan(value = {"tr"})
public class GoogleCalendarApplication {

  public static void main(String[] args) {
    SpringApplication.run(GoogleCalendarApplication.class, args);
  }
}
