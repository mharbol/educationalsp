package org.lbpl.lsp.messages;

import io.github.mharbol.json.JSONObject;
import io.github.mharbol.json.JSONValue;

/**
 * ClientInfo
 */
public class ClientInfo implements JSONWritable {

    public final String name;
    public final String version;

    public ClientInfo(JSONObject jsonObject) {
        this.name = jsonObject.get("name").toString();
        this.version = jsonObject.get("version").toString();
    }

    @Override
    public JSONValue toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("version", version);
        return jsonObject;
    }
}
