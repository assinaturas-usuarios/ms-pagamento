package com.empresa.pagamento.infrastructure.adapter.out.persistence;

import com.empresa.pagamento.domain.model.StatusPagamento;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidade JPA que representa um pagamento no banco de dados.
 */
@Entity
@Table(name = "pagamentos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagamentoEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "assinatura_id", nullable = false)
  private UUID assinaturaId;

  @Column(name = "usuario_id", nullable = false)
  private UUID usuarioId;

  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal valor;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private StatusPagamento status;

  @Column(name = "data_processamento", nullable = false)
  private LocalDateTime dataProcessamento;
}
