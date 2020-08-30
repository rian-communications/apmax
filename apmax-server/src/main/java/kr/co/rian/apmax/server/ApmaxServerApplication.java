package kr.co.rian.apmax.server;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class ApmaxServerApplication implements CommandLineRunner {
  
  public static void main(String[] args) {
    SpringApplication.run(ApmaxServerApplication.class, args);
  }
  
  @Override
  public void run(String... args) throws IOException, InterruptedException {
    //ClientServiceCollector.start();
  }

}
