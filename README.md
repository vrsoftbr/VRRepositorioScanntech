# VR Reprocessar Scanntech

Aplicação **Spring Boot** para reprocessamento de dados Scanntech, com suporte a configuração dinâmica de bancos de dados via arquivo `vr.properties`.  
O projeto foi preparado para rodar de forma isolada em **containers Docker**.

---

## **Pré-requisitos**
- [Docker](https://docs.docker.com/get-docker/)
- [Docker Compose](https://docs.docker.com/compose/install/)
- Banco de dados **PostgreSQL** (local, remoto ou containerizado)

---

## **Configuração do Banco de Dados**
A aplicação utiliza um arquivo externo `vr.properties` para configurar a conexão com o banco de dados.

### **Exemplo de `vr.properties`**
```properties
database.usuario=postgres
database.senha=postgres
database.ip=123.456.7.890
database.porta=5432
database.nome=reprocessarscanntech
database.alias=ReprocessarScanntech
```
Importante:
- Evite localhost (não funciona de dentro do container)
- Se estiver em outro servidor, use o IP da máquina onde o PostgreSQL está rodando.
---

## **Variáveis de Ambiente**
No momento de realizar o build do projeto será necessário que existam as seguintes variáveis em seu gradle.properties:
```
github.user=seuuser
github.token=seutoken
```
---

## Produção

Para utilizar a aplicação execute o comandoPara utilizar a aplicação, execute o comando abaixo no terminal, dentro da pasta onde está localizado o arquivo docker-compose.yml:
```
docker compose up -d
```
Caso seja necessário trocar o banco em uso, comente a configuração atual (ou renomeie-a para database2), descomente 
o banco que será utilizado e execute o comando:
```
docker compose restart
```
---

## Desenvolvimento:

Para testar qualquer alteração, não é necessário executar comandos Docker, pois o programa lê diretamente o arquivo 
vr.properties presente na sua máquina. Basta iniciar o processo em modo de debug.




