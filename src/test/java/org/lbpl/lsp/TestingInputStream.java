package org.lbpl.lsp;

import java.io.IOException;
import java.io.InputStream;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * TestingInputStream
 */
public class TestingInputStream extends InputStream {

    public final Queue<String> strings = new LinkedList<>();
    public final StringCharacterIterator charIter = new StringCharacterIterator("");

    public void writeString(String string) {
        if (null != string) {
            strings.add(string);
        }
    }

    @Override
    public int read() throws IOException {
        char ch = charIter.next();
        if (ch != CharacterIterator.DONE) {
            return ch;
        } else {
            String currString = strings.poll();
            if (null != currString) {
                if (!currString.isEmpty()) {
                    charIter.setText(currString);
                    return charIter.first();
                } else {
                    return read();
                }
            }
        }
        return -1;
    }
}
