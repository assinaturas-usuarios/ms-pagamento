package com.empresa.pagamento;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principal da aplicação de pagamento.
 */
@SpringBootApplication
public class PagamentoApplication {

  /**
   * Ponto de entrada da aplicação.
   *
   * @param args argumentos de linha de comando
   */
  static void main(String[] args) {
    SpringApplication.run(PagamentoApplication.class, args);
  }
}
