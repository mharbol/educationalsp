import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;

public class Server {

    public static void main(String[] args) {
        System.err.println("Start");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8))) {
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
                    System.err.println("buff: " + buffer);
                    System.err.println("x: " + x);
                }
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
