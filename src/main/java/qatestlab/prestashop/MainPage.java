package qatestlab.prestashop;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.*;
import org.apache.log4j.Logger;

public class MainPage {

    final static Logger logger = Logger.getLogger(String.valueOf(MainPage.class));

    private By currencyDiv = By.id("_desktop_currency_selector");
    private By currentCurrency = By.className("expand-more");
    private By currencySelector = By.className("currency-selector");
    private By currencyComboBox = By.className("dropdown-menu");

    private By productPrice = By.className("price");

    private final WebDriver driver;

    public MainPage(WebDriver driver) {
        this.driver = driver;
    }

    public String getCurrentCurrency() {
        WebElement element = driver.findElement(currencyDiv).findElement(currencySelector).findElement(currentCurrency);
        logger.info("Получаем текущий тип валюты из выбранного в шапке сайта");
        return element.getText();
    }

    public String getCurrencyType() {
        logger.info("Получаем знак валюты из типа по которому будем сравнивать");
        return getCurrentCurrency().split(" ")[1];
    }

    public List<String> getCurrencyTypeOfProduct() {
        List<WebElement> prices = driver.findElements(productPrice);
        logger.info("Создаем набор элементов с ценами товаров");
        List<String> types = new ArrayList<>();
        logger.info("Создаем список для типов валют у товаров, и заносим в цикле знак в список и возвращаем список знаков");
        for (int i = 0; i < prices.size(); i++) {
            types.add(prices.get(i).getText().split(" ")[1]);
        }
        return types;
    }

    public boolean equalsCurrencyType() {
        boolean equals = true;
        logger.info("Инициализируем переменную для проверки");
        String currencyType = getCurrencyType();
        logger.info("Получаем текущий тип и заносим его в строку, сравниваем знаки валют, если равны то true, если нет то false");
        for (int i = 0; i < getCurrencyTypeOfProduct().size(); i++) {
            equals = (currencyType.equalsIgnoreCase(getCurrencyTypeOfProduct().get(i))) ? true : false;
        }
        logger.info("Возвращаем результат проверки");
        return equals;
    }

