# Short URL

Aplicação para geração de URL encurtada.

## Tecnologias utilizadas:

- Java
- Aws Lambda
- S3 Storage
- Aws Api Getway

## Funcionalidades:

Para gerar a URL encurtada, faça uma requisição HTTP do tipo __POST__ para:

```
  {url-application}/create
```

Passando o seguinte __JSON__ no corpo da requisição:

```json
  {
    "originalUrl" : "https://example.com.br",
    "expirationTime" : "123i4101"  // date in epoch (seconds)
  }
```

Em caso de sucesso, receberá no corpo da resposta um `código único` que representa a URL encurtada.

```json
  {
    "code" : "12df345g"
  }
```

Agora para utilizar a URL encurtada, basta chamar o seguinte endpoint do tipo __GET__:

```
  {url-application}/{code}
```

Em caso de sucesso, será redirecionado para a URL original. 

> Obs: A URL orginial da aplicação não foi exposta para evitar billing

## Aprendizados:

Este meu primeiro contato com serviços da AWS (Amazon Web Services), pude aprender como utilizar das funções Lambdas, fazer a persistência
de dados com o S3 Storage e como integrar serviços com o Aws API Getway.



