package kr.co.rian.apmax.agent;

import kr.co.rian.apmax.agent.mock.MockTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.Objects;

public class Bootstrap {
  
  public static void premain(String options, Instrumentation instrumentation) {
    instrumentation.addTransformer(new ClassFileTransformer() {
      @Override
      public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        System.out.printf("%s (%d bytes)\n", className, classfileBuffer.length);
        
        if (!Objects.equals("kr/co/rian/apmax/agent/TestSimpleASM", className)) {
          return classfileBuffer;
        }
        
        ClassReader reader = new ClassReader(classfileBuffer);
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        
        MockTransformer transformer = new MockTransformer(writer);
        
        reader.accept(transformer, ClassReader.EXPAND_FRAMES);
        
        return writer.toByteArray();
      }
    }, true);
  }
  
}
