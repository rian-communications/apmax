package kr.co.rian.apmax.agent.config;

import kr.co.rian.apmax.agent.utility.CodeUtils;
import lombok.Getter;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public final class TargetMethod {
  
  protected static final Map<String, TargetMethod> CONTAINER =
      new ConcurrentHashMap<String, TargetMethod>();

  private Class<?> clazz;
  private Method method;
  
  
  protected TargetMethod(final String signature) {
    final int methodPosition = signature.indexOf('.');
    final String className = signature.substring(0, methodPosition);
    final int parameterPosition = signature.indexOf('(');
    final String methodName = signature.substring(methodPosition + 1, parameterPosition);
    final String parameters = signature.substring(parameterPosition);
    
//    try {
//      clazz = Class.forName(className.replace('/', '.'));
//      method = CodeUtils.getMethod(clazz, methodName, parameters);
  
      CONTAINER.put(className, this);
//    }
//    catch (ClassNotFoundException e) {
//      e.printStackTrace(System.err);
//    }
  }

  public static boolean has(String className) {
    return CONTAINER.containsKey(className);
  }
  
  public static TargetMethod get(Class<?> clazz) {
    return CONTAINER.get(clazz);
  }
  
}
