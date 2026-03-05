package rpc

import (
	"bytes"
	"educationalsp/lsp"
	"encoding/json"
	"errors"
	"fmt"
	"strconv"
)

const contentLength string = "Content-Length: "
const lenContentLength int = len(contentLength)

func EncodeMessage(msg any) string {
	content, err := json.Marshal(msg)
	if nil != err {
		panic(err) // this should never fail so let's panic here if it does...
	}
	return fmt.Sprintf("%s%d\r\n\r\n%s", contentLength, len(content), content)
}

func DecodeMessage(msg []byte) (string, []byte, error) {
	// Separate header and content
	header, content, found := bytes.Cut(msg, []byte{'\r', '\n', '\r', '\n'})
	if !found {
		return "", nil, errors.New("Did not find separator")
	}
	// Content-Length: <number>
	contentLengthBytes := header[lenContentLength:]
	contentLength, err := strconv.Atoi(string(contentLengthBytes))
	if nil != err {
		return "", nil, err
	}
	var baseMessage lsp.BaseMessage
	if err := json.Unmarshal(content[:contentLength], &baseMessage); err != nil {
		return "", nil, err
	}
	return baseMessage.Method, content[:contentLength], nil
}

// The big guy...
// Read in based on content length
func Split(data []byte, _ bool) (advance int, token []byte, err error) {
	// Stolen from Decode start
	// Separate header and content
	header, content, found := bytes.Cut(data, []byte{'\r', '\n', '\r', '\n'})
	if !found {
		return 0, nil, nil
	}
	// Content-Length: <number>
	contentLengthBytes := header[lenContentLength:]
	contentLength, err := strconv.Atoi(string(contentLengthBytes))
	if nil != err {
		return 0, nil, err
	}
	// Stolen from Decode end

	// keep waiting of the full messasge is not here
	if len(content) < contentLength {
		return 0, nil, nil
	}
	totalLength := len(header) + 4 + contentLength
	return totalLength, data[:totalLength], nil
}
