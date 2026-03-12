package org.lbpl.lsp.messages;

import io.github.mharbol.json.JSONNumber;
import io.github.mharbol.json.JSONObject;
import io.github.mharbol.json.JSONValue;

/**
 * InitializeMessage
 */
public class InitializeRequestMessage extends BaseMessage {

    public final int id;
    public final InitializeRequestParams params;

    public InitializeRequestMessage(JSONObject jsonObject) {
        super(jsonObject);
        this.id = Integer.parseInt(((JSONNumber) jsonObject.get("id")).serialize());
        this.params = new InitializeRequestParams((JSONObject) jsonObject.get("params"));
    }

    @Override
    public JSONValue toJson() {
        JSONObject jsonObject = (JSONObject) super.toJson();
        jsonObject.put("id", this.id);
        jsonObject.put("params", this.params.toJson());
        return jsonObject;
    }
}
