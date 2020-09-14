package kr.co.rian.apmax.server.port.performance;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import io.grpc.stub.StreamObserver;
import kr.co.rian.apmax.agent.Commons.Noop;
import kr.co.rian.apmax.agent.performance.SystemPerformance;
import kr.co.rian.apmax.agent.performance.SystemPerformanceServiceGrpc.SystemPerformanceServiceImplBase;
import kr.co.rian.apmax.server.port.PortService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SystemPerformanceServiceImpl extends SystemPerformanceServiceImplBase {
  
  @Override
  public void collect(SystemPerformance request, StreamObserver<Noop> responseObserver) {
    try {
      logger.info("collect: {}", JsonFormat.printer().print(request));
    }
    catch (InvalidProtocolBufferException e) {
      e.printStackTrace();
    }
    PortService.noopCompleted(responseObserver);
  }
}
