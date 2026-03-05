package lsp

const JSONRPC string = "2.0"

type BaseMessage struct {
	RPC    string `json:"jsonrpc"`
	Method string `json:"method"`
}

type Request struct {
	RPC    string `json:"jsonrpc"`
	ID     int    `json:"id"` // LSP in other editors might not be string
	Method string `json:"method"`
	// Other stuff comes later
}

type Response struct {
	RPC string `json:"jsonrpc"`
	ID  *int   `json:"id,omitempty"` // LSP in other editors might not be string, in this case it could be null

	// "result" and "error" are optional
}

type Notification struct {
	RPC    string `json:"jsonrpc"`
	Method string `json:"method"`
}
