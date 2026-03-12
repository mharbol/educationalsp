package org.lbpl.lsp.messages;

import io.github.mharbol.json.JSONObject;
import io.github.mharbol.json.JSONValue;

/**
 * ResponseMessage
 */
public class ResponseMessage implements JSONWritable {

    public final String jsonRpc;
    public final int id;

    public ResponseMessage(int id) {
        this.id = id;
        this.jsonRpc = BaseMessage.JSON_RPC;
    }

	@Override
	public JSONValue toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", this.id);
        jsonObject.put("jsonrpc", this.jsonRpc);
        return jsonObject;
	}
}
