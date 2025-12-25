package br.eagletecnologia.CamelBridge.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Barra de status na parte inferior da janela.
 */
public class StatusBar extends JPanel {
    
    private JLabel lastSyncLabel;
    private JLabel statusLabel;
    private JLabel logCountLabel;
    
    private static final Color BACKGROUND_COLOR = new Color(240, 240, 240);
    private static final Color BORDER_COLOR = new Color(200, 200, 200);
    
    public StatusBar() {
        initializeComponents();
        setupLayout();
    }
    
    private void initializeComponents() {
        lastSyncLabel = new JLabel("Última sincronização: --");
        statusLabel = new JLabel("Status: Iniciando...");
        logCountLabel = new JLabel("Logs: 0");
        
        // Estilizar labels
        Font labelFont = new Font(Font.SANS_SERIF, Font.PLAIN, 11);
        lastSyncLabel.setFont(labelFont);
        statusLabel.setFont(labelFont);
        logCountLabel.setFont(labelFont);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        setBackground(BACKGROUND_COLOR);
        
        // Painel esquerdo com informações principais
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftPanel.setBackground(BACKGROUND_COLOR);
        leftPanel.add(lastSyncLabel);
        leftPanel.add(Box.createHorizontalStrut(20));
        leftPanel.add(statusLabel);
        
        // Painel direito com informações secundárias
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setBackground(BACKGROUND_COLOR);
        rightPanel.add(logCountLabel);
        
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
    }
    
    /**
     * Atualiza a data da última sincronização.
     */
    public void updateLastSync(String lastSearch) {
        SwingUtilities.invokeLater(() -> {
            lastSyncLabel.setText("Última sincronização: " + lastSearch);
        });
    }
    
    /**
     * Atualiza o status da aplicação.
     */
    public void updateStatus(String status) {
        SwingUtilities.invokeLater(() -> {
            statusLabel.setText("Status: " + status);
        });
    }
    
    /**
     * Atualiza o contador de logs.
     */
    public void updateLogCount(int count) {
        SwingUtilities.invokeLater(() -> {
            logCountLabel.setText("Logs: " + count);
        });
    }
}

