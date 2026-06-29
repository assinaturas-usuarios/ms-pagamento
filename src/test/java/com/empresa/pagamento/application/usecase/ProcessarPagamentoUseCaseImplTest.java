package com.empresa.pagamento.application.usecase;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.empresa.pagamento.application.dto.PagamentoResultadoEvent;
import com.empresa.pagamento.application.dto.RenovacaoSolicitadaEvent;
import com.empresa.pagamento.domain.model.Pagamento;
import com.empresa.pagamento.domain.model.ResultadoPagamento;
import com.empresa.pagamento.domain.model.StatusPagamento;
import com.empresa.pagamento.domain.port.out.PagamentoRepositoryPort;
import com.empresa.pagamento.domain.port.out.PagamentoResultadoProducerPort;
import com.empresa.pagamento.infrastructure.simulador.SimuladorPagamento;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProcessarPagamentoUseCaseImplTest {

  @Mock
  private PagamentoRepositoryPort repository;
  @Mock
  private PagamentoResultadoProducerPort pagamentoResultadoProducer;
  @Mock
  private SimuladorPagamento simuladorPagamento;

  private ProcessarPagamentoUseCaseImpl useCase;

  private UUID assinaturaId;
  private UUID usuarioId;
  private RenovacaoSolicitadaEvent evento;
  private Pagamento pagamento;

  @BeforeEach
  void setUp() {
    useCase = new ProcessarPagamentoUseCaseImpl(repository, pagamentoResultadoProducer,
        simuladorPagamento,
        new SimpleMeterRegistry());
    assinaturaId = UUID.randomUUID();
    usuarioId = UUID.randomUUID();
    evento = new RenovacaoSolicitadaEvent(assinaturaId, usuarioId, "PREMIUM",
        new BigDecimal("39.90"));
    pagamento = Pagamento.novo(assinaturaId, usuarioId, new BigDecimal("39.90"),
        StatusPagamento.APROVADO);
  }

  @Nested
  @DisplayName("Processamento de pagamento")
  class ProcessamentoPagamento {

    @Test
    @DisplayName("Deve processar pagamento aprovado e publicar resultado")
    void deveProcessarPagamentoAprovado() {
      given(repository.existePagamentoParaAssinatura(assinaturaId)).willReturn(false);
      given(simuladorPagamento.simular(any(), any())).willReturn(
          new ResultadoPagamento.Aprovado(UUID.randomUUID()));
      given(repository.salvar(any())).willReturn(pagamento);

      useCase.processar(evento);

      verify(pagamentoResultadoProducer).publicar(any(PagamentoResultadoEvent.class));
    }

    @Test
    @DisplayName("Deve ignorar evento quando pagamento ja foi processado")
    void deveIgnorarEventoDuplicado() {
      given(repository.existePagamentoParaAssinatura(assinaturaId)).willReturn(true);

      useCase.processar(evento);

      verify(simuladorPagamento, never()).simular(any(), any());
      verify(pagamentoResultadoProducer, never()).publicar(any());
    }

    @Test
    @DisplayName("Deve processar pagamento recusado e publicar resultado negativo")
    void deveProcessarPagamentoRecusado() {
      given(repository.existePagamentoParaAssinatura(assinaturaId)).willReturn(false);
      given(simuladorPagamento.simular(any(), any())).willReturn(
          new ResultadoPagamento.Recusado(UUID.randomUUID(), "Saldo insuficiente")
      );
      Pagamento pagamentoRecusado = Pagamento.novo(assinaturaId, usuarioId, new BigDecimal("39.90"),
          StatusPagamento.RECUSADO);
      given(repository.salvar(any())).willReturn(pagamentoRecusado);

      useCase.processar(evento);

      verify(pagamentoResultadoProducer).publicar(argThat(e -> "RECUSADO".equals(e.status())));
    }
  }
}