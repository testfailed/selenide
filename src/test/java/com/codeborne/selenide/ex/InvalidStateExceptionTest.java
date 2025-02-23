package com.codeborne.selenide.ex;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.StaleElementReferenceException;

import static org.assertj.core.api.Assertions.assertThat;

final class InvalidStateExceptionTest {
  @Test
  void constructorWithCause() {
    StaleElementReferenceException cause = new StaleElementReferenceException("Houston, we have a problem");
    InvalidStateException invalidStateException = new InvalidStateException("#link", cause);

    assertThat(invalidStateException).hasMessageStartingWith("Invalid element state");
    assertThat(invalidStateException).hasMessageEndingWith("StaleElementReferenceException: Houston, we have a problem");
    assertThat(invalidStateException).hasToString(String.format("Invalid element state [#link]: " +
      "Houston, we have a problem%n" +
      "Timeout: 0 ms.%n" +
      "Caused by: StaleElementReferenceException: Houston, we have a problem"));
  }

  @Test
  void constructorWithMessage() {
    InvalidStateException invalidStateException = new InvalidStateException("#link", "Houston, we have a problem");

    assertThat(invalidStateException).hasMessageStartingWith("Invalid element state [#link]: Houston, we have a problem");
    assertThat(invalidStateException).hasToString(String.format("Invalid element state [#link]: Houston, we have a problem%n" +
      "Timeout: 0 ms."));
  }
}
