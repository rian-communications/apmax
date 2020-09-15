package kr.co.rian.apmax.agent.asm.web.servlet;

import kr.co.rian.apmax.agent.chaser.SeizedBooty;
import kr.co.rian.apmax.agent.chaser.SeizedBooty.Stack;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ServletMonitoredChaser {
  
  public static void swipe(int signature) {
  
    System.out.println("ServletMonitoredChaser.swipe(): " + signature);
    
    SeizedBooty.Builder seizedBooty = HttpServletServiceChaser.CHASER.get();
    if (seizedBooty != null) {
      seizedBooty.addStacks(
          Stack.newBuilder()
//              .setSignature(signature)
//              .addAllParameters(Arrays.asList(parameters))
              .setElapsed(seizedBooty.getTimestamp() - System.currentTimeMillis())
              .build()
      );
    }
  }
  
}
