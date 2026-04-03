package org.lbpl.lsp.messages;

import io.github.mharbol.json.JSONObject;
import io.github.mharbol.json.JSONValue;

/**
 * InitializeResult
 */
public class InitializeResult implements JSONWritable {

    public final ServerInfo serverInfo;
    public final ServerCapabilities capabilities;

    public InitializeResult() {
        this.serverInfo = new ServerInfo();
        this.capabilities = new ServerCapabilities();
    }

    @Override
    public JSONValue toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("serverInfo", this.serverInfo.toJson());
        jsonObject.put("capabilities", this.capabilities.toJson());
        return jsonObject;
    }
}
