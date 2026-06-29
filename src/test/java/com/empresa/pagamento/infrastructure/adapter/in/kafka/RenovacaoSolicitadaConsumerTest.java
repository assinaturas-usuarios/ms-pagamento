package com.empresa.pagamento.infrastructure.adapter.in.kafka;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.empresa.pagamento.application.dto.RenovacaoSolicitadaEvent;
import com.empresa.pagamento.domain.port.in.ProcessarPagamentoUseCase;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.support.Acknowledgment;

@ExtendWith(MockitoExtension.class)
class RenovacaoSolicitadaConsumerTest {

  @Mock
  private ProcessarPagamentoUseCase useCase;

  @Mock
  private Acknowledgment ack;

  @InjectMocks
  private RenovacaoSolicitadaConsumer consumer;

  @Test
  void deveProcessarPagamentoEConfirmarAck() {
    RenovacaoSolicitadaEvent evento = new RenovacaoSolicitadaEvent(
        UUID.randomUUID(), UUID.randomUUID(), "BASICO", new BigDecimal("29.90"));
    doNothing().when(useCase).processar(evento);

    consumer.consumir(evento, ack);

    verify(useCase).processar(evento);
    verify(ack).acknowledge();
  }

  @Test
  void devePropagaarExcecaoSemAckQuandoUseCaseFalha() {
    RenovacaoSolicitadaEvent evento = new RenovacaoSolicitadaEvent(
        UUID.randomUUID(), UUID.randomUUID(), "PREMIUM", new BigDecimal("59.90"));
    doThrow(new RuntimeException("erro simulado")).when(useCase).processar(any());

    assertThrows(RuntimeException.class, () -> consumer.consumir(evento, ack));

    verify(ack, never()).acknowledge();
  }
}