    public void changeCurrency() {
        logger.info("Получаем список типов валют");
        List<WebElement> elements = driver.findElement(By.className("col-md-8"))
                .findElement(currencyDiv).findElement(currencySelector)
                .findElement(By.tagName("ul")).findElements(By.tagName("li"));
        logger.info("Нажимаем на выпадающий элемент содеращий список валют");
        driver.findElement(By.className("col-md-8"))
                .findElement(currencyDiv).findElement(currencySelector).click();
        logger.info("Находим элемент для выбора");
        WebElement element = elements.get(2).findElement(By.tagName("a"));
        logger.info("Кликаем на ссылку выбраного элементав");
        new WebDriverWait(driver, 20).until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    public void searchProducts(String productType) {
        logger.info("Находим поле поиска");
        WebElement element = driver.findElement(By.name("s"));
        logger.info("Вводим в поле поиска нужный ключ");
        element.sendKeys(productType);
        logger.info("Производим поиск");
        element.submit();
    }

    public boolean checkCountProducts() {
        logger.info("Получаем целочисленное значение к-ства товаров, находя элемент и парся целое значение из текста");
        int totalCount = Integer.parseInt(driver.findElement(By.className("total-products")).getText().replaceAll("[^0-9]", ""));
        logger.info("Получаем реальное к-ство товаров в каталоге");
        int currentProductSize = driver.findElements(By.className("product-miniature")).size();
        logger.info("Сравниваем значения и возвращаем результат сравнения");
        return totalCount == currentProductSize;
    }

    public void changeSorting() {
        logger.info("Находим выпадающий список для сортировки");
        WebElement sorts = driver.findElement(By.className("sort-by-row")).findElement(By.className("products-sort-order"));
        logger.info("Кликаем на него");
        sorts.click();
        logger.info("И кликаем на элемент в списке найденный по тексту ссылки");
        sorts.findElement(By.linkText("Цене: от высокой к низкой")).click();

    }

    public boolean checkSorting() {
        logger.info("Получаем список цен из товаров в каталоге");
        List<WebElement> elems = driver.findElements(By.className("product-price-and-shipping"));

        ArrayList<Double> types = new ArrayList<>();
        logger.info("Создаем список из вещественных типов");
        logger.info("Заносим в список цены, меня запятую на точку");
        for (int i = 0; i < elems.size(); i++) {
            types.add(Double.parseDouble(elems.get(i).getText().split(" ")[0].replaceAll(",", ".")));
        }

        logger.info("Дублируем список и сортируем новый список");
        ArrayList<Double> sorted = (ArrayList<Double>) types.clone();
        sorted.sort(Collections.reverseOrder());

        logger.info("Возвращаем сравнение двух списков на по убыванию с учетом старых цен без скидок");
        return types.equals(sorted);
    }

    public boolean checkDiscount() {
        logger.info("Создаем переменную для проверки");
        boolean check = true;

        logger.info("Получаем элемент старой цены для первого товара со скидкой");
        WebElement element1For1 = driver.findElement(By.xpath("//*[@id=\"js-product-list\"]/div[1]/article[2]/div/div[1]/div/span[1]"));
        logger.info("Получаем вещественное значение из текста");
        double regularPrice1 = Double.parseDouble(element1For1.getText().replaceAll("[^0-9,]", "").replaceAll(",", "."));
        logger.info("Получаем значение скидки и заносим его в целочисленную переменную");
        WebElement element2For1 = driver.findElement(By.xpath("//*[@id=\"js-product-list\"]/div[1]/article[2]/div/div[1]/div/span[2]"));
        int discount1 = Integer.parseInt(element2For1.getText().replaceAll("[^0-9]", ""));
        logger.info("Получаем значение после скидки и заносим в переменную новой цены");
        WebElement element3For1 = driver.findElement(By.xpath("//*[@id=\"js-product-list\"]/div[1]/article[2]/div/div[1]/div/span[3]"));
        double discountPrice1 = Double.parseDouble(element3For1.getText().replaceAll("[^0-9,]", "").replaceAll(",", "."));

        logger.info("Проверяем значения округляя их до сотых");
        if (!((Math.round((regularPrice1 - discountPrice1) * 100.0) / 100.0) == (Math.round((regularPrice1 * discount1 / 100) * 100.0) / 100.0))) {
            check = false;
        }

        logger.info("Повторяем действия для второго товара со скидкой");
        WebElement element1For2 = driver.findElement(By.xpath("//*[@id=\"js-product-list\"]/div[1]/article[6]/div/div[1]/div/span[1]"));
        double regularPrice2 = Double.parseDouble(element1For2.getText().replaceAll("[^0-9,]", "").replaceAll(",", "."));
        WebElement element2For2 = driver.findElement(By.xpath("//*[@id=\"js-product-list\"]/div[1]/article[6]/div/div[1]/div/span[2]"));
        int discount2 = Integer.parseInt(element2For2.getText().replaceAll("[^0-9]", ""));
        WebElement element3For2 = driver.findElement(By.xpath("//*[@id=\"js-product-list\"]/div[1]/article[6]/div/div[1]/div/span[3]"));
        double discountPrice2 = Double.parseDouble(element3For2.getText().replaceAll("[^0-9,]", "").replaceAll(",", "."));

        if (!((Math.round((regularPrice2 - discountPrice2) * 100.0) / 100.0) == (Math.round((regularPrice2 * discount2 / 100) * 100.0) / 100.0))) {
            check = false;
        }

        logger.info("Возвращаем результат сравнения цен");
        return check;
    }

    public void quit() {
        logger.info("Закрываем драйвер и выходим из сеанса в браузере");
        driver.close();
        driver.quit();
    }

}
