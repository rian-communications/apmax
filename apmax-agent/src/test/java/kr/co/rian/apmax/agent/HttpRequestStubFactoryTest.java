package kr.co.rian.apmax.agent;

import kr.co.rian.apmax.protocol.client.HttpRequest;

class HttpRequestStubFactoryTest {
  
  private static final String IP = "localhost";
  private static final int PORT = 3210;
  
  public static void main(String[] args) {
    final HttpRequestStubFactory factory = new HttpRequestStubFactory(IP, PORT);
    final HttpRequestClient client = new HttpRequestClient(factory.getStub());
    
    HttpRequest request1 = HttpRequest.newBuilder()
        .setAgentId("test-request1-agent-id")
        .setUri("test-request1-uri")
        .build();
    
    client.send(request1);
  }
  
}
