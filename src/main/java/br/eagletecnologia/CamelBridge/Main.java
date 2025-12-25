package br.eagletecnologia.CamelBridge;

import br.eagletecnologia.CamelBridge.gui.CamelBridgeFrame;
import br.eagletecnologia.CamelBridge.service.ApplicationService;

import javax.swing.*;

/**
 * Classe principal que inicia a aplicação Camel com interface gráfica.
 */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) throws Exception {
        // Registrar o appender do Log4j2 ANTES de qualquer coisa
        // Isso garante que os logs sejam capturados desde o início
        br.eagletecnologia.CamelBridge.gui.LogAppender.register();
        
        // Iniciar GUI na Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                // Criar serviço da aplicação
                ApplicationService applicationService = new ApplicationService();
                
                // Criar e exibir janela principal
                CamelBridgeFrame frame = new CamelBridgeFrame(applicationService);
                frame.setVisible(true);
                
                // Pequeno delay para garantir que a GUI esteja totalmente inicializada
                Timer initTimer = new Timer(100, e -> {
                    // Iniciar Camel em thread separada para não bloquear a GUI
                    Thread camelThread = new Thread(() -> {
                        try {
                            org.apache.camel.main.Main camelMain = new org.apache.camel.main.Main(Main.class);
                            applicationService.setCamelMain(camelMain);
                            
                            // Atualizar status na GUI
                            SwingUtilities.invokeLater(() -> {
                                frame.getStatusBar().updateStatus("Iniciando...");
                            });
                            
                            // Executar Camel (bloqueia até ser parado)
                            camelMain.run(args);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            SwingUtilities.invokeLater(() -> {
                                frame.getStatusBar().updateStatus("Erro: " + ex.getMessage());
                                JOptionPane.showMessageDialog(
                                    frame,
                                    "Erro ao iniciar Camel: " + ex.getMessage(),
                                    "Erro",
                                    JOptionPane.ERROR_MESSAGE
                                );
                            });
                        }
                    });
                    
                    camelThread.setDaemon(true);
                    camelThread.setName("Camel-Main-Thread");
                    camelThread.start();
                    
                    ((Timer) e.getSource()).stop();
                });
                initTimer.setRepeats(false);
                initTimer.start();
                
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(
                    null,
                    "Erro ao iniciar aplicação: " + e.getMessage(),
                    "Erro Fatal",
                    JOptionPane.ERROR_MESSAGE
                );
                System.exit(1);
            }
        });
    }

}

