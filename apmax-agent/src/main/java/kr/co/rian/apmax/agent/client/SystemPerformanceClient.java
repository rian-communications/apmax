package kr.co.rian.apmax.agent.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import kr.co.rian.apmax.agent.config.Config;
import kr.co.rian.apmax.agent.system.SystemPerformanceDeliveryGrpc.SystemPerformanceDeliveryStub;

import java.util.concurrent.TimeUnit;

import static kr.co.rian.apmax.agent.system.SystemPerformanceDeliveryGrpc.newStub;

public class SystemPerformanceClient implements Runnable {
  
  private final ManagedChannel channel;
  private final SystemPerformanceDeliveryStub stub;
  
  public SystemPerformanceClient(ManagedChannel channel, SystemPerformanceDeliveryStub stub) {
    this.channel = ManagedChannelBuilder
        .forAddress(Config.getServerHost(), Config.getServerPort())
        .usePlaintext()
        .build();

    this.stub = newStub(this.channel);
  }
  
  public void shutdown() {
    try {
      channel.awaitTermination(5, TimeUnit.SECONDS);
    }
    catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void run() {
    while (true) {
      stub.deliver(null);
      
      try {
        TimeUnit.SECONDS.sleep(1);
      }
      catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
  
}
