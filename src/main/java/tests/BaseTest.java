package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
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
        ChromeOptions opts = new ChromeOptions();
        opts.addArguments("--start-fullscreen");

        driver = new ChromeDriver(opts);
    }

    @AfterTest
    void teardown() {
        driver.quit();
    }


}
