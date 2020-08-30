//package kr.co.rian.apmax.server;
//
//import io.grpc.Server;
//import io.grpc.ServerBuilder;
//import io.grpc.stub.StreamObserver;
//import kr.co.rian.apmax.protocol.client.ClientServiceGrpc.ClientServiceImplBase;
//import kr.co.rian.apmax.protocol.client.HttpRequest;
//
//import java.io.IOException;
//
//public class ClientServiceCollector extends ClientServiceImplBase {
//
//  @Override
//  public void collectRequest(HttpRequest request, StreamObserver<HttpRequest> responseObserver) {
//
//    System.out.println("1st argument HttpRequest:");
//    System.out.println(request.toString());
//
//    final HttpRequest builder = HttpRequest.newBuilder()
//        .setAgentId("test-agent-id")
//        .setUri("test-uri")
//        .build();
//    System.out.println("onNext(HttpRequest)");
//    responseObserver.onNext(builder);
//
//    System.out.println("onCompleted()");
//    responseObserver.onCompleted();
//  }
//
//  public static void start() throws IOException, InterruptedException {
//    final Server server = ServerBuilder.forPort(3210)
//        .addService(new ClientServiceCollector())
//        .build();
//
//    server.start();
//
//    System.out.println("gRPC Server start");
//
//    server.awaitTermination();
//  }
//
//  public static void main(String[] args) throws IOException, InterruptedException {
//    start();
//  }
//
//}
