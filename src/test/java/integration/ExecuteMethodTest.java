package integration;

import com.codeborne.selenide.Command;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ex.ElementNotFound;
import com.codeborne.selenide.impl.WebElementSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

final class ExecuteMethodTest extends ITest {

  @BeforeEach
  void setUp() {
    openFile("page_with_selects_without_jquery.html");
  }

  @Test
  void userCanExecuteCustomCommand() {
    $("#username").scrollTo()
      .setValue("value")
      .execute(new CustomSetValueCommand("custom value"))
      .pressEnter()
      .execute(new TripleClick());
    assertThat($("#username-mirror").text())
      .startsWith("custom value");
  }

  @Test
  void userCanExecuteCustomCommandWithGivenTimeout() {
    setTimeout(1);

    $("#dynamic-content2")
      .execute(new TripleClick(), Duration.ofSeconds(3));
  }

  @Test
  void executeMethodWithGivenTimeoutThrowsErrorAfterTimeout() {
    setTimeout(1);

    long timeout = 1100;
    long startMs = System.currentTimeMillis();
    assertThatCode(() -> $("#non-existing-element").execute(new TripleClick(), Duration.ofMillis(timeout)))
      .isInstanceOf(ElementNotFound.class)
      .hasMessageContaining("Timeout: 1.100 s.");
    long elapsedTimeMs = System.currentTimeMillis() - startMs;
    assertThat(elapsedTimeMs).isBetween(timeout, timeout * 2);
  }

  @ParametersAreNonnullByDefault
  private static final class TripleClick implements Command<SelenideElement> {
    @Override
    @Nonnull
    public SelenideElement execute(SelenideElement proxy, WebElementSource locator, @Nullable Object[] args) {
      WebElement element = locator.findAndAssertElementIsInteractable();
      element.click();
      element.click();
      element.click();
      return proxy;
    }
  }

  @ParametersAreNonnullByDefault
  private static final class CustomSetValueCommand implements Command<SelenideElement> {
    private final String value;

    CustomSetValueCommand(String value) {
      this.value = value;
    }

    @Override
    @Nonnull
    public SelenideElement execute(SelenideElement proxy, WebElementSource locator, @Nullable Object[] args) {
      proxy.clear();
      proxy.sendKeys(value);
      return proxy;
    }
  }
}