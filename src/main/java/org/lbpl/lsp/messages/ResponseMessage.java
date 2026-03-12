package org.lbpl.lsp.messages;

import io.github.mharbol.json.JSONValue;

/**
 * ResponseMessage
 */
public class ResponseMessage implements JSONWritable {

    public final int id;

    public ResponseMessage(int id) {
        this.id = id;
    }

	@Override
	public JSONValue toJson() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'toJson'");
	}
}
