package com.empresa.pagamento.infrastructure.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.empresa.pagamento.domain.model.Pagamento;
import com.empresa.pagamento.domain.model.StatusPagamento;
import com.empresa.pagamento.infrastructure.mapper.PagamentoMapper;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("PagamentoPersistenceAdapter")
class PagamentoPersistenceAdapterTest {

  @Mock
  private PagamentoJpaRepository jpaRepository;

  @Mock
  private PagamentoMapper mapper;

  private PagamentoPersistenceAdapter adapter;

  @BeforeEach
  void setUp() {
    adapter = new PagamentoPersistenceAdapter(jpaRepository, mapper);
  }

  @Test
  @DisplayName("Deve salvar pagamento no repositório")
  void deveSalvarPagamento() {
    UUID id = UUID.randomUUID();
    Pagamento pagamento = new Pagamento(null, UUID.randomUUID(), UUID.randomUUID(),
        new BigDecimal("99.99"), StatusPagamento.APROVADO, LocalDateTime.now());

    PagamentoEntity entity = PagamentoEntity.builder()
        .id(id)
        .assinaturaId(pagamento.getAssinaturaId())
        .usuarioId(pagamento.getUsuarioId())
        .valor(pagamento.getValor())
        .status(pagamento.getStatus())
        .dataProcessamento(pagamento.getDataProcessamento())
        .build();

    when(mapper.toEntity(pagamento)).thenReturn(entity);
    when(jpaRepository.save(entity)).thenReturn(entity);
    when(mapper.toDomain(entity)).thenReturn(new Pagamento(id, pagamento.getAssinaturaId(),
        pagamento.getUsuarioId(), pagamento.getValor(), pagamento.getStatus(), pagamento.getDataProcessamento()));

    Pagamento resultado = adapter.salvar(pagamento);

    assertThat(resultado.getId()).isEqualTo(id);
    verify(mapper).toEntity(pagamento);
    verify(jpaRepository).save(entity);
  }

  @Test
  @DisplayName("Deve verificar existência de pagamento para assinatura")
  void deveVerificarExistencia() {
    UUID assinaturaId = UUID.randomUUID();
    when(jpaRepository.existsByAssinaturaIdAndStatus(assinaturaId, StatusPagamento.APROVADO)).thenReturn(true);

    boolean existe = adapter.existePagamentoParaAssinatura(assinaturaId);

    assertThat(existe).isTrue();
    verify(jpaRepository).existsByAssinaturaIdAndStatus(assinaturaId, StatusPagamento.APROVADO);
  }

  @Test
  @DisplayName("Deve buscar pagamento por ID")
  void deveBuscarPorId() {
    UUID id = UUID.randomUUID();
    PagamentoEntity entity = PagamentoEntity.builder()
        .id(id)
        .assinaturaId(UUID.randomUUID())
        .usuarioId(UUID.randomUUID())
        .valor(new BigDecimal("49.99"))
        .status(StatusPagamento.APROVADO)
        .dataProcessamento(LocalDateTime.now())
        .build();

    Pagamento pagamento = new Pagamento(entity.getId(), entity.getAssinaturaId(),
        entity.getUsuarioId(), entity.getValor(), entity.getStatus(), entity.getDataProcessamento());

    when(jpaRepository.findById(id)).thenReturn(Optional.of(entity));
    when(mapper.toDomain(entity)).thenReturn(pagamento);

    Optional<Pagamento> resultado = adapter.buscarPorId(id);

    assertThat(resultado).isPresent();
    assertThat(resultado.get().getId()).isEqualTo(id);
    verify(jpaRepository).findById(id);
  }
}
