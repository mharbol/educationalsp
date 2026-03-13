package org.lbpl.lsp.rpc;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * RpcWriter
 */
public class RpcWriter implements AutoCloseable {

    private final BufferedWriter writer;
    private final OutputStream outputStream;

    public RpcWriter(OutputStream outputStream) {
        this.outputStream = outputStream;
        this.writer = new BufferedWriter(new OutputStreamWriter(this.outputStream));
    }

    public void writeString(String message) throws IOException {
        outputStream.write(message.getBytes());
    }

    @Override
    public void close() throws Exception {
        writer.close();
    }
}
