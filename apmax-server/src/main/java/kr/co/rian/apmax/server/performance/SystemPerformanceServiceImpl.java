package kr.co.rian.apmax.server.performance;

import io.grpc.stub.StreamObserver;
import kr.co.rian.apmax.agent.system.SystemPerformance;
import kr.co.rian.apmax.agent.Commons.Noop;
import kr.co.rian.apmax.agent.system.SystemPerformanceServiceGrpc.SystemPerformanceServiceImplBase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SystemPerformanceServiceImpl extends SystemPerformanceServiceImplBase {
  
  @Override
  public void collect(SystemPerformance request, StreamObserver<Noop> responseObserver) {
    logger.info("collect: {}", request.toString());
    
    responseObserver.onNext(
        Noop.newBuilder()
            .build()
    );
    
    responseObserver.onCompleted();
  }
}
