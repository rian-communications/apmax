package kr.co.rian.apmax.agent;

import kr.co.rian.apmax.agent.Bootstrap.Config;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Transformer implements ClassFileTransformer {

  private static final Set<String> DEFAULT_SERVLET_CLASSES = new HashSet<String>(Arrays.asList(
      "javax/servlet/HttpService"
  ));

  private static final Set<String> DEFAULT_JDBC_CLASSES = new HashSet<String>(Arrays.asList(
      "javax/sql/Statement",
      "javax/sql/PreparedStatement",
      "javax/sql/CallableStatement"
  ));
  
  /*
      new ClassFileTransformer() {
        @Override
        public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
          if (!className.startsWith("kr/co/rian/apmax")) {
            return classfileBuffer;
          }
          
          System.out.printf("%s (%d bytes)\n", className, classfileBuffer.length);
          
          
          ClassReader reader = new ClassReader(classfileBuffer);
          ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
          
          TestTransformer transformer = new TestTransformer(writer);

          reader.accept(transformer, ClassReader.EXPAND_FRAMES);
          
          return writer.toByteArray();
        }
      }
  */
  
  @Override
  public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
    if (!isTargetClass(className)) {
      return classfileBuffer;
    }
    
    return classfileBuffer;
  }
  
  private boolean isTargetClass(final String className) {
    for (final String pkg : Config.getPackages()) {
      if (className.startsWith(pkg)) {
        return true;
      }
    }
    
    return false;
  }
}
