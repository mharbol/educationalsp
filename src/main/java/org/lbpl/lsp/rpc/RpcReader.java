package org.lbpl.lsp.rpc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

import org.lbpl.lsp.LanguageServerMain;

public class RpcReader implements AutoCloseable {

    private final InputStream inputStream;
    private final BufferedReader bufferedReader;

    /**
     * LSP header consistis of just "Content-Length: <number>"
     */
    private static final String CONTENT_LENGTH_STR = "Content-Length: ";

    /**
     * Length of CONTENT_LENGTH_STR for substring-ing
     */
    private static final int CONTENT_LENGTH_STR_LEN = CONTENT_LENGTH_STR.length();

    /**
     * Regex to verify a line reading "Content-Length: <number>"
     */
    private static final String CONTENT_LENGTH_REGEX = "^" + CONTENT_LENGTH_STR + "[0-9]+$";

    private static Logger logger = Logger.getLogger(LanguageServerMain.LOGGER_NAME);

    /**
     * Reader specifically made to read LSP messages from a client.
     * 
     * @param inputStream
     */
    public RpcReader(InputStream inputStream) {
        this.inputStream = inputStream;
        this.bufferedReader = new BufferedReader(new InputStreamReader(this.inputStream, StandardCharsets.UTF_8));
    }

    /**
     * Read the next JSON {@link String} from the client
     * 
     * @return the next JSON {@link String} from the client
     */
    public String readRpcJson() {
        try {
            String line;
            if ((line = bufferedReader.readLine()) != null) {
                if (line.matches(CONTENT_LENGTH_REGEX)) {
                    String sz = line.substring(CONTENT_LENGTH_STR_LEN);
                    bufferedReader.read(); // \r
                    bufferedReader.read(); // \n
                    CharBuffer buffer = CharBuffer.allocate(Integer.parseInt(sz));
                    /* int x = */ bufferedReader.read(buffer);
                    buffer.flip();
                    String theString = buffer.toString();
                    logger.fine("RECV: " + theString);
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
