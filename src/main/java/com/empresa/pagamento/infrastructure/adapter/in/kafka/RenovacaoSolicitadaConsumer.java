package com.empresa.pagamento.infrastructure.adapter.in.kafka;

import com.empresa.pagamento.application.dto.RenovacaoSolicitadaEvent;
import com.empresa.pagamento.domain.port.in.ProcessarPagamentoUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * Componente que consome eventos de renovação solicitada do Kafka e delega o processamento para o
 * use case de processar pagamento.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RenovacaoSolicitadaConsumer {

  private final ProcessarPagamentoUseCase useCase;

  /**
   * Método que consome eventos de renovação de assinatura solicitada do Kafka.
   *
   * @param evento evento com os dados da renovação solicitada
   * @param ack    objeto para confirmação de processamento
   */
  @KafkaListener(
      topics = "${kafka.topics.renovacao-solicitada}",
      groupId = "${spring.kafka.consumer.group-id}",
      containerFactory = "kafkaListenerContainerFactory"
  )
  public void consumir(@Payload RenovacaoSolicitadaEvent evento, Acknowledgment ack) {
    log.info("Recebendo solicitação de renovação via Kafka: assinaturaId={}", evento.assinaturaId());
    try {
      useCase.processar(evento);
      ack.acknowledge();
    } catch (Exception e) {
      log.error("Erro ao processar renovação: assinaturaId={}", evento.assinaturaId(), e);
      throw e;
    }
  }
}
