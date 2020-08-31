package kr.co.rian.apmax.agent.config;

import kr.co.rian.apmax.agent.utility.CodeUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Getter
public final class TargetMethod {
  
  protected static final Map<Class<?>, TargetMethod> CONTAINER =
      new ConcurrentHashMap<Class<?>, TargetMethod>();
  
  static {
    // 설정에 등록된 추출 대상 메서드들을 초기화 해놔요.
    final Set<String> targetMethodSignatures = CodeUtils.splitAndToSet(Config.getTargetMethodSignatures());
    for (final String targetMethodSignature : targetMethodSignatures) {
      new TargetMethod(targetMethodSignature);
    }
  }
  
  private Class<?> clazz;
  private Method method;
  
  
  private TargetMethod(final String fullQualifiedClassNameAndMethodSignature) {
    final int i = fullQualifiedClassNameAndMethodSignature.lastIndexOf('/');
    final String className = fullQualifiedClassNameAndMethodSignature.substring(0, i - 1);
    
    try {
      clazz = Class.forName(className);
      
      int j = fullQualifiedClassNameAndMethodSignature.indexOf('(');
      method = clazz.getMethod(fullQualifiedClassNameAndMethodSignature.substring(i, j - 1));
      
      CONTAINER.put(clazz, this);
    }
    catch (ClassNotFoundException e) {
      logger.error(e.getMessage(), e);
    }
    catch (NoSuchMethodException e) {
      logger.error(e.getMessage(), e);
    }
  }
  
  public static boolean has(String className) {
    try {
      final Class<?> clazz = Class.forName(className.replace('/', '.'));
      return CONTAINER.containsKey(clazz);
    }
    catch (ClassNotFoundException e) {
      return false;
    }
  }
  
  public static TargetMethod get(Class<?> clazz) {
    return CONTAINER.get(clazz);
  }
  
}
