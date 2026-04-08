
package org.lbpl.lsp.rpc;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lbpl.lsp.TestingInputStream;

/**
 * RpcReaderTest
 */
public class RpcReaderTest {

    private RpcReader cut;
    private TestingInputStream inputStream;

    @Before
    public void setup() {
        inputStream = new TestingInputStream();
        cut = new RpcReader(inputStream);
    }

    @Test
    public void testReadJsonRpc() {
        inputStream.writeString("Content-Length: 2\r\n\r\n{}");
        Assert.assertEquals("{}", cut.readRpcJson());
        Assert.assertNull(cut.readRpcJson());
    }

    @Test
    public void testTestingInputStream() throws IOException {
        inputStream.writeString(null);
        inputStream.writeString("abc");
        Assert.assertEquals('a', inputStream.read());
        Assert.assertEquals('b', inputStream.read());
        Assert.assertEquals('c', inputStream.read());
        Assert.assertEquals(-1, inputStream.read());

        inputStream.writeString("d");
        inputStream.writeString("");
        inputStream.writeString("e");
        Assert.assertEquals('d', inputStream.read());
        Assert.assertEquals('e', inputStream.read());
        Assert.assertEquals(-1, inputStream.read());
    }
}
