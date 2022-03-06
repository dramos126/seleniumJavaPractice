package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import utils.WaitUtil;

public class BaseTest {
    WaitUtil waitUtil = new WaitUtil();

    WebDriver driver;
    static final String chromedriverPath = PracticeTests.class.getClassLoader().getResource("chromedriver.exe").getPath();

    @BeforeTest
    void setup() {
        System.setProperty("webdriver.chrome.driver", chromedriverPath);
        driver = new ChromeDriver();
    }

    @AfterTest
    void teardown() {
        driver.quit();
    }


}
