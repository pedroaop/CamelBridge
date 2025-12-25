package br.eagletecnologia.CamelBridge;

/**
 * Classe principal que inicia a aplicação Camel
 */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) throws Exception {
        // usa a classe Main do Camel
        org.apache.camel.main.Main camelMain = new org.apache.camel.main.Main(Main.class);
        // mantém a aplicação em execução até que a JVM seja encerrada (ctrl + c ou sigterm)
        camelMain.run(args);
    }

}

