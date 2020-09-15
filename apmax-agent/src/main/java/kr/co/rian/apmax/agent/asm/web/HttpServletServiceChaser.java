package kr.co.rian.apmax.agent.asm.web;

import kr.co.rian.apmax.agent.chaser.SeizedBooty;
import kr.co.rian.apmax.agent.chaser.SeizedBooty.Stack;
import kr.co.rian.apmax.agent.config.Config;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpServletServiceChaser {
  
  private static final ThreadLocal<SeizedBooty.Builder> CHASER =
      new ThreadLocal<SeizedBooty.Builder>();
  
  public static void swipe(HttpServletRequest request) {
    System.out.println("swipe ^");
    final SeizedBooty.Builder builder = SeizedBooty.newBuilder();
    CHASER.set(builder);
    
    builder.setId(Config.getId())
        .setTimestamp(System.currentTimeMillis())
        .setUri(request.getRequestURI());
    
    fillParametersAndHeaders(request, builder);
    
    System.out.println("swipe $");
  }
  
  public static void emit() {
    final SeizedBooty.Builder builder = CHASER.get();
    if (builder != null) {
      final SeizedBooty webChaser = builder.build();
      System.out.printf("ServletServiceChaser.emit() : \n%s\n------------\n",
          webChaser.toString());
      
      CHASER.remove();
    }
  }
  
  public static void addMethodStack(String signature, long elapsed) {
    final SeizedBooty.Builder builder = CHASER.get();
    if (builder != null) {
      builder.addStacks(
          Stack.newBuilder()
              .setSignature(signature)
              .setElapsed((int) (builder.getTimestamp() - elapsed)).build()
      );
    }
  }
  
  private static void fillParametersAndHeaders(HttpServletRequest request, SeizedBooty.Builder builder) {
    @SuppressWarnings("unchecked") final Map<String, String> parameters = request.getParameterMap();
    builder.putAllParameters(parameters);
    
    @SuppressWarnings("unchecked") final Enumeration<String> headerNames = request.getHeaderNames();
    while (headerNames.hasMoreElements()) {
      final String headerName = headerNames.nextElement();
      builder.putHeaders(headerName, request.getHeader(headerName));
    }
  }
  
}
