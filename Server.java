import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;

public class Server {

    public static void main(String[] args) {
        System.err.println("Start");
        RpcReader reader = new RpcReader(System.in);
        reader.read();
    }

    private static class RpcReader {
        private final InputStream inputStream;

        public RpcReader(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        private void read() {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(this.inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.matches("^Content-Length: [0-9]+$")) {
                        String sz = line.substring("Content-Length: ".length());
                        reader.read(); // \r
                        reader.read(); // \n
                        System.err.println("Len: " + sz);
                        CharBuffer buffer = CharBuffer.allocate(Integer.parseInt(sz));
                        int x = reader.read(buffer);
                        buffer.flip();
                        String theString = buffer.toString();
                        System.err.println("buffer: " + theString);
                        System.err.println("x: " + x);
                    }
                }
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }
}
