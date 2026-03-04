package main

import (
	"bufio"
	"educationalsp/analysis"
	"educationalsp/lsp"
	"educationalsp/rpc"
	"encoding/json"
	"io"
	"log"
	"os"
)

func main() {
	logger := getLogger("/Users/marcusharbol/development/educationalsp/log.txt")
	logger.Println("Hey, I started")

	scanner := bufio.NewScanner(os.Stdin)
	scanner.Split(rpc.Split)

	state := analysis.NewState()
	writer := os.Stdout

	for scanner.Scan() {
		msg := scanner.Bytes()
		method, contents, err := rpc.DecodeMessage(msg)
		if err != nil {
			logger.Printf("Got error: %s", err)
			continue
		}
		handleMessage(logger, writer, &state, method, contents)
	}
}

func handleMessage(logger *log.Logger, writer io.Writer, state *analysis.State, method string, contents []byte) {
	logger.Printf("RECV msg with method: %s", method)
	switch method {
	case "initialize":
		var request lsp.InitializeRequest
		if err := json.Unmarshal(contents, &request); err != nil {
			logger.Printf("initialize error: %s", err)
		}
		logger.Printf("Connected to: %s %s",
			request.Params.ClientInfo.Name,
			request.Params.ClientInfo.Version)
		msg := lsp.NewInitializeResponse(request.ID)
		writeResponse(writer, &msg)
		logger.Printf("Sent reply")
	case "textDocument/didOpen":
		var notification lsp.DidOpenTextDocumentNotification
		if err := json.Unmarshal(contents, &notification); err != nil {
			logger.Printf("textDocument/didOpen error: %s", err)
		}
		logger.Printf("Opened: %s", notification.Params.TextDocument.URI)
		state.OpenDocument(notification.Params.TextDocument.URI, notification.Params.TextDocument.Text)
	case "textDocument/didChange":
		var notification lsp.TextDocumentDidChangeNotification
		if err := json.Unmarshal(contents, &notification); err != nil {
			logger.Printf("textDocument/didChange error: %s", err)
		}
		logger.Printf("Changed: %s", notification.Params.TextDocument.URI)
		for _, change := range notification.Params.ContentChanges {
			state.UpdateDocument(notification.Params.TextDocument.URI, change.Text)
		}
	case "textDocument/hover":
		var request lsp.HoverRequest
		if err := json.Unmarshal(contents, &request); err != nil {
			logger.Printf("textDocument/hover error: %s", err)
		}
		response := lsp.HoverResponse{
			Response: lsp.Response{
				RPC: "2.0",
				ID:  &request.ID,
			},
			Result: lsp.HoverResult{
				Contents: "Hello, from LSP",
			},
		}
		writeResponse(writer, &response)
	}
}

func writeResponse(writer io.Writer, msg any) {
	reply := rpc.EncodeMessage(msg)
	writer.Write([]byte(reply))
}

func getLogger(filename string) *log.Logger {
	logfile, err := os.OpenFile(filename, os.O_CREATE|os.O_TRUNC|os.O_RDWR, 0666)
	if err != nil {
		panic(err)
	}
	return log.New(logfile, "[educationalsp]", log.Ldate|log.Ltime|log.Lshortfile)
}
