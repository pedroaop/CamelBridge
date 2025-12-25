package br.eagletecnologia.CamelBridge.gui;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

/**
 * Painel para visualização de logs em estilo terminal.
 */
public class LogViewerPanel extends JPanel {
    
    private static final int MAX_LINES = 1000;
    private static final Font MONOSPACE_FONT = new Font(Font.MONOSPACED, Font.PLAIN, 12);
    private static final Color BACKGROUND_COLOR = new Color(30, 30, 30);
    
    private JTextPane textPane;
    private JScrollPane scrollPane;
    private StyledDocument document;
    private StyleContext styleContext;
    private int lineCount = 0;
    private boolean autoScroll = true;
    
    public LogViewerPanel() {
        initializeComponents();
        setupLayout();
    }
    
    private void initializeComponents() {
        styleContext = new StyleContext();
        document = new DefaultStyledDocument(styleContext);
        
        textPane = new JTextPane(document);
        textPane.setEditable(false);
        textPane.setFont(MONOSPACE_FONT);
        textPane.setBackground(BACKGROUND_COLOR);
        textPane.setForeground(Color.WHITE);
        
        // Configurar scroll
        scrollPane = new JScrollPane(textPane);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        // Detectar quando usuário rola manualmente
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                if (!e.getValueIsAdjusting()) {
                    // Verifica se está no final (com margem de 5 pixels)
                    int max = verticalScrollBar.getMaximum();
                    int extent = verticalScrollBar.getVisibleAmount();
                    int value = verticalScrollBar.getValue();
                    autoScroll = (value + extent >= max - 5);
                }
            }
        });
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
    }
    
    /**
     * Adiciona uma mensagem de log ao painel.
     * Thread-safe: deve ser chamado via SwingUtilities.invokeLater()
     */
    public void appendLog(LogMessage logMessage) {
        if (SwingUtilities.isEventDispatchThread()) {
            doAppendLog(logMessage);
        } else {
            SwingUtilities.invokeLater(() -> doAppendLog(logMessage));
        }
    }
    
    private void doAppendLog(LogMessage logMessage) {
        try {
            // Criar estilo com cor baseada no nível do log
            Style style = styleContext.addStyle("logStyle", null);
            StyleConstants.setForeground(style, Color.decode(logMessage.getColor()));
            
            // Adicionar mensagem
            String formattedMessage = logMessage.getFormattedMessage() + "\n";
            document.insertString(document.getLength(), formattedMessage, style);
            
            lineCount++;
            
            // Remover linhas antigas se exceder o limite
            if (lineCount > MAX_LINES) {
                int removeLength = document.getText(0, Math.min(500, document.getLength())).length();
                document.remove(0, removeLength);
                lineCount -= MAX_LINES / 2; // Aproximação
            }
            
            // Scroll automático se estiver no final
            if (autoScroll) {
                textPane.setCaretPosition(document.getLength());
            }
            
        } catch (BadLocationException e) {
            // Ignorar erros de inserção
            e.printStackTrace();
        }
    }
    
    /**
     * Limpa todos os logs do painel.
     */
    public void clearLogs() {
        SwingUtilities.invokeLater(() -> {
            try {
                document.remove(0, document.getLength());
                lineCount = 0;
                textPane.setCaretPosition(0);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        });
    }
    
    /**
     * Retorna o número de linhas de log.
     */
    public int getLineCount() {
        return lineCount;
    }
}

