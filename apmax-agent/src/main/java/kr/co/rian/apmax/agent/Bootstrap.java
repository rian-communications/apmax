package kr.co.rian.apmax.agent;

import kr.co.rian.apmax.agent.config.Config;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.instrument.Instrumentation;

@Slf4j
public class Bootstrap {
  
  private Bootstrap() {
  }
  
  /**
   * JVMTI(JVM Tool Interface)로 APMAX Agent를 시작해요.
   *
   * @param options         -javaagent:jarpath=options 의 options 값으로 설정파일을 경로로 이용해요.
   * @param instrumentation 기본 도구에요.
   */
  public static void premain(String options, Instrumentation instrumentation) {
    intro();
    
    System.setProperty(Config.APMAX_CONFIG_KEY, options);
    
    instrumentation.addTransformer(
        new APMAXAgentTransformer()
    );
  }
  
  private static void intro() {
    final InputStream in = ClassLoader.getSystemResourceAsStream("apmax-agent-logo");
    
    if (in != null) {
      final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
      
      try {
        String line;
        while ((line = reader.readLine()) != null) {
          logger.info(line);
        }
      }
      catch (IOException e) {
        // no work
      }
    }
  }
  
}
