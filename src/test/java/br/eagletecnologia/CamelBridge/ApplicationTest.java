package br.eagletecnologia.CamelBridge;

import java.util.concurrent.TimeUnit;

import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.test.main.junit5.CamelMainTestSupport;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A simple unit test showing how to test the application {@link Main}.
 */
class ApplicationTest extends CamelMainTestSupport {

    @Override
    protected Class<?> getMainClass() {
        // The main class of the application to test
        return Main.class;
    }

    @Test
    void should_start_route_successfully() {
        // Verifica se a rota foi iniciada corretamente
        // A rota de sincronização de usuários executa via cron a cada minuto
        // e depende de banco de dados e API externa, então apenas verificamos
        // se a rota está ativa
        assertTrue(
                context.getRouteController().getRouteStatus("route1").isStarted(),
                "A rota deve estar iniciada"
        );
    }
}
