package pageObjects;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class OrderPage {
    private WebDriver driver;

    // ******************** ПЕРВАЯ ФОРМА (ЛИЧНЫЕ ДАННЫЕ) ********************

    // Поле "Имя"
    // Локатор: XPath по placeholder "* Имя"
    private By firstNameField = By.xpath("//input[@placeholder='* Имя']");

    // Поле "Фамилия"
    // Локатор: XPath по placeholder "* Фамилия"
    private By lastNameField = By.xpath("//input[@placeholder='* Фамилия']");

    // Поле "Адрес: куда привезти заказ"
    // Локатор: XPath по placeholder "* Адрес: куда привезти заказ"
    private By addressField = By.xpath("//input[@placeholder='* Адрес: куда привезти заказ']");

    // Выпадающий список "Станция метро"
    // Локатор поля ввода внутри выпадающего списка.
    private By metroStationInput = By.xpath("//input[@placeholder='* Станция метро']");
    // Локатор для варианта выбора. Здесь используется форматирование строки, чтобы подставить нужное название станции.
    private String metroStationOptionXpath = "//div[contains(@class, 'select-search__option') and normalize-space(text())='%s']";

    // Поле "Телефон: на него позвонит курьер"
    // Локатор: XPath по placeholder "* Телефон: на него позвонит курьер"
    private By phoneField = By.xpath("//input[@placeholder='* Телефон: на него позвонит курьер']");

    // Кнопка "Далее"
    // Локатор: XPath по тексту кнопки "Далее"
    private By nextButton = By.xpath("//button[text()='Далее']");


    // ******************** ВТОРАЯ ФОРМА (ДАННЫЕ ЗАКАЗА) ********************

    // Поле "Когда привезти самокат"
    // Локатор: XPath по placeholder "* Когда привезти самокат"
    private By deliveryDateField = By.xpath("//input[@placeholder='* Когда привезти самокат']");

    // Выпадающий список "Срок аренды"
    // Локатор: здесь указан пример – возможно, в реальном проекте потребуется другой локатор
    private By rentalPeriodDropdown = By.className("Dropdown-control");

    // Поле "Комментарий для курьера"
    // Локатор: XPath по placeholder "Комментарий для курьера"
    private By commentField = By.xpath("//input[@placeholder='Комментарий для курьера']");

    // Кнопка "Заказать" (на второй форме)
    // Локатор: XPath по тексту кнопки "Заказать"
    private By orderButton = By.xpath("//button[text()='Заказать']");

    // Кнопка подтверждения заказа в модальном окне ("Да")
    // Локатор: XPath по тексту "Да"
    private By confirmYesButton = By.xpath("//button[text()='Да']");

    public OrderPage(WebDriver driver) {
        this.driver = driver;
    }

    // Метод для заполнения первой формы (личные данные)
    public void fillOrderFormStepOne(String firstName, String lastName, String address, String metroStation, String phone) {
        driver.findElement(firstNameField).sendKeys(firstName);
        driver.findElement(lastNameField).sendKeys(lastName);
        driver.findElement(addressField).sendKeys(address);

        // Для поля "Станция метро":
        WebElement metroInput = driver.findElement(metroStationInput);
        metroInput.click(); // открываем выпадающий список
        metroInput.sendKeys(metroStation);

        // Явное ожидание появления нужного варианта в списке
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        By metroOptionLocator = By.xpath(String.format(metroStationOptionXpath, metroStation));
        WebElement metroOption = wait.until(ExpectedConditions.elementToBeClickable(metroOptionLocator));

        // Пробуем обычный клик, если он не срабатывает – используем JavaScript
        try {
            metroOption.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", metroOption);
        }

        driver.findElement(phoneField).sendKeys(phone);
    }

    public void clickNextButton() {
        driver.findElement(nextButton).click();
    }

    // Метод для заполнения второй формы (данные заказа)
    public void fillOrderFormStepTwo(String deliveryDate, String rentalPeriod, String comment) {
        driver.findElement(deliveryDateField).sendKeys(deliveryDate);

        // Выбор срока аренды из выпадающего списка:
        driver.findElement(rentalPeriodDropdown).click();
        // Локатор опции выбирается по тексту равному rentalPeriod
        By rentalPeriodOption = By.xpath("//div[@class='Dropdown-menu']/div[text()='" + rentalPeriod + "']");
        driver.findElement(rentalPeriodOption).click();

        driver.findElement(commentField).sendKeys(comment);
    }

    public void clickOrderButton() {
        driver.findElement(orderButton).click();
    }

    public void confirmOrder() {
        driver.findElement(confirmYesButton).click();
    }

    // Метод для проверки отображения сообщения об успешном оформлении заказа.
    // Локатор – ищем элемент, содержащий текст "Заказ оформлен"
    public boolean isOrderSuccessMessageDisplayed() {
        try {
            WebElement successMessage = driver.findElement(By.xpath("//*[contains(text(),'Заказ оформлен')]"));
            return successMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}