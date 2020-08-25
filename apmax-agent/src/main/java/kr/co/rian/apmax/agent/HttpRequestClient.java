package kr.co.rian.apmax.agent;

import io.grpc.stub.StreamObserver;
import kr.co.rian.apmax.protocol.client.ClientServiceGrpc.ClientServiceStub;
import kr.co.rian.apmax.protocol.client.HttpRequest;

public class HttpRequestClient {
  
  private final ClientServiceStub stub;
  
  public HttpRequestClient(ClientServiceStub stub) {
    this.stub = stub;
  }
  
  public void send(HttpRequest request) {
    stub.collectRequest(request, new StreamObserver<HttpRequest>() {
      public void onNext(HttpRequest request) {
        System.out.println("send(HttpRequest) ==> onNext(HttpRequest)");
        System.out.println(request.toString());
      }
      
      public void onError(Throwable throwable) {
        System.out.println("send(HttpRequest) ==> onError(Throwable)");
        throwable.printStackTrace();
        
      }
      
      public void onCompleted() {
        System.out.println("send(HttpRequest) ==> onCompleted()");
      }
    });
  }
  
}
