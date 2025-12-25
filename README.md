# CamelBridge

Sistema de sincronização de dados desenvolvido com Apache Camel que sincroniza informações do banco de dados Firebird para APIs externas. CamelBridge é uma ponte de integração que utiliza padrões Enterprise Integration Patterns (EIP) para garantir uma arquitetura robusta e escalável.

## 📋 Índice

- [Descrição](#descrição)
- [Pré-requisitos](#pré-requisitos)
- [Configuração](#configuração)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Criando Rotas YAML](#criando-rotas-yaml)
- [Padrões e Exemplos](#padrões-e-exemplos)
  - [Ponto de Entrada (Message Endpoint)](#1-ponto-de-entrada-message-endpoint)
  - [Logging](#2-logging)
  - [Modificação de Cabeçalhos (Message Translator)](#3-modificação-de-cabeçalhos-message-translator)
  - [Consulta SQL (Content Enricher)](#4-consulta-sql-content-enricher)
  - [Splitter (Divisão de Mensagens)](#5-splitter-divisão-de-mensagens)
  - [Agregação (Aggregator)](#6-agregação-aggregator)
  - [Serialização JSON (Marshal)](#7-serialização-json-marshal)
  - [Desserialização JSON (Unmarshal)](#8-desserialização-json-unmarshal)
  - [Chamadas HTTP (Messaging Gateway)](#9-chamadas-http-messaging-gateway)
  - [Transformação de Dados (Transform)](#10-transformação-de-dados-transform)
  - [Filtragem (Content Filter)](#11-filtragem-content-filter)
  - [Tratamento de Erros (Error Handler)](#12-tratamento-de-erros-error-handler)
  - [Roteamento Condicional (Content-Based Router)](#13-roteamento-condicional-content-based-router)
  - [Loops e Iterações](#14-loops-e-iterações)
  - [Delay e Throttling](#15-delay-e-throttling)
  - [Multicast (Envio Paralelo)](#16-multicast-envio-paralelo)
- [Propriedades de Configuração](#propriedades-de-configuração)
- [Executando a Aplicação](#executando-a-aplicação)
- [Referências](#referências)

## 📝 Descrição

O CamelBridge permite sincronizar dados entre sistemas heterogêneos de forma declarativa usando arquivos YAML. As rotas são definidas na pasta `routes/` e são carregadas automaticamente pela aplicação.

## 🔧 Pré-requisitos

- Java 17 ou superior
- Maven 3.6 ou superior
- Banco de dados Firebird (ou outro banco de dados compatível com JDBC)

## ⚙️ Configuração

1. Clone o repositório
2. Configure as propriedades do banco de dados em `src/main/resources/application.properties`
3. Compile o projeto: `mvn clean install`
4. Execute: `mvn camel:run`

## 📁 Estrutura do Projeto

```
CamelBridge/
├── routes/                    # Diretório para arquivos YAML de rotas
│   └── usuarios.yaml         # Exemplo de rota de sincronização
├── src/main/java/
│   └── br/eagletecnologia/CamelBridge/
│       ├── Main.java         # Classe principal
│       └── AppConfiguration.java  # Configuração de beans (DataSource)
├── src/main/resources/
│   ├── application.properties    # Propriedades da aplicação
│   └── log4j2.properties        # Configuração de logs
└── pom.xml                       # Dependências Maven
```

## 📖 Enterprise Integration Patterns (EIP) e Camel YAML DSL

### O que são Enterprise Integration Patterns (EIP)?

Enterprise Integration Patterns (EIP) são um conjunto de padrões arquiteturais documentados por Gregor Hohpe e Bobby Woolf que descrevem soluções comuns para problemas de integração entre sistemas. Esses padrões fornecem uma linguagem comum e reutilizável para projetar sistemas de integração robustos e escaláveis.

Os padrões EIP resolvem desafios como:
- **Como conectar sistemas heterogêneos?** → Message Endpoint
- **Como transformar mensagens entre formatos?** → Message Translator
- **Como dividir mensagens grandes?** → Splitter
- **Como agrupar múltiplas mensagens?** → Aggregator
- **Como rotear mensagens baseado em conteúdo?** → Content-Based Router
- **Como tratar erros de forma resiliente?** → Error Handler

### Por que usar EIP?

1. **Padronização**: Soluções testadas e comprovadas para problemas comuns
2. **Manutenibilidade**: Código mais legível e fácil de manter
3. **Documentação**: Os padrões servem como documentação viva do sistema
4. **Reutilização**: Padrões podem ser combinados para resolver problemas complexos

### Apache Camel e EIP

O Apache Camel é um framework de integração open-source que implementa mais de 50 padrões EIP de forma nativa. O Camel permite implementar integrações complexas usando uma sintaxe declarativa e baseada em padrões, sem precisar escrever código de integração manualmente.

**Vantagens do Apache Camel:**
- Implementação robusta de padrões EIP
- Suporte a mais de 300 componentes (HTTP, JMS, FTP, Database, etc.)
- Múltiplas DSLs (Domain-Specific Languages): Java, XML, YAML, Groovy, Scala
- Tratamento de erros e recuperação automática
- Observabilidade e métricas integradas
- Testes facilitados

### Suporte do Camel para Rotas YAML

O Apache Camel oferece suporte nativo para definir rotas usando a sintaxe YAML através do **Camel YAML DSL**. Esta é uma das DSLs mais recentes e modernas do Camel, oferecendo várias vantagens:

#### Vantagens do YAML DSL

1. **Legibilidade**: Sintaxe limpa e fácil de ler, especialmente para pessoas não-familiarizadas com programação Java
2. **Declarativo**: Descreve **o que** fazer, não **como** fazer
3. **Versionamento**: Arquivos YAML são facilmente versionados e comparados em sistemas de controle de versão
4. **Separação de Responsabilidades**: Lógica de negócio separada do código Java
5. **Colaboração**: Não-programadores podem entender e até modificar rotas
6. **Manutenção**: Mais fácil de manter e atualizar que código Java

#### Como Funciona no CamelBridge

No CamelBridge, as rotas YAML são:

1. **Carregadas Automaticamente**: O Camel Main carrega automaticamente todos os arquivos `.yaml` e `.yml` da pasta `routes/` configurada em `application.properties`:
   ```properties
   camel.main.routes-include-pattern = routes/*.yaml,routes/*.yml
   ```

2. **Processadas em Tempo de Execução**: As rotas são lidas e compiladas quando a aplicação inicia

3. **Equivalente ao Java DSL**: Cada construção YAML tem equivalência direta com o Java DSL do Camel, oferecendo toda a funcionalidade

4. **Suporte Completo a EIP**: Todos os padrões EIP suportados pelo Camel estão disponíveis na sintaxe YAML

#### Exemplo de Equivalência

**YAML DSL:**
```yaml
- from:
    uri: "cron:minha-rota?schedule={{cronExpression}}"
    steps:
      - log: "Processando mensagem"
      - marshal:
          json:
            library: Jackson
```

**Equivalente em Java DSL:**
```java
from("cron:minha-rota?schedule={{cronExpression}}")
    .log("Processando mensagem")
    .marshal().json(JsonLibrary.Jackson);
```

Ambos produzem exatamente o mesmo resultado, mas o YAML é mais acessível para não-programadores e mais fácil de versionar.

### Padrões EIP no CamelBridge

No CamelBridge, você encontrará diversos padrões EIP sendo utilizados nas rotas:

- **Message Endpoint**: Definição de pontos de entrada (cron, timer, HTTP)
- **Message Translator**: Transformação de formatos (JSON, XML) e modificação de cabeçalhos
- **Content Enricher**: Busca de dados externos (consultas SQL)
- **Splitter**: Divisão de mensagens compostas em mensagens individuais
- **Aggregator**: Combinação de múltiplas mensagens
- **Messaging Gateway**: Invocação de serviços externos (HTTP)
- **Content-Based Router**: Roteamento condicional baseado no conteúdo
- **Error Handler**: Tratamento de erros e recuperação

Cada padrão será detalhado com exemplos práticos nas seções seguintes deste README.

## 🛣️ Criando Rotas YAML

As rotas devem ser criadas na pasta `routes/` com extensão `.yaml` ou `.yml`. O Camel Main carrega automaticamente todos os arquivos YAML deste diretório.

### Estrutura Básica de uma Rota

```yaml
- from:
    uri: "cron:minha-rota?schedule={{cronExpression}}"
    steps:
      - log: "Minha mensagem de log"
      # ... outros passos
```

## 📚 Padrões e Exemplos

### 1. Ponto de Entrada (Message Endpoint)

Define o ponto de entrada da rota. Pode ser um scheduler (cron), timer, endpoint HTTP, fila, etc.

#### Exemplo 1: Cron (Agendamento)

```yaml
- from:
    uri: "cron:minha-rota?schedule={{cronExpression}}"
    steps:
      - log: "Rota executada por cron"
```

#### Exemplo 2: Timer (Intervalo Fixo)

```yaml
- from:
    uri: "timer:minha-rota?period=5000"
    steps:
      - log: "Rota executada a cada 5 segundos"
```

#### Exemplo 3: Endpoint HTTP (REST)

```yaml
- from:
    uri: "platform-http:/api/dados?httpMethodRestrict=POST"
    steps:
      - log: "Requisição recebida: ${body}"
```

### 2. Logging

Registra informações durante o processamento da rota.

```yaml
- log: "Mensagem simples de log"

# Log com expressão
- log:
    message: "Processando registro: ${body.usuario_id}"

# Log com nível específico
- log:
    loggingLevel: "DEBUG"
    message: "Mensagem de debug: ${body}"
```

### 3. Modificação de Cabeçalhos (Message Translator)

Adiciona ou modifica cabeçalhos da mensagem para passar metadados entre os passos.

```yaml
# Definir cabeçalho com valor estático
- set-header:
    name: Content-Type
    simple: "application/json"

# Definir cabeçalho com valor de propriedade
- set-header:
    name: ULTIMA_SINCRONIZACAO
    simple: "{{lastSearch}}"

# Definir cabeçalho com expressão
- set-header:
    name: USUARIO_ID
    simple: "${body.usuario_id}"

# Definir múltiplos cabeçalhos
- set-header:
    name: Authorization
    simple: "Bearer ${header.token}"
- set-header:
    name: X-Custom-Header
    simple: "Valor customizado"
```

### 4. Consulta SQL (Content Enricher)

Executa consultas SQL no banco de dados configurado.

```yaml
# Consulta simples
- to:
    uri: "sql:SELECT * FROM USUARIOS WHERE ATIVO = 1?dataSource=#dataSource"

# Consulta com parâmetros do cabeçalho
- to:
    uri: "sql:SELECT * FROM USUARIOS WHERE MODIFICADO > :#ULTIMA_SINCRONIZACAO?dataSource=#dataSource"

# Consulta com parâmetros do corpo
- to:
    uri: "sql:SELECT * FROM PEDIDOS WHERE CLIENTE_ID = :#CLIENTE_ID?dataSource=#dataSource"

# Inserção/Atualização
- to:
    uri: "sql:UPDATE USUARIOS SET ULTIMA_SINCRONIZACAO = NOW() WHERE ID = :#ID?dataSource=#dataSource"
```

### 5. Splitter (Divisão de Mensagens)

Divide uma mensagem composta (lista) em múltiplas mensagens individuais para processamento separado.

```yaml
# Dividir lista de objetos
- split:
    expression:
      simple: "${body}"
    steps:
      - log: "Processando item: ${body}"

# Dividir com agregação (coletar resultados)
- split:
    expression:
      simple: "${body}"
    aggregation-strategy: "#aggregationStrategy"
    steps:
      - process: "#processadorItem"

# Dividir JSON array
- split:
    jsonpath: "$.items[*]"
    steps:
      - log: "Item: ${body}"
```

### 6. Agregação (Aggregator)

Combina múltiplas mensagens em uma única mensagem.

```yaml
# Agregar após split
- split:
    expression:
      simple: "${body}"
    steps:
      - to: "direct:processarItem"
- aggregate:
    correlation-expression:
      constant: true
    completion-size:
      constant: 10
    steps:
      - log: "Processados 10 itens: ${body}"
```

### 7. Serialização JSON (Marshal)

Converte objetos Java para formato JSON.

```yaml
# Marshal simples com Jackson
- marshal:
    json:
      library: Jackson

# Marshal com configurações
- marshal:
    json:
      library: Jackson
      prettyPrint: true

# Marshal XML para JSON (requer componente XML)
- unmarshal:
    xmljson:
      rootName: "root"
- marshal:
    json:
      library: Jackson
```

### 8. Desserialização JSON (Unmarshal)

Converte JSON para objetos Java.

```yaml
# Unmarshal JSON simples
- unmarshal:
    json:
      library: Jackson

# Unmarshal para classe específica
- unmarshal:
    json:
      library: Jackson
      unmarshalType: "br.eagletecnologia.modelo.Usuario"
```

### 9. Chamadas HTTP (Messaging Gateway)

Realiza chamadas HTTP para APIs externas.

```yaml
# POST simples
- to:
    uri: "https://api.exemplo.com/usuarios?httpMethod=POST"

# POST com cabeçalhos
- set-header:
    name: Content-Type
    simple: "application/json"
- set-header:
    name: Authorization
    simple: "Bearer ${header.token}"
- to:
    uri: "https://api.exemplo.com/usuarios?httpMethod=POST"

# GET com parâmetros de query
- to:
    uri: "https://api.exemplo.com/usuarios?id=${header.USUARIO_ID}&httpMethod=GET"

# PUT
- to:
    uri: "https://api.exemplo.com/usuarios/${header.USUARIO_ID}?httpMethod=PUT"

# DELETE
- to:
    uri: "https://api.exemplo.com/usuarios/${header.USUARIO_ID}?httpMethod=DELETE"

# Com timeout e retry
- to:
    uri: "https://api.exemplo.com/usuarios?httpMethod=POST&connectTimeout=5000&socketTimeout=10000"

# Acessar código de resposta HTTP
- to:
    uri: "https://api.exemplo.com/usuarios?httpMethod=POST"
- log: "Status HTTP: ${header.CamelHttpResponseCode}"
```

### 10. Transformação de Dados (Transform)

Transforma o conteúdo do corpo da mensagem usando expressões.

```yaml
# Transformação simples
- transform:
    simple: "${body.usuario_id}"

# Transformação complexa
- transform:
    simple: "${body.nome} ${body.sobrenome}"

# Criar novo JSON
- set-body:
    simple:
      value: |
        {
          "id": "${body.usuario_id}",
          "nome": "${body.nome}",
          "ativo": "${body.ativo}"
        }
```

### 11. Filtragem (Content Filter)

Filtra mensagens baseado em condições.

```yaml
# Filtrar mensagens
- filter:
    simple: "${body.ativo} == true"
    steps:
      - log: "Usuário ativo: ${body}"

# Filtrar com expressão complexa
- filter:
    simple: "${body.idade} >= 18 && ${body.ativo} == true"
    steps:
      - log: "Usuário válido: ${body}"
```

### 12. Tratamento de Erros (Error Handler)

Define estratégias de tratamento de erros.

```yaml
- from:
    uri: "cron:minha-rota?schedule={{cronExpression}}"
    error-handler:
      dead-letter-channel:
        uri: "direct:erro-handler"
    steps:
      - to: "direct:processar"
      
# Rota de tratamento de erros
- from:
    uri: "direct:erro-handler"
    steps:
      - log: "Erro ocorrido: ${exception.message}"
      - log: "Mensagem original: ${body}"

# Try-Catch-Finally
- do-try:
    steps:
      - to: "direct:operacao-risco"
    do-catch:
      - exception:
          - java.lang.Exception
        steps:
          - log: "Erro capturado: ${exception.message}"
    do-finally:
      steps:
        - log: "Sempre executa (cleanup)"
```

### 13. Roteamento Condicional (Content-Based Router)

Roteia mensagens para diferentes destinos baseado em condições.

```yaml
# Choice (switch case)
- choice:
    when:
      - simple: "${body.status} == 'ATIVO'"
        steps:
          - to: "direct:processar-ativo"
      - simple: "${body.status} == 'INATIVO'"
        steps:
          - to: "direct:processar-inativo"
    otherwise:
      steps:
        - log: "Status desconhecido: ${body.status}"

# Choice com múltiplas condições
- choice:
    when:
      - simple: "${body.idade} < 18"
        steps:
          - log: "Menor de idade"
      - simple: "${body.idade} >= 18 && ${body.idade} < 65"
        steps:
          - log: "Adulto"
      - simple: "${body.idade} >= 65"
        steps:
          - log: "Idoso"
```

### 14. Loops e Iterações

```yaml
# Loop simples
- loop:
    constant: 10
    steps:
      - log: "Iteração ${header.CamelLoopIndex}"

# Loop sobre lista
- loop:
    simple: "${body.items.size()}"
    steps:
      - log: "Processando item ${header.CamelLoopIndex}: ${body.items[${header.CamelLoopIndex}]}"
```

### 15. Delay e Throttling

```yaml
# Delay entre mensagens
- delay:
    constant: 1000  # 1 segundo

# Throttle (limitar taxa)
- throttle:
    constant: 10  # Máximo 10 mensagens
    time-period-millis: 60000  # por minuto
    steps:
      - to: "direct:processar"
```

### 16. Multicast (Envio Paralelo)

Envia a mesma mensagem para múltiplos destinos em paralelo ou sequencial.

```yaml
# Multicast paralelo
- multicast:
    parallel-processing: true
    steps:
      - to: "direct:destino1"
      - to: "direct:destino2"
      - to: "direct:destino3"

# Multicast sequencial
- multicast:
    steps:
      - to: "direct:destino1"
      - to: "direct:destino2"
```

## ⚙️ Propriedades de Configuração

As propriedades são definidas em `src/main/resources/application.properties`:

```properties
# Configuração do Camel
camel.main.name = CamelBridge
camel.main.routes-include-pattern = routes/*.yaml,routes/*.yml

# Expressão Cron global
cronExpression = 0/50 * * * * ?

# Timestamp da última busca
lastSearch = 2025-12-25 16:34:00

# Configuração do Banco de Dados
db.url = jdbc:firebirdsql://localhost:3050/FINANCEiro
db.username = SYSDBA
db.password = masterkey
db.driver = org.firebirdsql.jdbc.FBDriver
db.pool.minimumIdle = 5
db.pool.maximumPoolSize = 20
db.pool.connectionTimeout = 30000
db.pool.idleTimeout = 600000
db.pool.maxLifetime = 1800000
db.pool.connectionTestQuery = SELECT 1 FROM RDB$DATABASE
```

### Usando Propriedades nas Rotas

Use `{{propriedade}}` para referenciar propriedades do arquivo de configuração:

```yaml
- set-header:
    name: ULTIMA_SINCRONIZACAO
    simple: "{{lastSearch}}"
```

## 🚀 Executando a Aplicação

### Desenvolvimento

```bash
mvn camel:run
```

### Produção

```bash
mvn clean package
java -jar target/camelbridge-1.0.0-SNAPSHOT.jar
```

## 📖 Exemplo Completo de Rota

```yaml
# ============================================================================
# Rota de Sincronização de Usuários
# ============================================================================
- from:
    uri: "cron:sincronizar-usuarios?schedule={{cronExpression}}"
    steps:
      - log: "Iniciando sincronização de usuários"
      
      # Define cabeçalho com última sincronização
      - set-header:
          name: ULTIMA_SINCRONIZACAO
          simple: "{{lastSearch}}"
      
      - log: "Buscando usuários modificados após: ${header.ULTIMA_SINCRONIZACAO}"
      
      # Consulta SQL no banco
      - to:
          uri: "sql:SELECT USUARIOS_ID, FUNCIONARIOS_ID, USUARIO_LOGIN, USUARIO_SENHA, PERFIS_ID, USUARIO_INATIVO FROM USUARIOS WHERE MODIFICADO > :#ULTIMA_SINCRONIZACAO?dataSource=#dataSource"
      
      - log: "Registros encontrados: ${body.size()} usuário(s)"
      
      # Divide em registros individuais
      - split:
          expression:
            simple: "${body}"
          steps:
            - log: "Processando usuário: ${body}"
            
            # Converte para JSON
            - marshal:
                json:
                  library: Jackson
            
            # Define cabeçalho HTTP
            - set-header:
                name: Content-Type
                simple: "application/json"
            
            # Envia para API externa
            - to:
                uri: "https://rbaskets.in/usuarios?httpMethod=POST"
            
            - log: "Usuário enviado com sucesso. Status HTTP: ${header.CamelHttpResponseCode}"
```

## 🔗 Referências

- [Apache Camel Documentation](https://camel.apache.org/manual/)
- [Camel YAML DSL](https://camel.apache.org/manual/yaml-dsl.html)
- [Enterprise Integration Patterns](https://www.enterpriseintegrationpatterns.com/)
- [Camel Components](https://camel.apache.org/components/)

## 📄 Licença

Este projeto está licenciado sob a Apache License 2.0.

