package br.eagletecnologia.CamelBridge.gui;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.io.Serializable;

/**
 * Appender customizado do Log4j2 que captura logs e os envia para a GUI.
 */
public class LogAppender extends AbstractAppender {
    
    private static LogViewerPanel logViewer;
    private static LogAppender instance;
    
    private LogAppender(String name, Filter filter, Layout<? extends Serializable> layout, 
                       boolean ignoreExceptions) {
        super(name, filter, layout, ignoreExceptions, null);
    }
    
    /**
     * Cria e registra o appender no Log4j2 programaticamente.
     */
    public static void register() {
        if (instance != null) {
            return; // Já registrado
        }
        
        try {
            org.apache.logging.log4j.core.LoggerContext loggerContext = 
                (org.apache.logging.log4j.core.LoggerContext) org.apache.logging.log4j.LogManager.getContext(false);
            
            Configuration config = loggerContext.getConfiguration();
            
            // Criar layout padrão
            Layout<? extends Serializable> layout = PatternLayout.createDefaultLayout(config);
            
            // Criar appender
            instance = new LogAppender("GuiAppender", null, layout, true);
            instance.start();
            
            // Adicionar appender à configuração
            config.addAppender(instance);
            
            // Adicionar appender ao logger raiz
            LoggerConfig rootLoggerConfig = config.getRootLogger();
            rootLoggerConfig.addAppender(instance, null, null);
            
            // Atualizar configuração
            loggerContext.updateLoggers();
            
            System.out.println("LogAppender registrado com sucesso!");
            
        } catch (Exception e) {
            System.err.println("Erro ao registrar LogAppender: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Define o painel de visualização de logs.
     * Deve ser chamado após a criação da GUI.
     */
    public static void setLogViewer(LogViewerPanel viewer) {
        logViewer = viewer;
    }
    
    @Override
    public void append(LogEvent event) {
        if (logViewer != null) {
            String level = event.getLevel().toString();
            String logger = event.getLoggerName();
            String message = event.getMessage().getFormattedMessage();
            
            LogMessage logMessage = new LogMessage(level, logger, message);
            logViewer.appendLog(logMessage);
        }
    }
}

