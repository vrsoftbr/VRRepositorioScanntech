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
```properties
github.user=seuuser
github.token=seutoken
```
---

## Produção Passo-a-Passo:
1° Verifique o Docker:
- Certifique-se de que o Docker Desktop está em execução.
- Confirme que você está logado na conta vrsoftbr.

2° Configure o Banco de dados:
- Abra o arquivo vr.properties.
- Verifique se a configuração de base de bancos não está apontando para localhost, mas sim para o IP correto do servidor de banco.

3° Inicie os containers:
- No terminal em que o docker-compose se entroca execute:
  ```
  docker-compose up -d
  ```
  
4° Acesse a interface:
- No navegador, abra: http://localhost:9011/ui/data-envio
- Importante: Se estiver acessando de Outra máquina, substitua localhost pelo IP da máquina que esta rodando o container.

5° Reinicie os containers(se necessário):
- Caso tenha alterado a configuração do banco de dados no vr.properties, execute:
  ```
  docker-compose restart
  ```
---
## Desenvolvimento:

- Não é necessário seguir o mesmo procedimento utilizado em produção.
Ao realizar o processo de debug, o projeto utiliza o arquivo vr.properties da sua máquina.
Isso significa que qualquer alteração pode ser testada localmente, sem a necessidade de alterar a imagem do Docker.
---

## Geração de Imagens:
Passos para gerar a imagem que será utilizada pelo container:

1° Definar a tag latest que será subistituida:
```
docker tag vrreprocessarscanntech:latest vrsoftbr/vrreprocessarscanntech:<versão>
```
(Substitua <versão> pela versão da imagem)

2° Gerar (build) a nova imagem:
```
docker build --secret id=gradle_props,src=C:\Users\Vr-Fulano\.gradle\gradle.properties -t vrreprocessarscanntech:<versão> .
```

3° Definir a imagem criada como latest:
```
docker tag vrreprocessarscanntech:<versão> vrsoftbr/vrreprocessarscanntech:latest
```

4° Enviar a imagem para o repositório (push)
```
docker push vrsoftbr/vrreprocessarscanntech:latest
```

