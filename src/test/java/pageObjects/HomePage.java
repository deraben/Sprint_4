package pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HomePage {
    private WebDriver driver;

    // ******************** ЭЛЕМЕНТЫ ГЛАВНОЙ СТРАНИЦЫ ********************

    // Кнопка принятия куки
    // Локатор: по id "rcc-confirm-button"
    private By cookieAcceptButton = By.id("rcc-confirm-button");

    // Кнопка «Заказать» в шапке
    // Локатор: XPath по тексту внутри блока с классом Header_Nav
    private By headerOrderButton = By.xpath("//div[contains(@class,'Header_Nav')]//button[text()='Заказать']");

    // Кнопка «Заказать» внизу страницы (секция «Как это работает»)
    // Локатор: XPath по наличию класса, характерного для этой кнопки
    private By footerOrderButton = By.xpath("//div[contains(@class,'Home_FinishButton')]//button[contains(@class, 'Button_UltraBig')]");

    // Кнопки вопросов в блоке «Вопросы о важном»
    // Каждый вопрос имеет кнопку с id вида "accordion__heading-{индекс}"
    // Соответствующая панель ответа имеет id "accordion__panel-{индекс}"

    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    // Метод для получения кнопки вопроса по индексу
    public WebElement getFAQQuestionButton(int index) {
        return driver.findElement(By.id("accordion__heading-" + index));
    }

    // Метод для получения панели ответа по индексу
    public WebElement getFAQAnswerPanel(int index) {
        return driver.findElement(By.id("accordion__panel-" + index));
    }

    // Метод для принятия куки
    public void acceptCookies() {
        driver.findElement(cookieAcceptButton).click();
    }

    // Метод для клика по кнопке «Заказать» в шапке
    public void clickHeaderOrderButton() {
        driver.findElement(headerOrderButton).click();
    }

    // Метод для клика по кнопке «Заказать» внизу страницы
    public void clickFooterOrderButton() {
        driver.findElement(footerOrderButton).click();
    }
}