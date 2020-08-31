package kr.co.rian.apmax.agent.visitor;

import kr.co.rian.apmax.agent.config.Config;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.LocalVariablesSorter;

public class SimpleTargetVisitor extends ClassVisitor implements Opcodes {
  
  public SimpleTargetVisitor() {
    super(Config.ASM_VERSION);
  }
  
  @Override
  public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
    super.visit(version, access, name, signature, superName, interfaces);
  }
  
  @Override
  public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
    final MethodVisitor visitor = super.visitMethod(access, name, descriptor, signature, exceptions);
    return new SimpleTargetMethodVisitor(access, descriptor, visitor);
  }
  
  
  private class SimpleTargetMethodVisitor extends LocalVariablesSorter {

    protected SimpleTargetMethodVisitor(int access, String descriptor, MethodVisitor methodVisitor) {
      super(Config.ASM_VERSION, access, descriptor, methodVisitor);
    }
  
    @Override
    public void visitCode() {
      super.visitCode();
    }
  
    @Override
    public void visitInsn(int opcode) {
      if (opcode >= IRETURN && opcode <= RETURN) {
        super.visitInsn(opcode);
      }
      
      
      
      mv.visitInsn(opcode);
    }
  }
}
