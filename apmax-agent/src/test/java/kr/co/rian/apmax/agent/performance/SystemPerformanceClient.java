package kr.co.rian.apmax.agent.performance;

import com.google.common.util.concurrent.ListenableFuture;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import kr.co.rian.apmax.agent.Commons.Noop;
import kr.co.rian.apmax.agent.performance.SystemPerformance.Disk;
import kr.co.rian.apmax.agent.performance.SystemPerformance.Network;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import static kr.co.rian.apmax.agent.performance.SystemPerformance.Memory;
import static kr.co.rian.apmax.agent.performance.SystemPerformance.Sensors;
import static kr.co.rian.apmax.agent.performance.SystemPerformance.newBuilder;

class SystemPerformanceClient {
  
  public static final StreamObserver<Noop> RESPONSE_OBSERVER = new StreamObserver<Noop>() {
    @Override
    public void onNext(Noop value) {
      System.err.printf("onNext(Noop): %s\n", value.toString());
    }
    
    @Override
    public void onError(Throwable t) {
      t.printStackTrace(System.err);
    }
    
    @Override
    public void onCompleted() {
      System.err.println("END!!");
    }
  };
  
  public static void main(String[] args) throws InterruptedException, ExecutionException {
    final ManagedChannel channel =
        ManagedChannelBuilder.forAddress("localhost", 3506)
            .usePlaintext()
            .build();
    
    final SystemPerformanceServiceGrpc.SystemPerformanceServiceFutureStub fstub =
        SystemPerformanceServiceGrpc.newFutureStub(channel);
    
    final SystemPerformance asp = newBuilder()
        .setId("WAS-1ST")
        .setTimestamp(System.currentTimeMillis())
        .setCpu(1.23D)
        .setDisk(Disk.newBuilder().setFree(123).setTotal(456).build())
        .setMemory(Memory.newBuilder().setFree(789).setTotal(12).build())
        .setNetwork(Network.newBuilder().setReceived(345).setSent(678).build())
        .setSensors(Sensors.newBuilder().setTemperature(9.01D).setVoltage(2.34D)
            .addFanSpeed(567)
            .addFanSpeed(890).build())
        .build();
    
    final ListenableFuture<Noop> collect = fstub.collect(asp);
    collect.addListener(new Runnable() {
      @Override
      public void run() {
        System.out.println("I'm running!!!");
      }
    }, Executors.newSingleThreadExecutor());
    
    final Noop noop = collect.get();//collect.get(10, TimeUnit.SECONDS);
    
    System.err.printf("FINISH: %s", noop.toString());
  }
  
}
