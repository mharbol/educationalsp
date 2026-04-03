package org.lbpl.lsp.messages;

import io.github.mharbol.json.JSONValue;

/**
 * JSONWritable
 */
public interface JSONWritable {

    JSONValue toJson();
}
