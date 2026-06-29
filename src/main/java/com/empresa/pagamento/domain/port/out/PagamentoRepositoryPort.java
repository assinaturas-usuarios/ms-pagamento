package com.empresa.pagamento.domain.port.out;

import com.empresa.pagamento.domain.model.Pagamento;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface que define o contrato de repositório para a entidade Pagamento. Ela fornece métodos
 * para salvar, buscar e verificar a existência de pagamentos.
 */
public interface PagamentoRepositoryPort {

  /**
   * Persiste um pagamento.
   *
   * @param pagamento entidade de domínio a ser salva
   * @return pagamento persistido
   */
  Pagamento salvar(Pagamento pagamento);

  /**
   * Verifica se já foi registrado um pagamento para a assinatura.
   *
   * @param assinaturaId identificador da assinatura
   * @return true se já existir
   */
  boolean existePagamentoParaAssinatura(UUID assinaturaId);

  /**
   * Busca pagamento pelo identificador.
   *
   * @param id identificador do pagamento
   * @return Optional com o pagamento, se encontrado
   */
  Optional<Pagamento> buscarPorId(UUID id);
}
