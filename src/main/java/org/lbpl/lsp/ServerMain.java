package org.lbpl.lsp;

import org.lbpl.lsp.rpc.RpcReader;

import io.github.mharbol.json.JSONObject;
import io.github.mharbol.json.parser.Parser;
import io.github.mharbol.json.exception.JSONException;

public class ServerMain {

    public static void main(String[] args) {
        System.err.println("Start");
        try (RpcReader reader = new RpcReader(System.in)) {
            String jsonString;
            while ((jsonString = reader.readRpcJson()) != null) {
                handleStringMessage(jsonString);
            }
        } catch (Exception e) {
            // Something....
        }
    }

    private static void handleStringMessage(String msg) throws JSONException {
        JSONObject jso = Parser.parseJSON(msg);
        System.err.println(jso.get("method").serialize());
    }
}
