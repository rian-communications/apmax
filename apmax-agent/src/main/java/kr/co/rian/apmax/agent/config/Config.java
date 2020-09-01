package kr.co.rian.apmax.agent.config;

import kr.co.rian.apmax.agent.utility.CodeUtils;
import org.objectweb.asm.Opcodes;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public final class Config {
  
  public static final int ASM_VERSION = Opcodes.ASM5;
  public static final String APMAX_CONFIG_KEY = "apmax.agent.config";
  
  protected static final Set<String> DEFAULT_JDBC_CLASSES = new HashSet<String>();
  protected static final Set<String> DEFAULT_SERVLET_CLASSES = new HashSet<String>();
  
  private static final Properties props = new Properties();
  private static final String CLASSPATH_PREFIX = "classpath:";
  private static final String DEFAULT_CONFIG_PATH = "classpath:apmax-agent-config.properties";
  
  static {
    DEFAULT_SERVLET_CLASSES.add("javax/servlet/http/HttpServlet");
    
    DEFAULT_JDBC_CLASSES.add("javax/sql/Statement");
    DEFAULT_JDBC_CLASSES.add("javax/sql/PreparedStatement");
    DEFAULT_JDBC_CLASSES.add("javax/sql/CallableStatement");
    
    // 설정파일을 통해서 초기 설정을 구성해요.
    configure();
  }
  
  
  private Config() {
  }
  
  
  public static String getName() {
    return props.getProperty("application.name");
  }
  
  public static Set<String> getPackages() {
    return CodeUtils.splitAndToSet(props.getProperty("application.packages"));
  }
  
  public static String getTargetMethodSignatures() {
    return props.getProperty("application.target-method-signatures");
  }
  
  public static TargetMethod getTargetMethod(Class<?> clazz) {
    return TargetMethod.CONTAINER.get(clazz);
  }
  
  public static String getServerHost() {
    return props.getProperty("server.host");
  }
  
  public static int getServerPort() {
    return Integer.parseInt(props.getProperty("server.port"));
  }
  
  
  public static void configure() {
    String path = System.getProperty(APMAX_CONFIG_KEY);
    if (path == null || "".equals(path.trim())) {
      path = DEFAULT_CONFIG_PATH;
    }
    
    InputStream in = null;
    
    try {
      if (!path.startsWith(CLASSPATH_PREFIX)) {
        in = new FileInputStream(path);
      }
      else {
        final String propFileName = path.replaceFirst(CLASSPATH_PREFIX, "");
        final URL propFileUrl = ClassLoader.getSystemResource(propFileName);
        
        in = propFileUrl.openStream();
        props.load(in);
        
        composeTargetMethod();
      }
    }
    catch (IOException e) {
      throw new ConfigLoadingError(e);
    }
    finally {
      if (in != null) {
        try {
          in.close();
        }
        catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
  
  private static void composeTargetMethod() {
    // 설정에 등록된 추출 대상 메서드들을 초기화 해놔요.
    final Set<String> targetMethodSignatures = CodeUtils.splitAndToSet(Config.getTargetMethodSignatures());
    for (final String targetMethodSignature : targetMethodSignatures) {
      new TargetMethod(targetMethodSignature);
    }

  }
  
  
  private static class ConfigLoadingError extends Error {
    
    public ConfigLoadingError(Exception e) {
      e.printStackTrace(System.err);
    }
    
  }
  
}
