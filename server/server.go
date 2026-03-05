package server

import (
	"bufio"
	"educationalsp/lsp"
	"educationalsp/rpc"
	"encoding/json"
	"io"
	"log"
	"os"
)

type Server struct {
	writer  io.Writer
	Logger  log.Logger
	State   State
	Scanner *bufio.Scanner
}

func NewServer() Server {
	scan := bufio.NewScanner(os.Stdin)
	scan.Split(rpc.Split)
	return Server{
		writer:  os.Stdout,
		Logger:  *getLogger("./log.txt"), // maybe update that...
		State:   NewState(),
		Scanner: scan,
	}
}

func (s *Server) Serve() {
	for s.Scanner.Scan() {
		msg := s.Scanner.Bytes()
		method, contents, err := rpc.DecodeMessage(msg)
		if err != nil {
			s.Logger.Printf("Error decoding message: %s", err)
			continue
		}
		s.HandleMessage(method, contents)
	}
}

func (s *Server) WriteMessage(msg any) {
	s.writer.Write([]byte(rpc.EncodeMessage(msg)))
}

func getLogger(filename string) *log.Logger {
	logfile, err := os.OpenFile(filename, os.O_CREATE|os.O_TRUNC|os.O_RDWR, 0666)
	if err != nil {
		panic(err)
	}
	return log.New(logfile, "[educationalsp]", log.Ldate|log.Ltime|log.Lshortfile)
}

func (s *Server) HandleMessage(method string, contents []byte) {
	s.Logger.Printf("RECV msg with method: %s", method)
	switch method {
	case "initialize":
		var request lsp.InitializeRequest
		if err := json.Unmarshal(contents, &request); err != nil {
			s.Logger.Printf("initialize error: %s", err)
		}
		s.Logger.Printf("Connected to: %s %s",
			request.Params.ClientInfo.Name,
			request.Params.ClientInfo.Version)
		msg := lsp.NewInitializeResponse(request.ID)
		s.WriteMessage(&msg)
		s.Logger.Printf("Sent reply")
	case "textDocument/didChange":
		var notification lsp.TextDocumentDidChangeNotification
		if err := json.Unmarshal(contents, &notification); err != nil {
			s.Logger.Printf("textDocument/didChange error: %s", err)
		}
		s.Logger.Printf("Changed: %s", notification.Params.TextDocument.URI)
		for _, change := range notification.Params.ContentChanges {
			// hook in diagnostics here...
			diagnostics := s.State.UpdateDocument(notification.Params.TextDocument.URI, change.Text)
			s.WriteMessage(lsp.PublishDiagnosticsNotification{
				Notification: lsp.Notification{
					RPC:    lsp.JSONRPC,
					Method: "textDocument/publishDiagnostics",
				},
				Params: lsp.PublishDiagnosticsParams{
					URI:         notification.Params.TextDocument.URI,
					Diagnostics: diagnostics,
				},
			})
		}
	case "textDocument/didOpen":
		var notification lsp.DidOpenTextDocumentNotification
		if err := json.Unmarshal(contents, &notification); err != nil {
			s.Logger.Printf("textDocument/didOpen error: %s", err)
		}
		s.Logger.Printf("Opened: %s", notification.Params.TextDocument.URI)
		// hook in diagnostics here too...
		diagnostics := s.State.OpenDocument(notification.Params.TextDocument.URI, notification.Params.TextDocument.Text)
		s.WriteMessage(lsp.PublishDiagnosticsNotification{
			Notification: lsp.Notification{
				RPC:    lsp.JSONRPC,
				Method: "textDocument/publishDiagnostics",
			},
			Params: lsp.PublishDiagnosticsParams{
				URI:         notification.Params.TextDocument.URI,
				Diagnostics: diagnostics,
			},
		})
	case "textDocument/hover":
		var request lsp.HoverRequest
		if err := json.Unmarshal(contents, &request); err != nil {
			s.Logger.Printf("textDocument/hover error: %s", err)
		}
		response := s.State.Hover(request.ID, request.Params.TextDocument.URI, request.Params.Position)
		s.WriteMessage(&response)
	case "textDocument/definition":
		var request lsp.DefinitionRequest
		if err := json.Unmarshal(contents, &request); err != nil {
			s.Logger.Printf("textDocument/definition error: %s", err)
		}
		response := s.State.Definition(request.ID, request.Params.TextDocument.URI, request.Params.Position)
		s.WriteMessage(&response)
	case "textDocument/codeAction":
		var request lsp.CodeActionRequest
		if err := json.Unmarshal(contents, &request); err != nil {
			s.Logger.Printf("textDocument/codeAction error: %s", err)
		}
		response := s.State.TextDocumentCodeAction(request.ID, request.Params.TextDocument.URI)
		s.WriteMessage(&response)
	case "textDocument/completion":
		var request lsp.CompletionRequest
		if err := json.Unmarshal(contents, &request); err != nil {
			s.Logger.Printf("textDocument/completion error: %s", err)
		}
		response := s.State.TextDocumentCompletion(request.ID, request.Params.TextDocument.URI)
		s.WriteMessage(&response)
	}
}
