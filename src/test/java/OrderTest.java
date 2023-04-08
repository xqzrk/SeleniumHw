import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.*;


public class OrderTest {

    private WebDriver driver;


    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);

    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void shouldTestV1() {
        driver.get("http://localhost:9999/");
        driver.findElements(By.className("input__control")).get(0).sendKeys("Иванов Иван");
        driver.findElements(By.className("input__control")).get(1).sendKeys("+79998887766");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button__text")).click();
        String expected = "  Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.tagName("p")).getText();
        assertEquals(expected, actual);
    }

    @Test
    void shouldErrorIfEnglishName() {
        driver.get("http://localhost:9999/");
        driver.findElements(By.className("input__control")).get(0).sendKeys("Ivanov Ivan");
        driver.findElements(By.className("input__control")).get(1).sendKeys("+79998887766");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button__text")).click();
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actual = driver.findElements(By.className("input__sub")).get(0).getText();
        assertEquals(expected, actual);
    }

    @Test
    void shouldErrorIfEmptyName() {
        driver.get("http://localhost:9999/");
        driver.findElements(By.className("input__control")).get(0).sendKeys("");
        driver.findElements(By.className("input__control")).get(1).sendKeys("+79998887766");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button__text")).click();
        String expected = "Поле обязательно для заполнения";
        String actual = driver.findElements(By.className("input__sub")).get(0).getText();
        assertEquals(expected, actual);
    }

    @Test
    void shouldTestTelFormat() {
        driver.get("http://localhost:9999/");
        driver.findElements(By.className("input__control")).get(0).sendKeys("Иванов Иван");
        driver.findElements(By.className("input__control")).get(1).sendKeys("89998887766");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button__text")).click();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElements(By.className("input__sub")).get(1).getText();
        assertEquals(expected, actual);
    }

    @Test
    void shouldErrorMsgIfTelIsEmpty() {
        driver.get("http://localhost:9999/");
        driver.findElements(By.className("input__control")).get(0).sendKeys("Иванов Иван");
        driver.findElements(By.className("input__control")).get(1).sendKeys("");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button__text")).click();
        String expected = "Поле обязательно для заполнения";
        String actual = driver.findElements(By.className("input__sub")).get(1).getText();
        assertEquals(expected, actual);
    }

    @Test
    void shouldErrorIfCheckBoxIsEmpty() {
        driver.get("http://localhost:9999/");
        driver.findElements(By.className("input__control")).get(0).sendKeys("Ivanov Ivan");
        driver.findElements(By.className("input__control")).get(1).sendKeys("+79998887766");
        driver.findElement(By.className("button__text")).click();

        Assertions.assertThrows(NoSuchElementException.class, () -> {
            driver.findElement(By.className("order-success"));
        });
    }
}




