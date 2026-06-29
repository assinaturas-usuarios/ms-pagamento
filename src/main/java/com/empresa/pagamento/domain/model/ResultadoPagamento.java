package com.empresa.pagamento.domain.model;

import java.util.UUID;

/**
 * Interface selada que representa o resultado do processamento de um pagamento. Pode ser Aprovado
 * ou Recusado.
 */
public sealed interface ResultadoPagamento
    permits ResultadoPagamento.Aprovado, ResultadoPagamento.Recusado {

  /**
   * Retorna o identificador do pagamento associado ao resultado.
   *
   * @return UUID do pagamento
   */
  UUID pagamentoId();

  /**
   * Classe que representa um pagamento aprovado.
   */
  record Aprovado(UUID pagamentoId) implements ResultadoPagamento {

  }

  /**
   * Classe que representa um pagamento recusado.
   */
  record Recusado(UUID pagamentoId, String motivo) implements ResultadoPagamento {

  }
}
