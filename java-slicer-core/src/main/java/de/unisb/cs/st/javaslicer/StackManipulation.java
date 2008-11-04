package de.unisb.cs.st.javaslicer;

import java.util.Collection;
import java.util.Collections;

public class StackManipulation implements VariableUsages {

    private final ExecutionFrame frame;
    private final int read;
    private final int write;
    private final int oldStackSize;
    private Collection<Variable> readVars = null;

    public StackManipulation(final ExecutionFrame frame, final int read, final int write,
            final int oldStackSize) {
        this.frame = frame;
        this.read = read;
        this.write = write;
        this.oldStackSize = oldStackSize;
    }

    @Override
    public Collection<? extends Variable> getDefinedVariables() {
        if (this.write == 0)
            return Collections.emptySet();

        final Collection<Variable> writtenVars;
        if (this.write == 1) {
            writtenVars = Collections.singleton((Variable)this.frame.getStackEntry(this.oldStackSize-1));
        } else {
            writtenVars = new StackEntrySet(this.frame, this.oldStackSize, this.write);
        }
        if (this.read == this.write)
            this.readVars  = writtenVars;
        return writtenVars;
    }

    @Override
    public Collection<? extends Variable> getUsedVariables() {
        if (this.readVars != null)
            return this.readVars;

        if (this.read == 0)
            this.readVars = Collections.emptySet();
        else if (this.read == 1)
            this.readVars = Collections.singleton((Variable)this.frame.getStackEntry(
                    this.oldStackSize + this.read - this.write - 1));
        else
            this.readVars = new StackEntrySet(this.frame, this.oldStackSize + this.read - this.write, this.read);

        return this.readVars;
    }

    @Override
    public Collection<? extends Variable> getUsedVariables(final Variable definedVariable) {
        return getUsedVariables();
    }

    @Override
    public boolean isCatchBlock() {
        return false;
    }

}
