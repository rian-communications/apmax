package kr.co.rian.apmax.agent;

import kr.co.rian.apmax.agent.config.Config;
import kr.co.rian.apmax.agent.config.TargetMethod;
import kr.co.rian.apmax.agent.visitor.SimpleTargetVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public class APMAXAgentTransformer implements ClassFileTransformer {
  
  @Override
  public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
    
    final ClassReader reader = new ClassReader(classfileBuffer);
    
//    final ClassWriter writer = new ClassWriter()
    
    if (TargetMethod.has(className)) {
      reader.accept(new SimpleTargetVisitor(), 0);
      
    }
    
    if (!isMonitoredClass(className)) {
      return classfileBuffer;
    }
    
    return classfileBuffer;
  }
  
  private boolean isMonitoredClass(final String className) {
    for (final String pkg : Config.getPackages()) {
      if (className.startsWith(pkg)) {
        return true;
      }
    }
    
    return false;
  }
}
