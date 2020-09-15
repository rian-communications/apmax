package kr.co.rian.apmax.agent.asm.web.servlet;

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
 		if (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN) {
      mv.visitMethodInsn(
          Opcodes.INVOKESTATIC,
          CHASER_CLASS_NAME,
          "emit",
          "()V",
          false);
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
