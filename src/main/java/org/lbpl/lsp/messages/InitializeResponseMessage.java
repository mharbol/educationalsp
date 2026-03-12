package org.lbpl.lsp.messages;

import io.github.mharbol.json.JSONObject;
import io.github.mharbol.json.JSONValue;

/**
 * InitializeResponseMessage
 */
public class InitializeResponseMessage extends ResponseMessage {

    public final InitializeResult result;

    public InitializeResponseMessage(int id) {
        super(id);
        this.result = new InitializeResult();
    }

    @Override
    public JSONValue toJson() {
        JSONObject jsonObject = (JSONObject) super.toJson();
        jsonObject.put("result", this.result.toJson());
        return jsonObject;
    }
}
