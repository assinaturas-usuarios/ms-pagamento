package com.empresa.pagamento.domain.port.out;

import com.empresa.pagamento.application.dto.PagamentoResultadoEvent;

/**
 * Interface que define o contrato para publicar o resultado do pagamento no Kafka.
 */
public interface PagamentoResultadoProducerPort {

  /**
   * Publica o resultado do pagamento no Kafka para o ms-assinatura.
   *
   * @param evento evento com o resultado do processamento
   */
  void publicar(PagamentoResultadoEvent evento);
}
