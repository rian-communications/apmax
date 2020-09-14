package kr.co.rian.apmax.server.port;

import io.grpc.stub.StreamObserver;
import kr.co.rian.apmax.agent.Commons.Noop;

public interface PortService {
  
  Noop NOOP = Noop.newBuilder().build();

  static void noopCompleted(StreamObserver<Noop> streamObserver) {
    streamObserver.onNext(NOOP);
    streamObserver.onCompleted();
    
  }
  
}
