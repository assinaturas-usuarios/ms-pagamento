package com.empresa.pagamento.infrastructure.simulador;

import com.empresa.pagamento.domain.model.ResultadoPagamento;
import java.math.BigDecimal;
import java.util.Random;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Componente que simula o processamento de pagamentos, determinando se um pagamento é aprovado ou
 * recusado com base em uma taxa de aprovação configurável.
 */
@Component
@Slf4j
public class SimuladorPagamento {

  private final Random random = new Random();

  @Value("${pagamento.taxa-aprovacao:0.8}")
  private double taxaAprovacao;

  /**
   * Simula o processamento de um pagamento com taxa de aprovação configurável.
   *
   * @param assinaturaId identificador da assinatura
   * @param valor        valor a ser cobrado
   * @return resultado do pagamento (Aprovado ou Recusado)
   */
  public ResultadoPagamento simular(UUID assinaturaId, BigDecimal valor) {
    log.info("Simulando pagamento: assinaturaId={}, valor={}", assinaturaId, valor);
    UUID pagamentoId = UUID.randomUUID();
    if (random.nextDouble() <= taxaAprovacao) {
      return new ResultadoPagamento.Aprovado(pagamentoId);
    }
    return new ResultadoPagamento.Recusado(pagamentoId, "Pagamento recusado pela operadora");
  }
}
