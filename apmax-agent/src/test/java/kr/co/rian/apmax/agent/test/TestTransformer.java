package kr.co.rian.apmax.agent.test;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.LocalVariablesSorter;

public class TestTransformer extends ClassVisitor implements Opcodes {
  
  public TestTransformer(ClassVisitor classVisitor) {
    super(ASM7, classVisitor);
  }
  
  @Override
  public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
    final MethodVisitor visitor = super.visitMethod(access, name, descriptor, signature, exceptions);
    
    if (visitor == null) {
      return null;
    }
    
    if ("main".equals(name) && "([Ljava/lang/String;)V".equals(descriptor)) {
      return new Transformer(access, visitor, descriptor);
    }
    
    return visitor;
  }
  
  public static void showTwo() {
    System.err.printf("MockTransformer.showTwo()\n");
  }
  
  
  public static class Transformer extends LocalVariablesSorter implements Opcodes {
    
    public Transformer(int access, MethodVisitor methodVisitor, String descriptor) {
      super(ASM7, access, descriptor, methodVisitor);
    }
    
    @Override
    public void visitCode() {
      System.err.printf("MockTransformer$Transformer.visitCode()\n");

      mv.visitMethodInsn(
          Opcodes.INVOKESTATIC,
          "kr/co/rian/apmax/agent/mock/MockTransformer",
          "showTwo",
          "()V",
          false
      );
      
      mv.visitCode();
    }
    
    @Override
    public void visitInsn(int opcode) {
      System.err.printf("MockTransformer$Transformer.visitInsn(opcode): %d\n", opcode);
      super.visitInsn(opcode);
    }
  }
}
