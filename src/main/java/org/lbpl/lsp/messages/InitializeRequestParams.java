package org.lbpl.lsp.messages;

import io.github.mharbol.json.JSONObject;
import io.github.mharbol.json.JSONValue;

/**
 * InitializeRequestParams
 */
public class InitializeRequestParams implements JSONWritable {

    public final ClientInfo clientInfo;

    public InitializeRequestParams(JSONObject jsonObject) {
        clientInfo = new ClientInfo((JSONObject) jsonObject.get("clientInfo"));
    }

    @Override
    public JSONValue toJson() {
        return clientInfo.toJson();
    }
}
