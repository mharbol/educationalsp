-- To enable, add this file to lsp/ directory in nvim config
-- Build lsp:
-- ~$ go build -o bin/ main.go
--
-- Add this line to init.lua file:
-- `vim.lsp.enable("educationalsp")`

return {
    cmd = { "/path/to/educationalsp/bin/main" }, -- update path
    filetypes = { "markdown" },
    single_file_support = true,
}
