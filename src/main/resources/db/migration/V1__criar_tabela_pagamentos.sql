CREATE TABLE pagamentos
(
    id                 UUID           NOT NULL DEFAULT gen_random_uuid(),
    assinatura_id      UUID           NOT NULL,
    usuario_id         UUID           NOT NULL,
    valor              NUMERIC(10, 2) NOT NULL,
    status             VARCHAR(20)    NOT NULL,
    data_processamento TIMESTAMP      NOT NULL DEFAULT now(),
    CONSTRAINT pk_pagamentos PRIMARY KEY (id)
);

CREATE INDEX idx_pagamento_assinatura_id ON pagamentos (assinatura_id);
CREATE INDEX idx_pagamento_status ON pagamentos (status);
