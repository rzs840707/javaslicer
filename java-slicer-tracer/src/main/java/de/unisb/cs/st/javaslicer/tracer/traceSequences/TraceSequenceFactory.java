package de.unisb.cs.st.javaslicer.tracer.traceSequences;

import java.io.IOException;
import java.io.OutputStream;

import de.unisb.cs.st.javaslicer.tracer.ThreadTracer;
import de.unisb.cs.st.javaslicer.tracer.Tracer;
import de.unisb.cs.st.javaslicer.tracer.traceSequences.TraceSequence.Type;


public interface TraceSequenceFactory {

    public interface PerThread {

        TraceSequence createTraceSequence(Type type, Tracer tracer) throws IOException;

        void finish() throws IOException;

        void writeOut(OutputStream out) throws IOException;

    }

    PerThread forThreadTracer(ThreadTracer tt);

    boolean shouldAutoFlushFile();

}
