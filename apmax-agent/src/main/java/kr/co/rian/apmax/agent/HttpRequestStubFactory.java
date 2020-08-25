package kr.co.rian.apmax.agent;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import kr.co.rian.apmax.protocol.client.ClientServiceGrpc;
import kr.co.rian.apmax.protocol.client.ClientServiceGrpc.ClientServiceBlockingStub;

import static kr.co.rian.apmax.protocol.client.ClientServiceGrpc.ClientServiceFutureStub;
import static kr.co.rian.apmax.protocol.client.ClientServiceGrpc.ClientServiceStub;

public class HttpRequestStubFactory {
  
  private final ManagedChannel channel;
  private final ClientServiceStub stub;
  private final ClientServiceBlockingStub blockingStub;
  private final ClientServiceFutureStub futureStub;
  
  public HttpRequestStubFactory(final String host, final int port) {
    this.channel = ManagedChannelBuilder.forAddress(host, port)
        .usePlaintext()
        .build();
    
    this.stub = ClientServiceGrpc.newStub(channel);
    this.blockingStub = ClientServiceGrpc.newBlockingStub(channel);
    this.futureStub = ClientServiceGrpc.newFutureStub(channel);
  }
  
  public ClientServiceStub getStub() {
    return stub;
  }
  
  public ClientServiceBlockingStub getBlockingStub() {
    return blockingStub;
  }
  
  public ClientServiceFutureStub getFutureStub() {
    return futureStub;
  }
}
