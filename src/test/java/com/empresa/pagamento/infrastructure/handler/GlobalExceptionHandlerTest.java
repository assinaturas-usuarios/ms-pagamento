package com.empresa.pagamento.infrastructure.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("GlobalExceptionHandler - ms-pagamento")
class GlobalExceptionHandlerTest {

  private GlobalExceptionHandler handler;

  @Mock
  private MethodArgumentNotValidException validacaoException;
  @Mock
  private BindingResult bindingResult;

  @BeforeEach
  void setUp() {
    handler = new GlobalExceptionHandler();
  }

  @Test
  @DisplayName("422 para MethodArgumentNotValidException com campos")
  void deveRetornar422ParaValidacao() {
    FieldError fieldError = new FieldError("pagamento", "valor", "obrigatorio");
    when(validacaoException.getBindingResult()).thenReturn(bindingResult);
    when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

    ProblemDetail result = handler.handleValidacao(validacaoException);

    assertThat(result.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
  }

  @Test
  @DisplayName("500 para excecao generica")
  void deveRetornar500ParaExcecaoGenerica() {
    ProblemDetail result = handler.handleGenerico(new RuntimeException("erro inesperado"));
    assertThat(result.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
  }
}