package br.eagletecnologia.CamelBridge.service;

import org.apache.camel.main.Main;

import java.io.InputStream;
import java.util.Properties;

/**
 * Serviço para gerenciar a aplicação e acessar configurações.
 */
public class ApplicationService {
    
    private static final String PROPERTIES_FILE = "/application.properties";
    private Properties properties;
    private Main camelMain;
    private String lastSearch;
    
    public ApplicationService() {
        loadProperties();
    }
    
    /**
     * Carrega as propriedades do arquivo application.properties.
     */
    private void loadProperties() {
        properties = new Properties();
        try (InputStream is = getClass().getResourceAsStream(PROPERTIES_FILE)) {
            if (is != null) {
                properties.load(is);
                lastSearch = properties.getProperty("lastSearch", "N/A");
            } else {
                lastSearch = "N/A";
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar propriedades: " + e.getMessage());
            lastSearch = "N/A";
        }
    }
    
    /**
     * Retorna a data da última sincronização (lastSearch).
     */
    public String getLastSearch() {
        return lastSearch;
    }
    
    /**
     * Atualiza a data da última sincronização.
     */
    public void setLastSearch(String lastSearch) {
        this.lastSearch = lastSearch;
    }
    
    /**
     * Define a instância do Camel Main.
     */
    public void setCamelMain(Main camelMain) {
        this.camelMain = camelMain;
    }
    
    /**
     * Retorna o status atual da aplicação Camel.
     */
    public String getStatus() {
        if (camelMain == null) {
            return "Não iniciado";
        }
        
        try {
            if (camelMain.getCamelContext() != null) {
                return camelMain.getCamelContext().getStatus().name();
            }
        } catch (Exception e) {
            // Ignorar
        }
        
        return "Iniciando...";
    }
    
    /**
     * Para a aplicação Camel de forma segura.
     */
    public void stop() {
        if (camelMain != null) {
            try {
                camelMain.stop();
            } catch (Exception e) {
                System.err.println("Erro ao parar Camel: " + e.getMessage());
            }
        }
    }
    
    /**
     * Recarrega as propriedades do arquivo.
     */
    public void reloadProperties() {
        loadProperties();
    }
}

