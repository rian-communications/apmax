package kr.co.rian.apmax.agent.asm.web.chaser;

import kr.co.rian.apmax.agent.Config;
import kr.co.rian.apmax.agent.asm.AgentLinkageAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.LocalVariablesSorter;

public class ChaserAdapter extends ClassVisitor implements AgentLinkageAdapter, Opcodes {
  
  private final String className;
  private boolean isInterface;
  
  public ChaserAdapter(String className, byte[] classfileBuffer) {
    super(Config.ASM_VERSION);
    this.className = className;
    
    final ClassReader reader = new ClassReader(classfileBuffer);
    final ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
    this.cv = writer;

    reader.accept(this, ClassReader.EXPAND_FRAMES);
  }
  
  @Override
  public byte[] toByteArray() {
    return ((ClassWriter) cv).toByteArray();
  }
  
  @Override
  public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
    cv.visit(version, access, name, signature, superName, interfaces);
    isInterface = (access & ACC_INTERFACE) != 0;
  }
  
  @Override
  public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
    MethodVisitor visitor = super.visitMethod(access, name, descriptor, signature, exceptions);
    
    if (!isInterface
        && !"<init>".equals(name)
        && null != visitor
        && Opcodes.ACC_PUBLIC == access) {
      visitor =
          new ChaserTargetMethodVisitor(
              visitor,
              access,
              descriptor,
              className,
              name
          );
    }
    
    return visitor;
  }
  
  private static class ChaserTargetMethodVisitor extends LocalVariablesSorter {
    
    private final String className;
    private final String methodName;
    
    public ChaserTargetMethodVisitor(MethodVisitor visitor, int access, String descriptor, String className, String methodName) {
      super(Config.ASM_VERSION, access, descriptor, visitor);
      
      this.className = className;
      this.methodName = methodName;
    }
  
    @Override
    public void visitCode() {
      mv.visitCode();
      System.out.printf("<< %s#%s >>%n", className, methodName);
      
      if (!"shout".equals(methodName)) return;
      
      mv.visitFieldInsn(
          GETSTATIC,
          "java/lang/System",
          "out",
          "Ljava/io/PrintStream;"
      );
      mv.visitLdcInsn("s-----------------------");
      mv.visitMethodInsn(
          INVOKEVIRTUAL,
          "java/io/PrintStream",
          "println",
          "(Ljava/lang/String;)V",
          false
      );

      

      mv.visitFieldInsn(
          GETSTATIC,
          "java/lang/System",
          "out",
          "Ljava/io/PrintStream;"
      );
      mv.visitIntInsn(ILOAD, 1);
      mv.visitMethodInsn(
          INVOKEVIRTUAL,
          "java/io/PrintStream",
          "println",
          "(I)V",
          false
      );

      mv.visitFieldInsn(
          GETSTATIC,
          "java/lang/System",
          "out",
          "Ljava/io/PrintStream;"
      );
      mv.visitLdcInsn("e-----------------------");
      mv.visitMethodInsn(
          INVOKEVIRTUAL,
          "java/io/PrintStream",
          "println",
          "(Ljava/lang/String;)V",
          false
      );
    }
  }
  
}
