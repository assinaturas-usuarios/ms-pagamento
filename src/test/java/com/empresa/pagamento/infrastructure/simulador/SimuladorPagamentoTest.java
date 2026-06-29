package com.empresa.pagamento.infrastructure.simulador;

import static org.assertj.core.api.Assertions.assertThat;

import com.empresa.pagamento.domain.model.ResultadoPagamento;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

@DisplayName("SimuladorPagamento")
class SimuladorPagamentoTest {

  private SimuladorPagamento simulador;

  @BeforeEach
  void setUp() {
    simulador = new SimuladorPagamento();
  }

  @Test
  @DisplayName("Deve retornar Aprovado quando taxa = 1.0 (sempre aprova)")
  void deveRetornarAprovadoComTaxaMaxima() {
    ReflectionTestUtils.setField(simulador, "taxaAprovacao", 1.0);
    ResultadoPagamento resultado = simulador.simular(UUID.randomUUID(), new BigDecimal("39.90"));
    assertThat(resultado).isInstanceOf(ResultadoPagamento.Aprovado.class);
    assertThat(resultado.pagamentoId()).isNotNull();
  }

  @Test
  @DisplayName("Deve retornar Recusado quando taxa = -1.0 (nunca aprova)")
  void deveRetornarRecusadoComTaxaZero() {
    ReflectionTestUtils.setField(simulador, "taxaAprovacao", -1.0);
    ResultadoPagamento resultado = simulador.simular(UUID.randomUUID(), new BigDecimal("39.90"));
    assertThat(resultado).isInstanceOf(ResultadoPagamento.Recusado.class);
    ResultadoPagamento.Recusado recusado = (ResultadoPagamento.Recusado) resultado;
    assertThat(recusado.motivo()).isNotBlank();
  }
}