package org.lbpl.lsp.rpc;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.logging.Logger;

import org.lbpl.lsp.LanguageServerMain;
import org.lbpl.lsp.messages.JSONWritable;

/**
 * RpcWriter
 */
public class RpcWriter implements AutoCloseable {

    private static Logger logger = Logger.getLogger(LanguageServerMain.LOGGER_NAME);

    private final BufferedWriter writer;
    private final OutputStream outputStream;

    public RpcWriter(OutputStream outputStream) {
        this.outputStream = outputStream;
        this.writer = new BufferedWriter(new OutputStreamWriter(this.outputStream));
    }

    public void writeJsonMessage(JSONWritable message) throws IOException {
        final String jsonMessage = message.toJson().serialize();
        final int jsonMessageLen = jsonMessage.length();
        this.writer.write(RpcReader.CONTENT_LENGTH_STR);
        this.writer.write(String.valueOf(jsonMessageLen));
        this.writer.write("\r\n\r\n");
        this.writer.write(jsonMessage);
        logger.fine("SEND: " + jsonMessage);
        this.writer.flush();
    }

    @Override
    public void close() throws Exception {
        writer.close();
    }
}
