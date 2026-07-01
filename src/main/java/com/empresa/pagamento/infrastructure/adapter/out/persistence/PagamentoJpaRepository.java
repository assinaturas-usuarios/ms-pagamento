package com.empresa.pagamento.infrastructure.adapter.out.persistence;

import com.empresa.pagamento.domain.model.StatusPagamento;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositório JPA para a entidade PagamentoEntidade.
 */
public interface PagamentoJpaRepository extends JpaRepository<PagamentoEntity, UUID> {

  /**
   * Verifica se existe um pagamento associado a uma assinatura específica.
   *
   * @param assinaturaId identificador da assinatura
   * @return true se existir um pagamento associado à assinatura, false caso contrário
   */
  boolean existsByAssinaturaId(UUID assinaturaId);

  /**
   * Verifica se existe um pagamento associado a uma assinatura específica, por status.
   *
   * @param assinaturaId identificador da assinatura
   * @param statusPagamento statusPagamento da assinatura
   * @return true se existir um pagamento associado à assinatura, false caso contrário
   */
  boolean existsByAssinaturaIdAndStatus(UUID assinaturaId, StatusPagamento statusPagamento);
}
