package FindJob;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

import static io.github.bonigarcia.wdm.WebDriverManager.chromedriver;

public class FindJob {
    private static final String DIV = "div";
    private static final By MULTICOMPLETE_LISTBOX = By.className("MuiAutocomplete-listbox");
    private static final By LOCATION_XPATH = By.xpath("//*[contains(text(), 'LOCATION')]");
    private static final By SEARCH_INPUT = By.xpath("//input[@placeholder = 'Search']");
    private static final By SEARCH_INPUT_EXPANDED = By.xpath("//input[@placeholder = 'Skill, location, company']");
    WebDriver driver;
    WebDriverWait wait;

    private WebDriverWait getWait() {
        return new WebDriverWait(driver, 15);
    }

    private By byTag(String tagName) {
        return By.tagName(tagName);
    }

    private void type(String text, By locator) {
        driver.findElement(locator).sendKeys(text);
    }

    private void click(By locator) {
        getWait().until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    private void click(WebElement element) {
        getWait().until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    private void waitUntilUrlContains(String phrase) {
        getWait().until(ExpectedConditions.urlContains(phrase));
    }

    private String getHref(WebElement element) {
        return element.getAttribute("href");
    }

    private String getText(WebElement element) {
        return element.getText();
    }

    private void waitForXSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Before
    public void turnOn() {
        chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Given("Go to JustJoinIT website")
    public void goToJustJoinITWebsite() {
        driver.get("https://justjoin.it/");
    }

    @Then("Select location, job and seniority")
    public void selectLocationJobAndSeniority() {
        click(By.xpath("//span[@class='MuiButton-label' and contains(text(),\"Location\")]"));
        click(By.xpath("//span[@class='MuiButton-label' and contains(text(),\"Wrocław\")]"));
        click(By.xpath("//span[@class='css-hx5b41' and contains(text(),\"Testing\")]"));
        click(By.xpath("//span[@class='MuiButton-label' and contains(text(),\"More filters\")]"));
        click(By.xpath("//span[@class='MuiButton-label' and contains(text(),\"Junior\")]"));
        click(By.xpath("//span[@class='MuiButton-label' and contains(text(),\"Show offers\")]"));
    }

    @Then("Select seniority")
    public void selectLocationAndSeniority() {
        click(By.xpath("//span[@class='MuiButton-label' and contains(text(),\"More filters\")]"));
        click(By.xpath("//span[@class='MuiButton-label' and contains(text(),\"Junior\")]"));
        click(By.xpath("//span[@class='MuiButton-label' and contains(text(),\"Show offers\")]"));
    }

    @And("Select offers with sallary and sort by lowest")
    public void selectOffersWithSallaryAndSortByLowest() {
        click(By.xpath("//span[@class='css-15vjopg' and contains(text(),\"Latest\")]"));
        click(By.xpath("//span[@class='MuiButton-label' and contains(text(),\"Lowest salary\")]"));
    }

    @After
    public void turnDown() {
        driver.quit();
    }

    @And("I search for offers in {string} city")
    public void iSearchForOffersInCity(String cityName) {
        click(SEARCH_INPUT);
        type(cityName, SEARCH_INPUT_EXPANDED);
        waitForXSeconds(1);
        click(driver.findElement(MULTICOMPLETE_LISTBOX).findElement(LOCATION_XPATH));
    }

    @And("I search for offers in {string} category")
    public void iSearchForOffersInCategory(String categoryName) {
        type(categoryName, SEARCH_INPUT_EXPANDED);
        waitForXSeconds(1);
        click(driver.findElement(MULTICOMPLETE_LISTBOX).findElements(byTag("li")).get(0));
        waitUntilUrlContains(categoryName);
    }

    @Then("I print out general information about offers in city {string}")
    public void iPrintOutGeneralInformationAboutTheOffer(String cityName) {
        waitForXSeconds(2);
        List<WebElement> listaOfert = driver.findElements(By.xpath("//div[contains(text(), 'd ago') or contains(text(), 'New')]/preceding-sibling::div/../../../../.."));
        listaOfert.forEach(oferta -> {
            if (oferta.getText().contains(cityName)) {

                List<WebElement> divs = oferta.findElements(byTag(DIV));
                System.out.println("Offer name       : " + getText(divs.get(6)));
                System.out.println("Company name     : " + getText(divs.get(14)));
                System.out.println("Salary           : " + getText(divs.get(10)));
                System.out.println("Link to the offer: " + getHref(oferta));
                System.out.println("____________________________________________________________________________");
            }
        });
    }
}