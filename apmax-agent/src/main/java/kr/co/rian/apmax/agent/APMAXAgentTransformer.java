package kr.co.rian.apmax.agent;

import kr.co.rian.apmax.agent.asm.web.servlet.HttpServletServiceAdapter;
import kr.co.rian.apmax.agent.asm.web.servlet.ServletMonitoredAdapter;
import kr.co.rian.apmax.agent.config.Config;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public class APMAXAgentTransformer implements ClassFileTransformer {
  
  @Override
  public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
    if (isServletClass(className)) {
      return new HttpServletServiceAdapter(classfileBuffer).toByteArray();
    }
    
    if (isMonitorTargetClass(className)) {
      return new ServletMonitoredAdapter(className, classfileBuffer).toByteArray();
    }
    
    return new byte[0];
  }
  
  private boolean isServletClass(final String className) {
    for (final String servletClass : Config.DEFAULT_SERVLET_CLASSES) {
      if (servletClass.startsWith(className)) {
        return true;
      }
    }
    
    return false;
  }
  
  private boolean isMonitorTargetClass(final String className) {
    for (final String pkg : Config.getPackages()) {
      if (className.startsWith(pkg)) {
        return true;
      }
    }
    
    return false;
  }
}
