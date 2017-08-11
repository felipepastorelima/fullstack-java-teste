## Começando

## Pré requisitos
* [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) instalado e JAVA_HOME configurado no path
* [Maven 3](https://maven.apache.org/) instalado e configurado no path
* [Node 6+](https://nodejs.org/en/) instalado e configurado no path
* [Tomcat 8](https://tomcat.apache.org/download-80.cgi) instalado e rodando.
* [Google Chrome](https://www.google.com/chrome/) para testes E2E do client

## Configurações padrão do Tomcat

O endereço padrão da API é: http://localhost:8080/contabilizei-server.

Caso o Tomcat não esteja com as configurações padrão, ajustar arquivo:
- `contabilizei-client/app/environment/environment.dev.js`

## Gerar WAR da API

    # Windows
    scripts/windows/1-gerar-war-api.bat

    # Mac
    sh scripts/mac/1-gerar-war-api.sh

- Após gerado o war, ele estará disponível em: `contabilizei-server/target/contabilizei-server.war`.
- Implantar o war no Tomcat.

## Iniciar client na porta 8000

    # Windows
    scripts/windows/2-iniciar-client-porta-8000.bat

    # Mac
    sh scripts/mac/2-iniciar-client-porta-8000.sh 

Primeira execução pode demorar pois irá instalar as dependências do projeto.

Para alterar a porta, modificar os arquivos:
- `contabilizei-client/package.json: scripts.start`.
- `contabilizei-client/protractor.conf.js: exports.config.baseUrl`

## Rodar testes da API

    # Windows
    scripts/windows/3-rodar-testes-api.bat

    # Mac
    sh scripts/mac/3-rodar-testes-api.sh       

## Rodar testes do client E2E

    # Windows
    scripts/windows/4-rodar-testes-client-depende-script-2.bat

    # Mac
    sh scripts/windows/4-rodar-testes-client-depende-script-2.sh    

- Os testes do client dependem do servidor client ativo (script 2).
- Os testes do client NÃO dependem da API, pois as chamadas Http são todas mockadas.

## Documentação da API

A documentação da API encontra-se em: [docs/api.html](docs/api.html).

Documentação foi gerada utilizando a ferramenta [postmanerator](https://github.com/aubm/postmanerator).

## Limitações

Não funciona corretamente no Internet Explorer.

## Tecnologias

* Aplicação pura Java EE
* RESTful API JAX-RS utilizando Servlets ou framework Jersey
* Banco de dados HSQLDB com JPA

### FRONT-END

* AngularJS 1.x
* Angular Material Design
* Protractor para testes

## Decisões de arquitetura

### Programação orientada a testes

Desenvolver orientado a testes além de criar mais segurança durante o ciclo do projeto, 
é mais motivador, pois a cada pequena funcionalidade desenvolvida e testada se tem uma 'vitória'.
Por isso, mesmo sendo um sistema demonstrativo, achei imprescindível o desenvolvimento de testes, tanto na camada de frontend quanto na de backend. 

### Organização dos arquivos por domínio/entidade

É uma prática mais convencional entre programadores frontend do que backend, porém adotei em ambas as camadas.
Acredito deixar os arquivos mais planos (menos sub-pastas) e mais fáceis de se encontrar.
Por exemplo, se em uma tarefa é necessário modificar a entidade de um domínio, 
normalmente também são necessárias modificações no DAO, Service, etc.. E nesta arquitetura estão todos na mesma pasta.

### Cálculo dos impostos utilizando o padrão Strategy
Para o cálculo dos impostos utilizou-se o padrão strategy.
A implementação encontra-se na pasta: `contabilizei-server/src/main/java/br/com/contabilizei/server/tax/generators`.

* Cons: Mais complexo para entender.
* Prós: Facilidade de mudanças com o mínimo de modificação no código existente.

Por exemplo, supondo que fosse necessário criar um novo imposto fictício para o Simples Nacional cujo valor seria fixo e a data de vencimento no dia 10 do próximo mês.

- Criar e implementar classe `.../tax/generators/strategies/dueDate/TaxDueDate10thDayOfNextMonthStrategy`.
- Criar e implementar classe `.../tax/generators/strategies/amount/TaxAmountFixedAmountStrategy`
- Criar e implementar classe `.../tax/generators/single/NovoImpostoTaxGenerator` utilizando as estratégias anteriormente criadas.
- Adicionar o novo imposto na classe `.../tax/generators/SimplesNacionalTaxesGenerator`.

O risco de adicionar bugs em algum processo existente seria mínimo, pois o único pedaço de código existente que seria modificado seria o da classe: `.../tax/generators/SimplesNacionalTaxesGenerator`.

### Não validação de email
A validação de email foi deixada de lado de propósito, pois em um sistema real é enviado um email
de verificação, o que faz a validação de email por regex redundante.
Mais detalhes em: https://davidcel.is/posts/stop-validating-email-addresses-with-regex/

## Bonus

Se voce fosse utilizar esse sistema comercialmente, que alterações vc faria para escalar e/ou facilitar a vida do usuario? OBS: Voce pode descrever isso aqui ou mostrar na implementação.

- Utilizar banco de dados e servidor escalável na núvem. (Opção 1 sugerida por este teste).
- Implementar HTTPS.
- Melhorar retornos de erro. Na implementação atual retorna apenas 400 para erros de validação e 500 para erros de programação ou inesperados.
- Confirmação e undo das ações. Ex.: Deletar empresa, nota fiscal e regerar impostos.
- Criar autenticação.
- Auditoria: Usuário que criou, alterou ou deletou e o horário da operação.
- Monitoramento do servidor e logs.
- Envio de emails aos usuários.
- Criar páginas de erros: 404, etc..
- Integração com outros sistemas (prefeitura, etc)
