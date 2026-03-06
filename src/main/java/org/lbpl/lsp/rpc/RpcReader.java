package org.lbpl.lsp.rpc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;

public class RpcReader implements AutoCloseable {

    private final InputStream inputStream;
    private final BufferedReader bufferedReader;

    public RpcReader(InputStream inputStream) {
        this.inputStream = inputStream;
        this.bufferedReader = new BufferedReader(new InputStreamReader(this.inputStream, StandardCharsets.UTF_8));
    }

    public String readRpcJson() {
        try {
            String line;
            if ((line = bufferedReader.readLine()) != null) {
                if (line.matches("^Content-Length: [0-9]+$")) {
                    String sz = line.substring("Content-Length: ".length());
                    bufferedReader.read(); // \r
                    bufferedReader.read(); // \n
                    CharBuffer buffer = CharBuffer.allocate(Integer.parseInt(sz));
                    /* int x = */ bufferedReader.read(buffer);
                    buffer.flip();
                    String theString = buffer.toString();
                    return theString;
                }
            }
        } catch (IOException e) {
            System.err.println(e);
        }
        return null;
    }

    @Override
    public void close() throws Exception {
        this.bufferedReader.close();
    }
}
