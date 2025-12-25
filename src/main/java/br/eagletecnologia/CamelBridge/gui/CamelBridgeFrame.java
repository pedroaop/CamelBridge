package br.eagletecnologia.CamelBridge.gui;

import br.eagletecnologia.CamelBridge.service.ApplicationService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Janela principal da aplicação CamelBridge.
 */
public class CamelBridgeFrame extends JFrame {
    
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    
    private LogViewerPanel logViewerPanel;
    private StatusBar statusBar;
    private ApplicationService applicationService;
    private Timer statusUpdateTimer;
    
    public CamelBridgeFrame(ApplicationService applicationService) {
        this.applicationService = applicationService;
        initializeFrame();
        createMenuBar();
        createComponents();
        setupLayout();
        startStatusUpdateTimer();
    }
    
    private void initializeFrame() {
        setTitle("CamelBridge - Sistema de Sincronização");
        setSize(WIDTH, HEIGHT);
        setMinimumSize(new Dimension(600, 400));
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        // Centralizar na tela
        setLocationRelativeTo(null);
        
        // Look and Feel do sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            // Usar look and feel padrão se falhar
        }
        
        // Handler para fechar janela
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitApplication();
            }
        });
    }
    
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // Menu Arquivo
        JMenu fileMenu = new JMenu("Arquivo");
        fileMenu.setMnemonic('A');
        
        JMenuItem clearLogsItem = new JMenuItem("Limpar Logs");
        clearLogsItem.setMnemonic('L');
        clearLogsItem.setAccelerator(KeyStroke.getKeyStroke("ctrl L"));
        clearLogsItem.addActionListener(e -> clearLogs());
        
        JMenuItem exitItem = new JMenuItem("Sair");
        exitItem.setMnemonic('S');
        exitItem.setAccelerator(KeyStroke.getKeyStroke("ctrl Q"));
        exitItem.addActionListener(e -> exitApplication());
        
        fileMenu.add(clearLogsItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        
        // Menu Sobre
        JMenu helpMenu = new JMenu("Sobre");
        helpMenu.setMnemonic('S');
        
        JMenuItem aboutItem = new JMenuItem("Sobre");
        aboutItem.setMnemonic('S');
        aboutItem.addActionListener(e -> showAboutDialog());
        
        helpMenu.add(aboutItem);
        
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }
    
    private void createComponents() {
        logViewerPanel = new LogViewerPanel();
        statusBar = new StatusBar();
        
        // Registrar o logViewerPanel no LogAppender
        // O appender já foi registrado no Main.java antes da GUI ser criada
        LogAppender.setLogViewer(logViewerPanel);
        
        // Atualizar barra de status com valores iniciais
        statusBar.updateLastSync(applicationService.getLastSearch());
        statusBar.updateStatus(applicationService.getStatus());
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        add(logViewerPanel, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);
    }
    
    /**
     * Inicia o timer para atualizar a barra de status periodicamente.
     */
    private void startStatusUpdateTimer() {
        statusUpdateTimer = new Timer(1000, e -> {
            statusBar.updateStatus(applicationService.getStatus());
            statusBar.updateLogCount(logViewerPanel.getLineCount());
        });
        statusUpdateTimer.start();
    }
    
    /**
     * Limpa os logs do painel.
     */
    private void clearLogs() {
        logViewerPanel.clearLogs();
        statusBar.updateLogCount(0);
    }
    
    /**
     * Exibe o diálogo "Sobre".
     */
    private void showAboutDialog() {
        String message = "<html>" +
            "<div style='text-align: center;'>" +
            "<h2>CamelBridge</h2>" +
            "<p><b>Versão:</b> 1.0.0-SNAPSHOT</p>" +
            "<p>Sistema de sincronização de dados desenvolvido com Apache Camel</p>" +
            "<p>que sincroniza informações do banco de dados Firebird para APIs externas.</p>" +
            "<br>" +
            "<p><b>Licença:</b> Apache License 2.0</p>" +
            "</div>" +
            "</html>";
        
        JOptionPane.showMessageDialog(
            this,
            message,
            "Sobre CamelBridge",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    /**
     * Encerra a aplicação de forma segura.
     */
    private void exitApplication() {
        int option = JOptionPane.showConfirmDialog(
            this,
            "Deseja realmente sair?",
            "Confirmar Saída",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (option == JOptionPane.YES_OPTION) {
            if (statusUpdateTimer != null) {
                statusUpdateTimer.stop();
            }
            applicationService.stop();
            dispose();
            System.exit(0);
        }
    }
    
    /**
     * Retorna o painel de visualização de logs.
     */
    public LogViewerPanel getLogViewerPanel() {
        return logViewerPanel;
    }
    
    /**
     * Retorna a barra de status.
     */
    public StatusBar getStatusBar() {
        return statusBar;
    }
}

