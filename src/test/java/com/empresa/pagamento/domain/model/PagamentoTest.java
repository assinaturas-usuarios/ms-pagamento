package com.empresa.pagamento.domain.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Pagamento - modelo de dominio")
class PagamentoTest {

  @Test
  @DisplayName("Deve criar pagamento aprovado via fabrica e foiAprovado retorna true")
  void deveCriarPagamentoAprovado() {
    UUID assinaturaId = UUID.randomUUID();
    UUID usuarioId = UUID.randomUUID();
    BigDecimal valor = new BigDecimal("39.90");

    Pagamento pagamento = Pagamento.novo(assinaturaId, usuarioId, valor, StatusPagamento.APROVADO);

    assertThat(pagamento.getId()).isNotNull();
    assertThat(pagamento.getAssinaturaId()).isEqualTo(assinaturaId);
    assertThat(pagamento.getUsuarioId()).isEqualTo(usuarioId);
    assertThat(pagamento.getValor()).isEqualByComparingTo(valor);
    assertThat(pagamento.getStatus()).isEqualTo(StatusPagamento.APROVADO);
    assertThat(pagamento.getDataProcessamento()).isNotNull();
    assertThat(pagamento.foiAprovado()).isTrue();
  }

  @Test
  @DisplayName("foiAprovado retorna false para pagamento recusado")
  void foiAprovadoRetornaFalseParaRecusado() {
    Pagamento pagamento = Pagamento.novo(UUID.randomUUID(), UUID.randomUUID(),
        new BigDecimal("29.90"), StatusPagamento.RECUSADO);

    assertThat(pagamento.foiAprovado()).isFalse();
  }
}