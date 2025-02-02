package tests;

import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import pageObjects.HomePage;
import pageObjects.OrderPage;

@RunWith(Parameterized.class)
public class OrderTest {
    private WebDriver driver;
    private HomePage homePage;
    private OrderPage orderPage;

    // Параметры набора данных:
    // orderButtonLocation – точка входа: "header" или "footer"
    // Данные для заполнения первой и второй формы
    private String orderButtonLocation;
    private String firstName;
    private String lastName;
    private String address;
    private String metroStation;
    private String phone;
    private String deliveryDate;
    private String rentalPeriod;
    private String comment;
    private String expectedSuccessMessage;

    public OrderTest(String orderButtonLocation, String firstName, String lastName, String address,
                     String metroStation, String phone, String deliveryDate, String rentalPeriod,
                     String comment, String expectedSuccessMessage) {
        this.orderButtonLocation = orderButtonLocation;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.deliveryDate = deliveryDate;
        this.rentalPeriod = rentalPeriod;
        this.comment = comment;
        this.expectedSuccessMessage = expectedSuccessMessage;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> testData() {
        // Для даты доставки используем текущее число + смещение (формат "dd.MM.yyyy")
        LocalDate date1 = LocalDate.now().plusDays(3);
        LocalDate date2 = LocalDate.now().plusDays(5);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        return Arrays.asList(new Object[][] {
                {"header", "Иван", "Иванов", "ул. Ленина, д. 1", "Лубянка", "9123456789",
                        date1.format(formatter), "сутки", "Комментарий теста 1", "Заказ оформлен"},
                {"footer", "Петр", "Петров", "ул. Пушкина, д. 2", "Пушкинская", "9876543210",
                        date2.format(formatter), "двое суток", "Комментарий теста 2", "Заказ оформлен"}
        });
    }

    @Before
    public void setUp() {
        // Инициализируем ChromeDriver
        System.setProperty("webdriver.chrome.driver", "B:\\Downloads\\chromedriver-win64\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        driver.get("https://qa-scooter.praktikum-services.ru/");

        homePage = new HomePage(driver);
        homePage.acceptCookies();
    }

    @Test
    public void testOrderFlow() {
        // Выбираем точку входа для оформления заказа
        if (orderButtonLocation.equalsIgnoreCase("header")) {
            homePage.clickHeaderOrderButton();
        } else if (orderButtonLocation.equalsIgnoreCase("footer")) {
            homePage.clickFooterOrderButton();
        }

        orderPage = new OrderPage(driver);

        // Заполняем первую форму заказа (личные данные)
        orderPage.fillOrderFormStepOne(firstName, lastName, address, metroStation, phone);
        orderPage.clickNextButton();

        // Заполняем вторую форму заказа (данные по заказу)
        orderPage.fillOrderFormStepTwo(deliveryDate, rentalPeriod, comment);
        orderPage.clickOrderButton();

        // Подтверждаем оформление заказа в модальном окне
        orderPage.confirmOrder();

        // Короткая задержка для появления модального окна с сообщением
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Проверяем, что сообщение об успешном оформлении заказа отображается
        assertTrue("Сообщение об успешном заказе не отображается", orderPage.isOrderSuccessMessageDisplayed());
    }

    @After
    public void tearDown() {
        driver.quit();
    }
}