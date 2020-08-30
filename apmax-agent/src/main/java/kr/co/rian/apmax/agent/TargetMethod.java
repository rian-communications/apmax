package kr.co.rian.apmax.agent;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@RequiredArgsConstructor
public class TargetMethod {
  
  protected static final Map<Class<?>, Set<TargetMethod>> MAP =
      new ConcurrentHashMap<Class<?>, Set<TargetMethod>>(new HashMap<Class<?>, Set<TargetMethod>>(1));
  
  private final Class<?> clazz;
  private final Method method;
  private final String methodSignature;
  
  
  public static boolean addMap(final String fullQualifiedClassNameAndMethodSignature) {
    try {
      final TargetMethod targetMethod = getTargetMethod(fullQualifiedClassNameAndMethodSignature);
      final Class<?> key = targetMethod.getClazz();
      
      if (MAP.containsKey(key)) {
        MAP.get(key).add(targetMethod);
      }
      else {
        final Set<TargetMethod> set = new HashSet<TargetMethod>(1);
        set.add(targetMethod);
        
        MAP.put(key, set);
      }
      
      return true;
    }
    catch (NoSuchMethodException e) {
      // no work
      return false;
    }
    catch (ClassNotFoundException e) {
      // no work
      return false;
    }
  }
  
  private static TargetMethod getTargetMethod(final String fullQualifiedClassNameAndMethodSignature) throws ClassNotFoundException, NoSuchMethodException {
    int i = fullQualifiedClassNameAndMethodSignature.lastIndexOf('/');
    final String className = fullQualifiedClassNameAndMethodSignature.substring(0, i - 1);
    final Class<?> targetClass = Class.forName(className);
    
    int j = fullQualifiedClassNameAndMethodSignature.indexOf('(');
    final Method targetMethod = targetClass.getMethod(fullQualifiedClassNameAndMethodSignature.substring(i, j - 1));
    
    return new TargetMethod(targetClass, targetMethod,
        fullQualifiedClassNameAndMethodSignature.substring(j));
  }
  
}
