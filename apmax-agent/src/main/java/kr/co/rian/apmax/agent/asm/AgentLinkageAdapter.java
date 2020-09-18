package kr.co.rian.apmax.agent.asm;

import org.objectweb.asm.Type;

public interface AgentLinkageAdapter {
  
  String TYPE_OBJECT = Type.getInternalName(Object.class);
  String TYPE_STRING = Type.getInternalName(String.class);
  String DESC_STRING = Type.getDescriptor(String.class);
  
  byte[] toByteArray();
  
}
