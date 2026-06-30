package com.empresa.pagamento.infrastructure.mapper;

import com.empresa.pagamento.domain.model.Pagamento;
import com.empresa.pagamento.domain.model.StatusPagamento;
import com.empresa.pagamento.infrastructure.adapter.out.persistence.PagamentoEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-30T09:29:07-0300",
    comments = "version: 1.6.2, compiler: javac, environment: Java 25.0.2 (Oracle Corporation)"
)
@Component
public class PagamentoMapperImpl implements PagamentoMapper {

    @Override
    public PagamentoEntity toEntity(Pagamento pagamento) {
        if ( pagamento == null ) {
            return null;
        }

        PagamentoEntity.PagamentoEntityBuilder pagamentoEntity = PagamentoEntity.builder();

        pagamentoEntity.id( pagamento.getId() );
        pagamentoEntity.assinaturaId( pagamento.getAssinaturaId() );
        pagamentoEntity.usuarioId( pagamento.getUsuarioId() );
        pagamentoEntity.valor( pagamento.getValor() );
        pagamentoEntity.status( pagamento.getStatus() );
        pagamentoEntity.dataProcessamento( pagamento.getDataProcessamento() );

        return pagamentoEntity.build();
    }

    @Override
    public Pagamento toDomain(PagamentoEntity entidade) {
        if ( entidade == null ) {
            return null;
        }

        UUID id = null;
        UUID assinaturaId = null;
        UUID usuarioId = null;
        BigDecimal valor = null;
        StatusPagamento status = null;
        LocalDateTime dataProcessamento = null;

        id = entidade.getId();
        assinaturaId = entidade.getAssinaturaId();
        usuarioId = entidade.getUsuarioId();
        valor = entidade.getValor();
        status = entidade.getStatus();
        dataProcessamento = entidade.getDataProcessamento();

        Pagamento pagamento = new Pagamento( id, assinaturaId, usuarioId, valor, status, dataProcessamento );

        return pagamento;
    }
}
