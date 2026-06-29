package com.empresa.pagamento.infrastructure.mapper;

import com.empresa.pagamento.domain.model.Pagamento;
import com.empresa.pagamento.infrastructure.adapter.out.persistence.PagamentoEntity;
import org.mapstruct.Mapper;

/**
 * Mapper para converter entre a entidade de domínio Pagamento e a entidade JPA PagamentoEntidade.
 */
@Mapper(componentModel = "spring")
public interface PagamentoMapper {

  /**
   * Converte entidade de domínio para entidade JPA.
   *
   * @param pagamento entidade de domínio
   * @return entidade JPA
   */
  PagamentoEntity toEntity(Pagamento pagamento);

  /**
   * Converte entidade JPA para entidade de domínio.
   *
   * @param entidade entidade JPA
   * @return entidade de domínio
   */
  Pagamento toDomain(PagamentoEntity entidade);
}
