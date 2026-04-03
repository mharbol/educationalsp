package org.lbpl.lsp;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.lbpl.lsp.messages.InitializeRequestMessage;
import org.lbpl.lsp.messages.InitializeResponseMessage;
import org.lbpl.lsp.rpc.RpcReader;
import org.lbpl.lsp.rpc.RpcWriter;

import io.github.mharbol.json.JSONObject;
import io.github.mharbol.json.parser.Parser;
import io.github.mharbol.json.exception.JSONException;

public class LanguageServerMain {

    public static final String LOGGER_NAME = "educationalsp";

    private static Logger logger = Logger.getLogger(LOGGER_NAME);
    private static FileHandler fileHandler;

    public static void main(String[] args) {
        initLogging();
        logger.info("Start");
        try (RpcReader reader = new RpcReader(System.in); RpcWriter writer = new RpcWriter(System.out)) {
            String jsonString;
            while ((jsonString = reader.readRpcJson()) != null) {
                handleStringMessage(jsonString, writer);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Bad stuff", e);
        }
    }

    private static void handleStringMessage(String msg, RpcWriter writer) throws JSONException, IOException {
        JSONObject jso = Parser.parseJSON(msg);
        final String method = jso.get("method").toString();
        switch (method) {
            case "initialize":
                logger.info("handling initialize: ");
                InitializeRequestMessage requestMessage = new InitializeRequestMessage(jso);
                logger.info(requestMessage.toString());
                InitializeResponseMessage responseMessage = new InitializeResponseMessage(requestMessage.id);
                writer.writeJsonMessage(responseMessage);
                break;
        }
    }

    private static void initLogging() {
        try {
            fileHandler = new FileHandler("log.txt", false);
            logger.setLevel(Level.ALL);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            logger.addHandler(fileHandler);
            logger.setUseParentHandlers(false);
            logger.info("Logging initialized");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Runtime error", e);
        }
    }
}
