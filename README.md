# Educational LSP

## Overview
Following TJ's [tutorial video](https://www.youtube.com/watch?v=YsdlcQoHqPY);
created a simple LSP for markdown files.
This demonstrates how to read requests and notifications and write responses using Go.
You can use this README to test the different features created.

## Quickstart
Steps are contained in the comments of [educationalsp.lua](/educationalsp.lua) file.
Build the server:
```bash
go build -o bin/ main.go
```

Place the educationalsp.lua file in `lsp/` of your nvim configuration.
Add the following to `init.lua`:
```lua
vim.lsp.enable("educationalsp")
```

## Demo
These lines show diagnostics and code actions.
LSP written in Neovim, but will also work in VS Code.

## Future Work
Refactor Go code and redo in Java.
