package kr.co.rian.apmax.agent;

import kr.co.rian.apmax.agent.config.Config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.instrument.Instrumentation;

public class Bootstrap {
  
  static {
    Config.configure();
    
    
  }
  
  private Bootstrap() {
  }
  
  /**
   * JVMTI(JVM Tool Interface)로 APMAX Agent를 시작해요.
   *
   * @param agentName       VM Options 를 설정할 때, "-javaagent:jarpath=agentName" 이렇게 사용해요.
   * @param instrumentation 기본 도구에요.
   */
  public static void premain(String agentName, Instrumentation instrumentation) {
    welcome();

    instrumentation.addTransformer(
        new APMAXAgentTransformer(),
        true
    );
  }

  public static void agentmain(String options, Instrumentation instrumentation) {
    System.err.printf("Called agentmain(\"%s\" :options, \"%s\" :instrumentation)",
        options,
        instrumentation.toString());
  }
  
  private static void welcome() {
    final InputStream in = ClassLoader.getSystemResourceAsStream("apmax-agent-logo");
    
    if (in != null) {
      final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
      
      try {
        String line;
        while ((line = reader.readLine()) != null) {
          System.out.println(line);
        }
      }
      catch (IOException e) {
        // no work
      }
    }
  }
  
}
