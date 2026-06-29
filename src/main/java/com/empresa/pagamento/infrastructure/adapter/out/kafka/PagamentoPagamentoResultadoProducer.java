package com.empresa.pagamento.infrastructure.adapter.out.kafka;

import com.empresa.pagamento.application.dto.PagamentoResultadoEvent;
import com.empresa.pagamento.domain.port.out.PagamentoResultadoProducerPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Componente responsável por publicar eventos de resultado de pagamento no Kafka.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class PagamentoPagamentoResultadoProducer implements PagamentoResultadoProducerPort {

  private final KafkaTemplate<String, Object> kafkaTemplate;

  @Value("${kafka.topics.pagamento-resultado}")
  private String topico;

  @Override
  public void publicar(PagamentoResultadoEvent evento) {
    log.info("Publicando resultado de pagamento no Kafka: assinaturaId={}, status={}", evento.assinaturaId(),
        evento.status());
    kafkaTemplate.send(topico, evento.assinaturaId().toString(), evento);
  }
}
