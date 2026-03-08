package org.lbpl.lsp;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.lbpl.lsp.rpc.RpcReader;

import io.github.mharbol.json.JSONObject;
import io.github.mharbol.json.parser.Parser;
import io.github.mharbol.json.exception.JSONException;

public class ServerMain {

    public static final String LOGGER_NAME = "educationalsp";

    private static Logger logger = Logger.getLogger(LOGGER_NAME);
    private static FileHandler fileHandler;

    public static void main(String[] args) {
        initLogging();
        logger.info("Start");
        try (RpcReader reader = new RpcReader(System.in)) {
            String jsonString;
            while ((jsonString = reader.readRpcJson()) != null) {
                handleStringMessage(jsonString);
            }
        } catch (Exception e) {
            // Something....
        }
    }

    private static void handleStringMessage(String msg) throws JSONException {
        JSONObject jso = Parser.parseJSON(msg);
        final String method = jso.get("method").serialize();
        switch (method) {
            case "\"initialize\"":
                logger.info("handling initialize");
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
