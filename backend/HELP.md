# Instruções de Uso da API de Petshop

Bem-vindo à API de controle de atendimentos do Petshop! Este guia fornecerá instruções sobre como usar a API para realizar diferentes operações, seja como administrador ou cliente.

## Acesso como Administrador

### 1. Autenticação

Para acessar a API como administrador, você deve primeiro autenticar-se usando suas credenciais de administrador.

1. Faça uma requisição POST para `/auth/login` com as credenciais de administrador (usuário e senha).
2. O servidor retornará um token de acesso.
3. Inclua o token de acesso em todas as requisições subsequentes no cabeçalho `Authorization` no formato `Bearer {token}`.

### 2. Operações de Administração

Como administrador, você tem permissão para realizar as seguintes operações:

- Incluir, excluir, alterar e visualizar qualquer cadastro.
- Gerenciar usuários, clientes, pets, atendimentos, raças, contatos e endereços.

## Acesso como Cliente

### 1. Autenticação

Clientes também precisam se autenticar para acessar a API e realizar operações relacionadas aos seus próprios registros e/ou registros dos seus pets.

1. Faça uma requisição POST para `/auth/login` com as credenciais do cliente (usuário e senha).
2. O servidor retornará um token de acesso.
3. Inclua o token de acesso em todas as requisições subsequentes no cabeçalho `Authorization` no formato `Bearer {token}`.

### 2. Operações do Cliente

Como cliente, você tem permissão para realizar as seguintes operações:

- Visualizar e alterar seus próprios registros e/ou registros dos seus pets.

## Endpoints Disponíveis

Aqui estão os principais endpoints da API que você pode acessar:

- `GET /users`: Obter informações sobre os usuários.
- `GET /clients`: Visualizar informações dos clientes.
- `GET /pets`: Visualizar informações dos pets.
- `GET /assistances`: Visualizar informações dos atendimentos.
- `GET /breeds`: Visualizar informações das raças.

- `GET /users/{cpf}`: Obter informações de um usuário.
- `GET /clients/{id}`: Visualizar informações de um cliente.
- `GET /pets/{id}`: Visualizar informações de um pet.
- `GET /assistances/{id}`: Visualizar informações de um atendimento.
- `GET /breeds/{id}`: Visualizar informações de uma raça.

- `POST /users`: Inserir um novo usuário.
- `POST /clients`: Inserir um novo cliente.
- `POST /pets`: Inserir um novo pet.
- `POST /assistances`: Inserir um novo atendimento.
- `POST /breeds`: Inserir uma nova raça.

- `PUT /users/{cpf}`: Atualizar um usuário.
- `PUT /clients/{id}`: Atualizar um cliente.
- `PUT /pets/{id}`: Atualizar um pet.
- `PUT /assistances/{id}`: Atualizar um atendimento.
- `PUT /breeds/{id}`: Atualizar uma raça.
- `PUT /addresses/{id}`: Atualizar um endereço.
- `PUT /contacts/{id}`: Atualizar um contato.

- `DELETE /users/{cpf}`: Excluir um usuário.
- `DELETE /clients/{id}`: Excluir um cliente.
- `DELETE /pets/{id}`: Excluir um pet.
- `DELETE /assistances/{id}`: Excluir um atendimento.
- `DELETE /breeds/{id}`: Excluir uma raça.

Esta é uma visão geral das operações possíveis. Consulte a documentação completa da API para obter detalhes sobre parâmetros, corpos de requisição e respostas para cada endpoint.

Se você tiver alguma dúvida ou encontrar problemas durante o uso da API, entre em contato com nossa equipe de suporte em [suporte@petshop.com](mailto:suporte@petshop.com).

Agradecemos por utilizar nossa API de petshop!
