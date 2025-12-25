package br.eagletecnologia.CamelBridge;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.apache.camel.BindToRegistry;
import org.apache.camel.Configuration;
import org.apache.camel.PropertyInject;

/**
 * Classe de configuração da aplicação Camel.
 * 
 * Esta classe é responsável por configurar e registrar beans de infraestrutura
 * necessários para as rotas. A anotação @Configuration permite que o Camel detecte
 * automaticamente esta classe e processe suas configurações.
 * 
 * As regras de negócio e lógica das rotas estão definidas nos arquivos YAML
 * na pasta routes/, mantendo 100% das regras de negócio nas rotas.
 */
@Configuration
public class AppConfiguration {

    /**
     * Configura e registra o DataSource para conexão com o banco de dados.
     * 
     * Utiliza HikariCP como pool de conexões para gerenciamento eficiente de conexões JDBC.
     * Todas as propriedades são injetadas do arquivo application.properties através
     * da anotação @PropertyInject.
     * 
     * O DataSource será registrado com o nome "dataSource" e pode ser referenciado
     * nas rotas através de #dataSource (ex: sql:SELECT * FROM tabela?dataSource=#dataSource)
     * 
     * @param url URL de conexão JDBC (ex: jdbc:postgresql://localhost:5432/banco)
     * @param username Usuário do banco de dados
     * @param password Senha do banco de dados
     * @param driverClassName Nome da classe do driver JDBC
     * @param minimumIdle Tamanho mínimo do pool de conexões
     * @param maximumPoolSize Tamanho máximo do pool de conexões
     * @param connectionTimeout Tempo máximo de espera para obter uma conexão (ms)
     * @param idleTimeout Tempo máximo que uma conexão pode ficar idle (ms)
     * @param maxLifetime Tempo máximo de vida de uma conexão no pool (ms)
     * @param connectionTestQuery Query SQL para validar conexões
     * @return DataSource configurado com HikariCP
     */
    @BindToRegistry("dataSource")
    public DataSource dataSource(
            @PropertyInject("db.url") String url,
            @PropertyInject("db.username") String username,
            @PropertyInject("db.password") String password,
            @PropertyInject("db.driver") String driverClassName,
            @PropertyInject(value = "db.pool.minimumIdle", defaultValue = "5") int minimumIdle,
            @PropertyInject(value = "db.pool.maximumPoolSize", defaultValue = "20") int maximumPoolSize,
            @PropertyInject(value = "db.pool.connectionTimeout", defaultValue = "30000") long connectionTimeout,
            @PropertyInject(value = "db.pool.idleTimeout", defaultValue = "600000") long idleTimeout,
            @PropertyInject(value = "db.pool.maxLifetime", defaultValue = "1800000") long maxLifetime,
            @PropertyInject(value = "db.pool.connectionTestQuery", defaultValue = "SELECT 1") String connectionTestQuery) {
        
        // Configura o HikariCP com as propriedades fornecidas
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName(driverClassName);
        config.setMinimumIdle(minimumIdle);
        config.setMaximumPoolSize(maximumPoolSize);
        config.setConnectionTimeout(connectionTimeout);
        config.setIdleTimeout(idleTimeout);
        config.setMaxLifetime(maxLifetime);
        config.setConnectionTestQuery(connectionTestQuery);
        
        // Configurações adicionais recomendadas
        config.setPoolName("CamelBridgeHikariPool");
        config.setLeakDetectionThreshold(60000); // Detecta vazamentos de conexão após 60 segundos
        
        // Cria e retorna o DataSource configurado
        return new HikariDataSource(config);
    }

}

