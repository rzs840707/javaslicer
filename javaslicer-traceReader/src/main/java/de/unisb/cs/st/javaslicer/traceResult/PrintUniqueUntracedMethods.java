package de.unisb.cs.st.javaslicer.traceResult;

import java.util.HashSet;

import org.objectweb.asm.Type;

import de.hammacher.util.maps.IntegerMap;
import de.unisb.cs.st.javaslicer.common.classRepresentation.InstructionInstance;
import de.unisb.cs.st.javaslicer.common.classRepresentation.instructions.MethodInvocationInstruction;


public class PrintUniqueUntracedMethods implements UntracedCallVisitor {

    private static class MethodSignature {
        String internalClassName, methodName, methodDesc;

        public MethodSignature(String internalClassName, String methodName,
                String methodDesc) {
            assert (internalClassName != null && methodName != null && methodDesc != null);
            this.internalClassName = internalClassName;
            this.methodName = methodName;
            this.methodDesc = methodDesc;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + this.internalClassName.hashCode();
            result = prime * result + this.methodDesc.hashCode();
            result = prime * result + this.methodName.hashCode();
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            MethodSignature other = (MethodSignature) obj;
            if (!this.internalClassName.equals(other.internalClassName))
                return false;
            if (!this.methodDesc.equals(other.methodDesc))
                return false;
            if (!this.methodName.equals(other.methodName))
                return false;
            return true;
        }

    }

    private final IntegerMap<Object> reportedCalls = new IntegerMap<Object>();
    private final HashSet<MethodSignature> reportedMethods = new HashSet<MethodSignature>();

    public void visitUntracedMethodCall(InstructionInstance instrInstance)
            throws InterruptedException {
        MethodInvocationInstruction mtdInv = (MethodInvocationInstruction)instrInstance.getInstruction();
        if (this.reportedCalls.containsKey(mtdInv.getIndex()))
            return;
        this.reportedCalls.put(mtdInv.getIndex(), null);

        MethodSignature sig = new MethodSignature(mtdInv.getInvokedInternalClassName(),
            mtdInv.getInvokedMethodName(), mtdInv.getInvokedMethodDesc());
        if (this.reportedMethods.contains(sig))
            return;
        this.reportedMethods.add(sig);
        System.out.format("\rWarning: Untraced method %s.%s %s\n",
            Type.getObjectType(sig.internalClassName).getClassName(),
            sig.methodName,
            sig.methodDesc);
    }

}