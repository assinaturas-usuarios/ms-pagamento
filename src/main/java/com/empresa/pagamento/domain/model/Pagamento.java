package com.empresa.pagamento.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Classe que representa um pagamento realizado por um usuário.
 */
public class Pagamento {

  private UUID id;
  private UUID assinaturaId;
  private UUID usuarioId;
  private BigDecimal valor;
  private StatusPagamento status;
  private LocalDateTime dataProcessamento;

  /**
   * Construtor da classe Pagamento.
   *
   * @param id                identificador único do pagamento
   * @param assinaturaId      identificador da assinatura associada ao pagamento
   * @param usuarioId         identificador do usuário que realizou o pagamento
   * @param valor             valor do pagamento
   * @param status            status do pagamento (ex: "APROVADO", "REJEITADO")
   * @param dataProcessamento data e hora em que o pagamento foi processado
   */
  public Pagamento(UUID id, UUID assinaturaId, UUID usuarioId, BigDecimal valor,
      StatusPagamento status, LocalDateTime dataProcessamento) {
    this.id = id;
    this.assinaturaId = assinaturaId;
    this.usuarioId = usuarioId;
    this.valor = valor;
    this.status = status;
    this.dataProcessamento = dataProcessamento;
  }

  /**
   * Cria um novo pagamento para uma assinatura específica.
   *
   * @param assinaturaId identificador da assinatura associada ao pagamento
   * @param usuarioId    identificador do usuário que está realizando o pagamento
   * @param valor        valor do pagamento
   * @param status       status inicial do pagamento (ex: "PENDENTE")
   * @return uma nova instância de Pagamento
   */
  public static Pagamento novo(UUID assinaturaId, UUID usuarioId, BigDecimal valor,
      StatusPagamento status) {
    return new Pagamento(null, assinaturaId, usuarioId, valor, status,
        LocalDateTime.now());
  }

  /**
   * Verifica se o pagamento foi aprovado.
   *
   * @return true se o pagamento foi aprovado, false caso contrário
   */
  public boolean foiAprovado() {
    return StatusPagamento.APROVADO.equals(this.status);
  }

  public UUID getId() {
    return id;
  }

  public UUID getAssinaturaId() {
    return assinaturaId;
  }

  public UUID getUsuarioId() {
    return usuarioId;
  }

  public BigDecimal getValor() {
    return valor;
  }

  public StatusPagamento getStatus() {
    return status;
  }

  public LocalDateTime getDataProcessamento() {
    return dataProcessamento;
  }
}
