package rpc_test

import (
	"educationalsp/rpc"
	"testing"
)

type EncodingExample struct {
	Testing bool `json:"testing"`
}

func TestEncoding(t *testing.T) {
	expected := "Content-Length: 16\r\n\r\n{\"testing\":true}"
	actual := rpc.EncodeMessage(EncodingExample{Testing: true})
	if expected != actual {
		t.Fatalf("Expected: %s, Actual: %s", expected, actual)
	}
}

func TestDecode(t *testing.T) {
	incomingMessage := "Content-Length: 15\r\n\r\n{\"method\":\"hi\"}"
	method, content, err := rpc.DecodeMessage([]byte(incomingMessage))
	contentLength := len(content)
	if nil != err {
		t.Fatal(err)
	}
	if 15 != contentLength {
		t.Fatalf("Expected: 15, Actual: %d", contentLength)
	}
	if method != "hi" {
		t.Fatalf("Expected: 'hi', Actual: %s", method)
	}
}
