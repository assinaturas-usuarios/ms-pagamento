package com.empresa.pagamento.infrastructure.adapter.out.persistence;

import com.empresa.pagamento.domain.model.Pagamento;
import com.empresa.pagamento.domain.port.out.PagamentoRepositoryPort;
import com.empresa.pagamento.infrastructure.mapper.PagamentoMapper;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Adaptador de persistência que implementa o contrato de repositório para a entidade Pagamento. Ele
 * utiliza o PagamentoJpaRepository para interagir com o banco de dados e o PagamentoMapper para
 * converter entre a entidade de domínio e a entidade JPA.
 */
@Component
@RequiredArgsConstructor
public class PagamentoPersistenceAdapter implements PagamentoRepositoryPort {

  private final PagamentoJpaRepository jpaRepository;
  private final PagamentoMapper mapper;

  @Override
  public Pagamento salvar(Pagamento pagamento) {
    PagamentoEntity entidade = mapper.toEntity(pagamento);
    PagamentoEntity salvo = jpaRepository.save(entidade);
    return mapper.toDomain(salvo);
  }

  @Override
  public boolean existePagamentoParaAssinatura(UUID assinaturaId) {
    return jpaRepository.existsByAssinaturaId(assinaturaId);
  }

  @Override
  public Optional<Pagamento> buscarPorId(UUID id) {
    return jpaRepository.findById(id).map(mapper::toDomain);
  }
}
