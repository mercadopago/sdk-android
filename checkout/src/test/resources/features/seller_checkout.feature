# language: pt
Funcionalidade: Resultado do checkout para o seller
  Como integrador do SDK, preciso que o resultado do checkout seja sempre
  um dos contratos publicos: Success, Error ou UserCancelled.

  Cenario: pagamento aprovado retorna Success
    Dado um checkout iniciado com um meio de pagamento valido
    Quando o pagamento e aprovado
    Entao o resultado deve ser "Success"

  Cenario: usuario volta na primeira tela retorna UserCancelled
    Dado um checkout iniciado com um meio de pagamento valido
    Quando o usuario cancela na tela inicial
    Entao o resultado deve ser "UserCancelled"

  Cenario: falha de rede retorna Error
    Dado um checkout iniciado com um meio de pagamento valido
    Quando ocorre uma falha de rede
    Entao o resultado deve ser "Error"
