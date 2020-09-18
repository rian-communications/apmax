package kr.co.rian.apmax.agent.asm.web.chaser;

import kr.co.rian.apmax.agent.Config;
import kr.co.rian.apmax.agent.asm.AgentLinkageAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.io.PrintStream;

public class HttpServletChaserAdapter extends ClassVisitor implements AgentLinkageAdapter, Opcodes {
  
  private final ClassReader reader;
  private final String className;
  private boolean isInterface;
  
  
  public HttpServletChaserAdapter(String className, byte[] classfileBuffer) {
    super(Config.ASM_VERSION);
    
    this.reader = new ClassReader(classfileBuffer);
    
    this.cv = new ClassWriter(reader, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
    this.className = className;
    
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
    MethodVisitor visitor = cv.visitMethod(access, name, descriptor, signature, exceptions);
    
    if (!isInterface
        && !"<init>".equals(name)
        && null != visitor
        && Opcodes.ACC_PUBLIC == access) {
      visitor = new HttpServletChaserMethodVisitor(
          visitor,
          access,
          descriptor,
          className,
          name
      );
    }
    
    return visitor;
  }
  
  private static class HttpServletChaserMethodVisitor extends MethodVisitor implements Opcodes {
    
    private final String className;
    private final String methodName;
    private final Type[] arguments;
    
    public HttpServletChaserMethodVisitor(MethodVisitor visitor, int access, String descriptor, String className, String methodName) {
      super(Config.ASM_VERSION, visitor);
      
      this.className = className;
      this.methodName = methodName;
      this.arguments = Type.getArgumentTypes(descriptor);
    }
    
    private void argumentsToVisitVarInsn(int argumentArrayIndex) {
      mv.visitIntInsn(BIPUSH, argumentArrayIndex - 1);
      mv.visitTypeInsn(ANEWARRAY, Type.getInternalName(Object.class));
      mv.visitVarInsn(ASTORE, argumentArrayIndex);
      
      int i = 1;
      for (Type type : arguments) {
        switch (type.getSort()) {
          case Type.BOOLEAN:
          case Type.CHAR:
          case Type.BYTE:
          case Type.SHORT:
          case Type.INT:
          case Type.FLOAT:
            mv.visitVarInsn(ALOAD, argumentArrayIndex);
            mv.visitIntInsn(BIPUSH, i - 1);
            mv.visitVarInsn(ILOAD, i);
            mv.visitMethodInsn(
                INVOKESTATIC,
                Type.getInternalName(Integer.class),
                "valueOf",
                "(I)Ljava/lang/Integer;",
                false
            );
            mv.visitInsn(AASTORE);
            break;
          case Type.LONG:
            mv.visitVarInsn(ALOAD, argumentArrayIndex);
            mv.visitIntInsn(BIPUSH, i - 1);
            mv.visitVarInsn(LLOAD, i);
            mv.visitMethodInsn(
                INVOKESTATIC,
                Type.getInternalName(Long.class),
                "valueOf",
                "(J)Ljava/lang/Long;",
                false
            );
            mv.visitInsn(AASTORE);
            break;
          case Type.DOUBLE:
            mv.visitVarInsn(ALOAD, argumentArrayIndex);
            mv.visitIntInsn(BIPUSH, i - 1);
            mv.visitVarInsn(DLOAD, i);
            mv.visitMethodInsn(
                INVOKESTATIC,
                Type.getInternalName(Double.class),
                "valueOf",
                "(D)Ljava/lang/Double;",
                false
            );
            mv.visitInsn(AASTORE);
            break;
          case Type.OBJECT:
            mv.visitVarInsn(ALOAD, argumentArrayIndex);
            mv.visitIntInsn(BIPUSH, i - 1);
            mv.visitVarInsn(ALOAD, 2);
            mv.visitInsn(AASTORE);
            break;
          default:
            throw new IllegalArgumentException("unknown type: " + type);
        }
        
        i++;
      }
    }
    
    @Override
    public void visitCode() {
      System.out.printf("<< %s#%s >>%n", className, methodName);

      mv.visitVarInsn(ALOAD, 0);
      mv.visitLdcInsn(className + "#" + methodName);

      if (arguments.length == 0) {
        mv.visitInsn(ACONST_NULL);
      }
      else {
        final int argumentArrayIndex = arguments.length + 1;
        argumentsToVisitVarInsn(argumentArrayIndex);
        mv.visitVarInsn(ALOAD, argumentArrayIndex);
      }
      mv.visitMethodInsn(
          INVOKESTATIC,
          HttpServletChaser.INTERNAL_NAME,
          "seize",
          "(Ljava/lang/String;[Ljava/lang/Object;)V",
          false
      );
      
      mv.visitFieldInsn(
          GETSTATIC,
          Type.getInternalName(System.class),
          "out",
          Type.getDescriptor(PrintStream.class)
      );
      mv.visitLdcInsn("Hello!");
      mv.visitMethodInsn(
          INVOKEVIRTUAL,
          Type.getInternalName(PrintStream.class),
          "println",
          "(Ljava/lang/String;)V",
          false
      );
      
      mv.visitCode();
    }
    
    @Override
    public void visitInsn(int opcode) {
      mv.visitInsn(opcode);
      
      if (IRETURN <= opcode && RETURN >= opcode) {
        mv.visitMaxs(0, 0);
        mv.visitEnd();
      }
    }
    
  }
  
}
