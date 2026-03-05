package rpc_test

import (
	"educationalsp/rpc"
	"fmt"
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
	const CONTENT string = "{\"method\":\"hi\"}"
	const LEN int = len(CONTENT)
	incomingMessage := fmt.Sprintf("Content-Length: %d\r\n\r\n%s", LEN, CONTENT)
	method, content, err := rpc.DecodeMessage([]byte(incomingMessage))
	contentLength := len(content)
	if nil != err {
		t.Fatal(err)
	}
	if LEN != contentLength {
		t.Fatalf("Expected: %d, Actual: %d", LEN, contentLength)
	}
	if method != "hi" {
		t.Fatalf("Expected: 'hi', Actual: %s", method)
	}
}
