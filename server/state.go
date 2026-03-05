package server

import (
	"educationalsp/lsp"
	"fmt"
	"strings"
)

type State struct {
	Documents map[string]string
}

func NewState() State {
	return State{
		Documents: map[string]string{},
	}
}

func getDaignosticsForFile(text string) []lsp.Diagnostic {
	dianostics := []lsp.Diagnostic{}
	for row, line := range strings.Split(text, "\n") {
		if strings.Contains(line, "VS Code") {
			idx := strings.Index(line, "VS Code")
			dianostics = append(dianostics, lsp.Diagnostic{
				Range:    LineRange(row, idx, idx+len("VS Code")),
				Severity: 1, // 1== Error
				Source:   "Common sense",
				Message:  "Please use good language here",
			})
		}
		if strings.Contains(line, "VS C*de") {
			idx := strings.Index(line, "VS C*de")
			dianostics = append(dianostics, lsp.Diagnostic{
				Range:    LineRange(row, idx, idx+len("VS C*de")),
				Severity: 2, // 2== Warning
				Source:   "Common sense",
				Message:  "A little better",
			})
		}
		if strings.Contains(line, "Neovim") {
			idx := strings.Index(line, "Neovim")
			dianostics = append(dianostics, lsp.Diagnostic{
				Range:    LineRange(row, idx, idx+len("Neovim")),
				Severity: 4, // 4== Hint
				Source:   "Common sense",
				Message:  "Great choice",
			})
		}
	}
	return dianostics
}

func (s *State) OpenDocument(uri, text string) []lsp.Diagnostic {
	// also do diagnostic on open
	s.Documents[uri] = text
	return getDaignosticsForFile(text)
}

func (s *State) UpdateDocument(uri, text string) []lsp.Diagnostic {
	s.Documents[uri] = text
	return getDaignosticsForFile(text)
}

func (s *State) Hover(id int, uri string, position lsp.Position) lsp.HoverResponse {
	// simulate some analysis...
	document := s.Documents[uri]
	return lsp.HoverResponse{
		Response: lsp.Response{
			RPC: lsp.JSONRPC,
			ID:  &id,
		},
		Result: lsp.HoverResult{
			Contents: fmt.Sprintf("File %s, Characters: %d", uri, len(document)),
		},
	}
}

func (s *State) Definition(id int, uri string, position lsp.Position) lsp.DefinitionResponse {
	// here we just go up one...
	return lsp.DefinitionResponse{
		Response: lsp.Response{
			RPC: lsp.JSONRPC,
			ID:  &id,
		},
		Result: lsp.Location{
			URI: uri,
			Range: lsp.Range{
				Start: lsp.Position{
					Line:      position.Line - 1, // TODO, negative results
					Character: 0,
				},
				End: lsp.Position{
					Line:      position.Line - 1,
					Character: 0,
				},
			},
		},
	}
}

func (s *State) TextDocumentCodeAction(id int, uri string) lsp.TextDocumentCodeActionResponse {
	text := s.Documents[uri]
	actions := []lsp.CodeAction{}
	for row, line := range strings.Split(text, "\n") {
		idx := strings.Index(line, "VS Code")
		if idx >= 0 {
			replaceChange := map[string][]lsp.TextEdit{}
			replaceChange[uri] = []lsp.TextEdit{
				{
					Range:   LineRange(row, idx, idx+len("VS Code")),
					NewText: "Neovim",
				},
			}
			actions = append(actions, lsp.CodeAction{
				Title: "Replace \"VS C*de\"with a superior editor",
				Edit:  &lsp.WorkspaceEdit{Changes: replaceChange},
			})
			censorChange := map[string][]lsp.TextEdit{}
			censorChange[uri] = []lsp.TextEdit{
				{
					Range:   LineRange(row, idx, idx+len("VS Code")),
					NewText: "VS C*de",
				},
			}
			actions = append(actions, lsp.CodeAction{
				Title: "Censor to \"VS C*de\"",
				Edit:  &lsp.WorkspaceEdit{Changes: censorChange},
			})
		}
	}
	response := lsp.TextDocumentCodeActionResponse{
		Response: lsp.Response{
			RPC: lsp.JSONRPC,
			ID:  &id,
		},
		Result: actions,
	}
	return response
}

func (s *State) TextDocumentCompletion(id int, uri string) lsp.CompletionResponse {

	// ask tools for completion...
	items := []lsp.CompletionItem{
		{
			// These get added by label
			Label:         "Neovim, btw",
			Detail:        "Cool editor",
			Documentation: "Please work",
		},
	}
	response := lsp.CompletionResponse{
		Response: lsp.Response{
			RPC: lsp.JSONRPC,
			ID:  &id,
		},
		Result: items,
	}
	return response
}

func LineRange(line, start, end int) lsp.Range {
	return lsp.Range{
		Start: lsp.Position{
			Line:      line,
			Character: start,
		},
		End: lsp.Position{
			Line:      line,
			Character: end,
		},
	}
}
