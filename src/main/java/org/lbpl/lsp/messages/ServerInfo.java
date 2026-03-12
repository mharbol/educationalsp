package org.lbpl.lsp.messages;

import io.github.mharbol.json.JSONObject;
import io.github.mharbol.json.JSONValue;

/**
 * ServerInfo
 */
public class ServerInfo implements JSONWritable {

    public final String name;
    public final String version;

    public ServerInfo(String name, String version) {
        this.name = name;
        this.version = version;
    }

    @Override
    public JSONValue toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("version", version);
        return jsonObject;
    }
}
