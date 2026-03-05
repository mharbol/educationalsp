package main

import (
	"educationalsp/server"
)

func main() {
	server := server.NewServer()
	server.Serve()
}
