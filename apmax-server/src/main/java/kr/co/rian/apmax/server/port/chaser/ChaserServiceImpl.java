package kr.co.rian.apmax.server.port.chaser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import io.grpc.stub.StreamObserver;
import kr.co.rian.apmax.agent.Commons.Noop;
import kr.co.rian.apmax.agent.chaser.ChaserServiceGrpc.ChaserServiceImplBase;
import kr.co.rian.apmax.agent.chaser.WebChaser;
import kr.co.rian.apmax.agent.chaser.WebChaserOrBuilder;
import kr.co.rian.apmax.server.port.PortService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ChaserServiceImpl extends ChaserServiceImplBase {
  
  @Override
  public void collectWeb(WebChaser request, StreamObserver<Noop> responseObserver) {
    try {
      logger.info("collect web: {}", JsonFormat.printer().print(request));
    }
    catch (InvalidProtocolBufferException e) {
      e.printStackTrace();
    }
    
    PortService.noopCompleted(responseObserver);
  }
}
