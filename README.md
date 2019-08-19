# Versão Back-end

``` 
Serviço para gerenciar tarefas
```
## Algumas orientações

**Necessário alterar as informações de conexão com o SGBD MySQL para iniciar**

Localização:
```
GerenciadorTarefasBackend > src > main > resources > META-INF > persistence.xml

15 : javax.persistence.jdbc.url
16 : javax.persistence.jdbc.user
17 : javax.persistence.jdbc.password
```

**Alteração Opcional**
```
GerenciadorTarefasBackend > src > main > resources > application.properties

1: server.port=${port:8080}
```

### 08/2019
