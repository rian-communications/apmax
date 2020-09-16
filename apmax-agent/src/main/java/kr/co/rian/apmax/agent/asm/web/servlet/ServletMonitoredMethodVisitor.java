package kr.co.rian.apmax.agent.asm.web.servlet;

import kr.co.rian.apmax.agent.Config;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.LocalVariablesSorter;

public class ServletMonitoredMethodVisitor extends LocalVariablesSorter {
  
  private static final String CHASER_CLASS_NAME =
      ServletMonitoredChaser.class.getName().replace('.', '/');
  
  private final String signature;
  private int signatureIndex;
  
  public ServletMonitoredMethodVisitor(MethodVisitor visitor, int access, String descriptor, String signature) {
    super(Config.ASM_VERSION, access, descriptor, visitor);
    this.signature = signature;
  }
  
  @Override
  public void visitCode() {
    System.out.printf("visitCode() ... %s\n", signature);
    
    if (signature.endsWith("shout")) {
      mv.visitVarInsn(Opcodes.ILOAD, 1);
      mv.visitMethodInsn(
          Opcodes.INVOKESTATIC,
          CHASER_CLASS_NAME,
          "swipe",
          "(I)V",
          false);
    }
    
    mv.visitCode();
  }
  
  @Override
  public void visitParameter(String name, int access) {
    System.out.printf("visitParameter(name: %s, access: %d) ... %s\n", name, access, signature);
    super.visitParameter(name, access);
  }
  
  @Override
  public void visitInsn(int opcode) {
    if (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN) {
//      signatureIndex = newLocal(Type.getType(String.class));
//      mv.visitVarInsn(Opcodes.ASTORE, signatureIndex);
//      mv.visitVarInsn(Opcodes.ALOAD, signatureIndex);
//
//      mv.visitMethodInsn(
//          Opcodes.INVOKESTATIC,
//          CHASER_CLASS_NAME,
//          "swipe",
//          "(Ljava/lang/String;)V",
//          false);
    }
    
    mv.visitInsn(opcode);
  }
  
  @Override
  public void visitMaxs(int maxStack, int maxLocals) {
    mv.visitMethodInsn(
        Opcodes.INVOKESTATIC,
        CHASER_CLASS_NAME,
        "emit",
        "()V",
        false);
    
    mv.visitMaxs(maxStack + 1, maxLocals);
  }
  
}
