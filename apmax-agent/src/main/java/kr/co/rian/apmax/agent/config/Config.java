package kr.co.rian.apmax.agent.config;

import kr.co.rian.apmax.agent.utility.CodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.objectweb.asm.Opcodes;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

@Slf4j
public final class Config {
  
  public static final int ASM_VERSION = Opcodes.ASM5;
  public static final String APMAX_CONFIG_KEY = "apmax.agent.config";
  
  protected static final Set<String> DEFAULT_JDBC_CLASSES = new HashSet<String>();
  protected static final Set<String> DEFAULT_SERVLET_CLASSES = new HashSet<String>();
  
  private static final Properties props = new Properties();
  private static final String CLASSPATH_PREFIX = "classpath:";
  private static final String DEFAULT_CONFIG_PATH = "classpath:apmax-agent-config.properties";
  
  static {
    DEFAULT_SERVLET_CLASSES.add("javax/servlet/HttpService");
    
    DEFAULT_JDBC_CLASSES.add("javax/sql/Statement");
    DEFAULT_JDBC_CLASSES.add("javax/sql/PreparedStatement");
    DEFAULT_JDBC_CLASSES.add("javax/sql/CallableStatement");
    
    // 설정파일을 통해서 초기 설정을 구성해요.
    loadConfigFile();
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
  
  
  private static void loadConfigFile() {
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
  
  
  @Slf4j
  private static class ConfigLoadingError extends Error {
    
    public ConfigLoadingError(Exception e) {
      logger.error(e.getMessage(), e);
    }
    
  }
  
}
