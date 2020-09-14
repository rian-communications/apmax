package kr.co.rian.apmax.agent.config;

import kr.co.rian.apmax.agent.exception.FallDownException;

import org.objectweb.asm.Opcodes;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public final class Config {
  
  public static final int ASM_VERSION = Opcodes.ASM7;
  public static final String APMAX_CONFIG_KEY = "apmax.agent.config";
  
  public static final Set<String> DEFAULT_JDBC_CLASSES = new HashSet<String>();
  public static final Set<String> DEFAULT_SERVLET_CLASSES = new HashSet<String>();
  
  private static final Properties props = new Properties();
  
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
  
  
  public static String getId() {
    return props.getProperty("agent.id");
  }
  
  public static Set<String> getPackages() {
    return splitAndToSet(props.getProperty("agent.monitor-package-prefix"));
  }
  
  public static String getServerHost() {
    return props.getProperty("server.host", "localhost");
  }
  
  public static int getServerPort() {
    return Integer.parseInt(props.getProperty("server.port", "3506"));
  }
  
  
  public static void configure() {
    InputStream in = null;
    
    try {
      final URL propFileUrl = ClassLoader.getSystemResource("apmax-agent-config.properties");
  
      in = propFileUrl.openStream();
      props.load(in);
    }
    catch (IOException e) {
      throw new FallDownException(e);
    }
    finally {
      if (in != null) {
        try {
          in.close();
        }
        catch (IOException e) {
          e.printStackTrace(System.err);
        }
      }
    }

    if (getId() == null || "".equals(getId())) {
      try {
        props.setProperty("agent.id", InetAddress.getLocalHost().getHostName());
      }
      catch (UnknownHostException e) {
        throw new FallDownException(e);
      }
    }
  }
  
  public static Set<String> splitAndToSet(final String values) {
    return new HashSet<String>(Arrays.asList(values.split("[\\s,|]+")));
  }
  
}
