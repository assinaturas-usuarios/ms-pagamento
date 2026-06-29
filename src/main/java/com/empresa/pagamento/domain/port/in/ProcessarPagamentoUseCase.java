package com.empresa.pagamento.domain.port.in;

import com.empresa.pagamento.application.dto.RenovacaoSolicitadaEvent;

/**
 * Interface que define o caso de uso para processar o pagamento de uma renovação solicitada.
 */
public interface ProcessarPagamentoUseCase {

  /**
   * Processa o pagamento de uma renovação solicitada pelo ms-assinatura.
   *
   * @param evento evento com os dados da renovação
   */
  void processar(RenovacaoSolicitadaEvent evento);
}
