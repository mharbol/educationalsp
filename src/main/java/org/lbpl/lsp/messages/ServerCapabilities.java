package org.lbpl.lsp.messages;

import io.github.mharbol.json.JSONObject;
import io.github.mharbol.json.JSONValue;

/**
 * ServerCapabilities
 */
public class ServerCapabilities implements JSONWritable {

    @Override
    public JSONValue toJson() {
        JSONObject jsonObject = new JSONObject();
        return jsonObject;
    }
}
