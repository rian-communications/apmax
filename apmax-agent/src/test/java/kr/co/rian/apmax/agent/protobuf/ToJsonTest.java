package kr.co.rian.apmax.agent.protobuf;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import kr.co.rian.apmax.agent.chaser.Booty;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

public class ToJsonTest {
  
  @Test
  void shouldSerialize() throws InvalidProtocolBufferException {
    final ByteBuffer longMaxBytes = ByteBuffer.allocate(8).putLong(Long.MAX_VALUE);
    final Booty.Trove trove = Booty.Trove.newBuilder()
        .setSignature("hello.hi#ni-hao")
        .putParameter("hello", ByteString.copyFrom(longMaxBytes))
        .build();
  
    System.out.println(longMaxBytes.getLong());
    
    
    
    JsonFormat.TypeRegistry typeRegistry = JsonFormat.TypeRegistry.newBuilder()
        .add(Booty.Trove.getDescriptor())
        .build();
    
    JsonFormat.Printer printer = JsonFormat.printer().usingTypeRegistry(typeRegistry);
    
    System.out.println(printer.print(trove));
    
  }
  
}
