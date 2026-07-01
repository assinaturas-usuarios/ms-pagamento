package com.empresa.pagamento.domain.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Pagamento - modelo de dominio")
class PagamentoTest {

  @Nested
  @DisplayName("novo")
  class NovoFactory {

    @Test
    @DisplayName("Deve criar pagamento novo com status APROVADO")
    void deveCriarPagamentoNovo() {
      UUID assinaturaId = UUID.randomUUID();
      UUID usuarioId = UUID.randomUUID();
      BigDecimal valor = new BigDecimal("39.90");

      Pagamento pagamento = Pagamento.novo(assinaturaId, usuarioId, valor, StatusPagamento.APROVADO);

      assertThat(pagamento.getId()).isNull();
      assertThat(pagamento.getAssinaturaId()).isEqualTo(assinaturaId);
      assertThat(pagamento.getUsuarioId()).isEqualTo(usuarioId);
      assertThat(pagamento.getValor()).isEqualByComparingTo(valor);
      assertThat(pagamento.getStatus()).isEqualTo(StatusPagamento.APROVADO);
      assertThat(pagamento.getDataProcessamento()).isNotNull();
    }
  }

  @Nested
  @DisplayName("foiAprovado")
  class FoiAprovado {

    @Test
    @DisplayName("Deve retornar true para pagamento aprovado")
    void deveCriarPagamentoAprovado() {
      UUID assinaturaId = UUID.randomUUID();
      UUID usuarioId = UUID.randomUUID();
      BigDecimal valor = new BigDecimal("39.90");

      Pagamento pagamento = Pagamento.novo(assinaturaId, usuarioId, valor, StatusPagamento.APROVADO);

      assertThat(pagamento.getId()).isNull();
      assertThat(pagamento.getAssinaturaId()).isEqualTo(assinaturaId);
      assertThat(pagamento.getUsuarioId()).isEqualTo(usuarioId);
      assertThat(pagamento.getValor()).isEqualByComparingTo(valor);
      assertThat(pagamento.getStatus()).isEqualTo(StatusPagamento.APROVADO);
      assertThat(pagamento.getDataProcessamento()).isNotNull();
      assertThat(pagamento.foiAprovado()).isTrue();
    }

    @Test
    @DisplayName("Deve retornar false para pagamento recusado")
    void foiAprovadoRetornaFalseParaRecusado() {
      Pagamento pagamento = Pagamento.novo(UUID.randomUUID(), UUID.randomUUID(),
          new BigDecimal("29.90"), StatusPagamento.RECUSADO);

      assertThat(pagamento.foiAprovado()).isFalse();
    }
  }

  @Nested
  @DisplayName("Construtor")
  class ConstructorTests {

    @Test
    @DisplayName("Deve criar pagamento com todos os parâmetros")
    void deveCriarPagamentoComTodosParametros() {
      UUID id = UUID.randomUUID();
      UUID assinaturaId = UUID.randomUUID();
      UUID usuarioId = UUID.randomUUID();
      BigDecimal valor = new BigDecimal("99.99");
      LocalDateTime data = LocalDateTime.of(2026, 1, 15, 10, 30, 0);

      Pagamento pagamento = new Pagamento(id, assinaturaId, usuarioId, valor, StatusPagamento.APROVADO, data);

      assertThat(pagamento.getId()).isEqualTo(id);
      assertThat(pagamento.getAssinaturaId()).isEqualTo(assinaturaId);
      assertThat(pagamento.getUsuarioId()).isEqualTo(usuarioId);
      assertThat(pagamento.getValor()).isEqualByComparingTo(valor);
      assertThat(pagamento.getStatus()).isEqualTo(StatusPagamento.APROVADO);
      assertThat(pagamento.getDataProcessamento()).isEqualTo(data);
    }

    @Test
    @DisplayName("Deve criar pagamento com status RECUSADO")
    void deveCriarPagamentoRecusado() {
      UUID id = UUID.randomUUID();
      UUID assinaturaId = UUID.randomUUID();
      UUID usuarioId = UUID.randomUUID();

      Pagamento pagamento = new Pagamento(id, assinaturaId, usuarioId, 
          new BigDecimal("49.99"), StatusPagamento.RECUSADO, LocalDateTime.now());

      assertThat(pagamento.getStatus()).isEqualTo(StatusPagamento.RECUSADO);
      assertThat(pagamento.foiAprovado()).isFalse();
    }
  }

  @Nested
  @DisplayName("Getters")
  class Getters {

    @Test
    @DisplayName("Deve retornar todos os valores através dos getters")
    void deveRetornarValores() {
      UUID id = UUID.randomUUID();
      UUID assinaturaId = UUID.randomUUID();
      UUID usuarioId = UUID.randomUUID();
      BigDecimal valor = new BigDecimal("149.99");
      LocalDateTime data = LocalDateTime.of(2026, 2, 20, 15, 45, 30);

      Pagamento pagamento = new Pagamento(id, assinaturaId, usuarioId, valor, StatusPagamento.APROVADO, data);

      assertThat(pagamento.getId()).isEqualTo(id);
      assertThat(pagamento.getAssinaturaId()).isEqualTo(assinaturaId);
      assertThat(pagamento.getUsuarioId()).isEqualTo(usuarioId);
      assertThat(pagamento.getValor()).isEqualByComparingTo(valor);
      assertThat(pagamento.getStatus()).isEqualTo(StatusPagamento.APROVADO);
      assertThat(pagamento.getDataProcessamento()).isEqualTo(data);
    }
  }
}