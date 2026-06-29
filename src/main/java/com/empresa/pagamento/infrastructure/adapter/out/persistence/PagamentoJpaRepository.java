package com.empresa.pagamento.infrastructure.adapter.out.persistence;

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
}
