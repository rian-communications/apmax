package kr.co.rian.apmax.agent.port.performance;

import com.google.common.util.concurrent.ListenableFuture;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import kr.co.rian.apmax.agent.Commons;
import kr.co.rian.apmax.agent.Commons.Noop;
import kr.co.rian.apmax.agent.config.Config;
import kr.co.rian.apmax.agent.performance.SystemPerformance;
import kr.co.rian.apmax.agent.performance.SystemPerformance.Disk;
import kr.co.rian.apmax.agent.performance.SystemPerformanceServiceGrpc;
import kr.co.rian.apmax.agent.performance.SystemPerformanceServiceGrpc.SystemPerformanceServiceFutureStub;
import kr.co.rian.apmax.agent.port.NoopListenableService;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static kr.co.rian.apmax.agent.performance.SystemPerformance.Memory;

public class SystemPerformanceWorker implements Runnable {
  
  private static final ManagedChannel channel;
  private static final SystemPerformanceServiceFutureStub stub;
  
  static {
    final ManagedChannelBuilder<?> builder = ManagedChannelBuilder
        .forAddress(Config.getServerHost(), Config.getServerPort())
        .executor(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()))
        .usePlaintext();
    
    channel = builder.build();
    stub = SystemPerformanceServiceGrpc.newFutureStub(channel);
  }
  
  @Override
  public void run() {
    while (!channel.isTerminated() && !channel.isShutdown()) {
      final long[] disk = SystemPerformanceService.getDisk();
      final long[] memory = SystemPerformanceService.getMemory();
      final long[] network = SystemPerformanceService.getNetwork();
      
      final ListenableFuture<Noop> collect = stub.collect(
          SystemPerformance.newBuilder()
              .setId(Config.getId())
              .setTimestamp(System.currentTimeMillis())
              .setCpu(SystemPerformanceService.getCpu())
              .setDisk(Disk.newBuilder().setTotal(disk[0]).setFree(disk[1]).build())
              .setMemory(Memory.newBuilder().setTotal(memory[0]).setFree(memory[1]).build())
              .setNetwork(SystemPerformance.Network.newBuilder().setSent(network[1]).setReceived(network[2]).build())
              .build()
      );
      
      NoopListenableService.listening(collect);
      
      try {
        TimeUnit.SECONDS.sleep(1);
      }
      catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        e.printStackTrace(System.err);
      }
    }
    
  }
  
}
