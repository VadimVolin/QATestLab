package qatestlab;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import qatestlab.prestashop.MainPage;

import java.util.concurrent.TimeUnit;


public class Main {

    final static Logger logger = Logger.getLogger(String.valueOf(MainPage.class));


    public static void main(String[] args) throws InterruptedException {
        logger.info("Пролаживаем путь к драйверу хром-браузера");
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\vadim\\Downloads\\chromedriver_win32\\chromedriver.exe");
        logger.info("Создаем драйвер");
        WebDriver webDriver = new ChromeDriver();
        logger.info("Ставим время ожидания прогрузки страницы");
        webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        logger.info("Переходим на страницу");
        webDriver.get("http://prestashop-automation.qatestlab.com.ua/ru/");
        logger.info("Ставим поток в сон на 1сек.");
        Thread.sleep(1000);


        logger.info("Создаем элемент для тестирования страницы");
        MainPage testPage = new MainPage(webDriver);
        try {

        logger.info("Вызываем проверку на тип валют и ставим тип в доллар и выводим в консоль");
            System.out.println("Типы валют равны - " + testPage.equalsCurrencyType());
            testPage.changeCurrency();

        logger.info("Находим товар по ключ. слову \"dress\" и проверяем типы валют у товаров на соответсвие доллару и выводим в консоль");
            testPage.searchProducts("dress");
            System.out.println("Типы валют равны - " + testPage.equalsCurrencyType());

        logger.info("Проверка к-ства товаров и выводим в консоль");
            System.out.println("Равно к-ство найденных товаров и заявленое - " + testPage.checkCountProducts());

        logger.info("Делаем сортировку товаров");
            testPage.changeSorting();

        logger.info("Ставим поток в сон на 2 сек.");
            Thread.sleep(2000);

        logger.info("Проверяем сортировку и выводим в консоль");
            System.out.println("Правильная сортировка - " + testPage.checkSorting());

        logger.info("Проверяем скидку и выводим в консоль");
            System.out.println("Правильная скидка - " + testPage.checkDiscount());

        } catch (Exception e) {
        logger.info("Получили исключение");
            throw new RuntimeException(e);
        } finally {
        logger.info("Завершаем работу");
            testPage.quit();
        }

    }

}
