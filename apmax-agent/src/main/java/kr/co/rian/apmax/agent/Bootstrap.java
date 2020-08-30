package kr.co.rian.apmax.agent;

import lombok.extern.slf4j.Slf4j;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.net.URL;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

@Slf4j
public class Bootstrap {
  
  private static final String defaultConfigPath = "apmax-agent-config.properties";
  
  /**
   * JVMTI(JVM Tool Interface)로 APMAX Agent를 시작해요.
   *
   * @param options         -javaagent:jarpath=options 의 options 값으로 설정파일을 경로로 이용해요.
   * @param instrumentation 기본 도구에요.
   */
  public static void premain(String options, Instrumentation instrumentation) throws IOException {
    // 설정파일을 통해서 초기 설정을 구성해요.
    new Config(options == null || "".equals(options.trim())
        ? defaultConfigPath
        : options);
    
    instrumentation.addTransformer(new Transformer());
  }
  
  
  public static class Config {
    
    public static final String CLASSPATH_PREFIX = "classpath:";
    
    private static String name;
    private static Set<String> packages;
    private static String serverHost;
    private static int serverPort;
    
  
    private Config(String path) throws IOException {
      final Properties props = loadConfig(path);
      name = props.getProperty("application.name");
      packages = splitAndToSet(props.getProperty("application.packages"));
      serverHost = props.getProperty("server.host");
      serverPort = Integer.parseInt(props.getProperty("server.port"));
      
      final Set<String> targetMethodSignatures = splitAndToSet(props.getProperty("application.target-method-signatures"));
      for (final String targetMethodSignature : targetMethodSignatures) {
        TargetMethod.addMap(targetMethodSignature);
      }
    }
    
    
    public static String getName() {
      return name;
    }
    
    public static Set<String> getPackages() {
      return packages;
    }
    
    public static Set<TargetMethod> getTargetMethod(Class<?> clazz) {
      return TargetMethod.MAP.get(clazz);
    }
    
    public static String getServerHost() {
      return serverHost;
    }
    
    public static int getServerPort() {
      return serverPort;
    }
    
    private Set<String> splitAndToSet(final String values) {
      return new HashSet<String>(Arrays.asList("[\\s,|]+"));
    }
    
    private Properties loadConfig(String path) throws IOException {
      final InputStream in;
      
      if (!path.startsWith(CLASSPATH_PREFIX)) {
        in = new FileInputStream(path);
      }
      else {
        final String propFileName = path.replaceFirst(CLASSPATH_PREFIX, "");
        final URL propFileUrl = ClassLoader.getSystemResource(propFileName);
        
        in = propFileUrl.openStream();
      }
      
      
      final Properties properties = new Properties();
      properties.load(in);
      return properties;
    }
    
  }
  
}
