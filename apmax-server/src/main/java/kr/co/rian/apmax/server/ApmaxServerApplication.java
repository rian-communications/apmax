package kr.co.rian.apmax.server;

import io.grpc.Attributes;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerTransportFilter;
import kr.co.rian.apmax.server.port.chaser.ChaserServiceImpl;
import kr.co.rian.apmax.server.port.performance.SystemPerformanceServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.concurrent.Executors;

@SpringBootApplication
@Slf4j
public class ApmaxServerApplication implements CommandLineRunner {
  
  private static final int GRPC_DEFAULT_PORT = 3506;
  
  @Override
  public void run(String... args) throws IOException, InterruptedException {
    final Server grpcServer = ServerBuilder
        .forPort(GRPC_DEFAULT_PORT)
        .executor(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()))
        .addService(new SystemPerformanceServiceImpl())
        .addService(new ChaserServiceImpl())
        .build();
    
    grpcServer.start();
    
    logger.info("gRPC Listening on {}", GRPC_DEFAULT_PORT);
    
    grpcServer.awaitTermination();
  }
  
  
  public static void main(String[] args) {
    SpringApplication.run(ApmaxServerApplication.class, args);
  }
  
}
