package com.empresa.pagamento.application.usecase;

import com.empresa.pagamento.application.dto.PagamentoResultadoEvent;
import com.empresa.pagamento.application.dto.RenovacaoSolicitadaEvent;
import com.empresa.pagamento.domain.model.Pagamento;
import com.empresa.pagamento.domain.model.ResultadoPagamento;
import com.empresa.pagamento.domain.model.StatusPagamento;
import com.empresa.pagamento.domain.port.in.ProcessarPagamentoUseCase;
import com.empresa.pagamento.domain.port.out.PagamentoRepositoryPort;
import com.empresa.pagamento.domain.port.out.PagamentoResultadoProducerPort;
import com.empresa.pagamento.infrastructure.simulador.SimuladorPagamento;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementação do caso de uso para processar o pagamento de uma renovação solicitada.
 */
@Service
@Slf4j
public class ProcessarPagamentoUseCaseImpl implements ProcessarPagamentoUseCase {

  private final PagamentoRepositoryPort repository;
  private final PagamentoResultadoProducerPort pagamentoResultadoProducer;
  private final SimuladorPagamento simuladorPagamento;
  private final Counter contadorMeterAprovados;
  private final Counter contadorMeterRecusados;

  /**
   * Construtor da classe.
   *
   * @param repository                 porta de repositório de pagamentos
   * @param pagamentoResultadoProducer porta de publicação de resultados de pagamento
   * @param simuladorPagamento         simulador de pagamento
   * @param meterRegistry              registro de métricas
   */
  public ProcessarPagamentoUseCaseImpl(PagamentoRepositoryPort repository,
      PagamentoResultadoProducerPort pagamentoResultadoProducer,
      SimuladorPagamento simuladorPagamento,
      MeterRegistry meterRegistry) {
    this.repository = repository;
    this.pagamentoResultadoProducer = pagamentoResultadoProducer;
    this.simuladorPagamento = simuladorPagamento;
    this.contadorMeterAprovados = Counter.builder("pagamento.processados")
        .tag("status", "APROVADO")
        .description("Total de pagamentos aprovados")
        .register(meterRegistry);
    this.contadorMeterRecusados = Counter.builder("pagamento.processados")
        .tag("status", "RECUSADO")
        .description("Total de pagamentos recusados")
        .register(meterRegistry);
  }

  @Override
  @Transactional
  public void processar(RenovacaoSolicitadaEvent evento) {
    log.info("Processando pagamento: assinaturaId={}, plano={}, valor={}",
        evento.assinaturaId(), evento.plano(), evento.valor());

    if (assinaturaJaFoiPaga(evento)) {
      return;
    }

    StatusPagamento statusPagamento = realizarSimulacaoPagamento(evento);
    Pagamento salvo = repository.salvar(Pagamento
        .novo(evento.assinaturaId(), evento.usuarioId(), evento.valor(), statusPagamento));

    pagamentoResultadoProducer.publicar(
        new PagamentoResultadoEvent(evento.assinaturaId(), salvo.getId(), statusPagamento.name()));
    log.info("Pagamento processado: id={}, statusPagamento={}", salvo.getId(), statusPagamento);
  }

  private boolean assinaturaJaFoiPaga(RenovacaoSolicitadaEvent evento) {
    if (repository.existePagamentoParaAssinatura(evento.assinaturaId())) {
      log.warn("Pagamento ja processado para assinatura: {}", evento.assinaturaId());
      return true;
    }
    return false;
  }

  private StatusPagamento realizarSimulacaoPagamento(RenovacaoSolicitadaEvent evento) {
    ResultadoPagamento resultadoPagamento = simuladorPagamento.simular(evento.assinaturaId(),
        evento.valor());
    StatusPagamento statusPagamento = resolverStatus(resultadoPagamento);
    incrementarContadorMeter(statusPagamento);

    return statusPagamento;
  }

  private StatusPagamento resolverStatus(ResultadoPagamento resultado) {
    return switch (resultado) {
      case ResultadoPagamento.Aprovado _ -> StatusPagamento.APROVADO;
      case ResultadoPagamento.Recusado _ -> StatusPagamento.RECUSADO;
    };
  }

  private void incrementarContadorMeter(StatusPagamento status) {
    if (StatusPagamento.APROVADO.equals(status)) {
      contadorMeterAprovados.increment();
    } else {
      contadorMeterRecusados.increment();
    }
  }
}