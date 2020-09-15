package kr.co.rian.apmax.agent.asm.web;

import kr.co.rian.apmax.agent.config.Config;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.LocalVariablesSorter;

public class HttpServletServiceMethodVisitor extends LocalVariablesSorter {
  
  private static final String CHASER_CLASS_NAME =
      HttpServletServiceChaser.class.getName().replace('.', '/');
  
  public HttpServletServiceMethodVisitor(MethodVisitor visitor, int access, String descriptor) {
    super(Config.ASM_VERSION, access, descriptor, visitor);
  }
  
  @Override
  public void visitCode() {
    mv.visitVarInsn(Opcodes.ALOAD, 1);
    mv.visitMethodInsn(
        Opcodes.INVOKESTATIC,
        CHASER_CLASS_NAME,
        "swipe",
        "(Ljavax/servlet/http/HttpServletRequest;)V",
        false);

    mv.visitCode();
  }
  
  @Override
 	public void visitInsn(int opcode) {
    System.out.printf("SerlvetserviceMethodVisitor.visitInsn(opcode: %d) ^\n", opcode);
    
 		if ((opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN)) {
      System.out.printf("SerlvetserviceMethodVisitor.visitInsn(opcode: %d) #1\n", opcode);
      mv.visitMethodInsn(
          Opcodes.INVOKESTATIC,
          CHASER_CLASS_NAME,
          "emit",
          "()V",
          false);
      
      System.out.printf("SerlvetserviceMethodVisitor.visitInsn(opcode: %d) #2\n", opcode);
 		}

 		mv.visitInsn(opcode);
 		
    System.out.printf("ServletServiceMethodVisitor.visitInsn(opcode: %d) $\n", opcode);
 	}
 	
 	@Override
 	public void visitMaxs(int maxStack, int maxLocals) {
    System.out.printf("visitMaxs(maxStack: %d, maxLocals: %d) ^\n", maxLocals, maxLocals);

    mv.visitMethodInsn(
        Opcodes.INVOKESTATIC,
        CHASER_CLASS_NAME,
        "emit",
        "()V",
        false);
 		
    mv.visitMaxs(maxStack + 8, maxLocals + 2);
    System.out.printf("visitMaxs(maxStack: %d, maxLocals: %d) $\n", maxLocals, maxLocals);
 	}
  
}
