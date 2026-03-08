package org.lbpl.lsp.messages;

import io.github.mharbol.json.JSONObject;

/**
 * BaseMessage
 */
public class BaseMessage {

    public static final String JSON_RPC = "2.0";

    public final String jsonRpc;
    public final String method;

    public BaseMessage(String jsonRpc, String method) {
        this.jsonRpc = jsonRpc;
        this.method = method;
    }

    public BaseMessage(String method) {
        this(JSON_RPC, method);
    }

    public BaseMessage(JSONObject jsonObject) {
        this(jsonObject.get("jsonrpc").toString(), jsonObject.get("method").toString());
    }
}
