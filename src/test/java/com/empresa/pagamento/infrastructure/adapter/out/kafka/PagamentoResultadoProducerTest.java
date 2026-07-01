package com.empresa.pagamento.infrastructure.adapter.out.kafka;

import static org.mockito.Mockito.verify;

import com.empresa.pagamento.application.dto.PagamentoResultadoEvent;
import com.empresa.pagamento.domain.model.StatusPagamento;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
@DisplayName("PagamentoPagamentoResultadoProducer")
class PagamentoPagamentoResultadoProducerTest {

  @Mock
  private KafkaTemplate<String, Object> kafkaTemplate;

  private PagamentoPagamentoResultadoProducer producer;

  @BeforeEach
  void setUp() {
    producer = new PagamentoPagamentoResultadoProducer(kafkaTemplate);
    ReflectionTestUtils.setField(producer, "topico", "pagamento-resultado");
  }

  @Test
  @DisplayName("Deve publicar evento no Kafka")
  void devePublicarEvento() {
    UUID assinaturaId = UUID.randomUUID();
    UUID pagamentoId = UUID.randomUUID();
    PagamentoResultadoEvent evento = new PagamentoResultadoEvent(assinaturaId, pagamentoId, "APROVADO");

    producer.publicar(evento);

    verify(kafkaTemplate).send("pagamento-resultado", assinaturaId.toString(), evento);
  }

  @Test
  @DisplayName("Deve publicar evento com status recusado")
  void devePublicarEventoRecusado() {
    UUID assinaturaId = UUID.randomUUID();
    UUID pagamentoId = UUID.randomUUID();
    PagamentoResultadoEvent evento = new PagamentoResultadoEvent(assinaturaId, pagamentoId, "RECUSADO");

    producer.publicar(evento);

    verify(kafkaTemplate).send("pagamento-resultado", assinaturaId.toString(), evento);
  }
}
