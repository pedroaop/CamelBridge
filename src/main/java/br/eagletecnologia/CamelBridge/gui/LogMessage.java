package br.eagletecnologia.CamelBridge.gui;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Modelo que representa uma mensagem de log para exibição na GUI.
 */
public class LogMessage {
    private final LocalDateTime timestamp;
    private final String level;
    private final String logger;
    private final String message;
    
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    
    public LogMessage(String level, String logger, String message) {
        this.timestamp = LocalDateTime.now();
        this.level = level;
        this.logger = logger;
        this.message = message;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public String getLevel() {
        return level;
    }
    
    public String getLogger() {
        return logger;
    }
    
    public String getMessage() {
        return message;
    }
    
    /**
     * Retorna a mensagem formatada para exibição.
     */
    public String getFormattedMessage() {
        return String.format("[%s] %-5s %s", 
            timestamp.format(TIME_FORMATTER), 
            level, 
            message);
    }
    
    /**
     * Retorna a cor HTML baseada no nível do log.
     */
    public String getColor() {
        switch (level.toUpperCase()) {
            case "ERROR":
                return "#FF6B6B"; // Vermelho
            case "WARN":
            case "WARNING":
                return "#FFD93D"; // Amarelo
            case "INFO":
                return "#6BCF7F"; // Verde
            case "DEBUG":
                return "#95A5A6"; // Cinza
            default:
                return "#FFFFFF"; // Branco
        }
    }
}

