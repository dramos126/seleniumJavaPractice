package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public class PracticeTests extends BaseTest {

    static final int[] validResponses = {200, 201};
    static final String username = "user@phptravels.com";
    static final String validCreds = "demouser";
    static final String invalidCreds = "demouser21344";


    @Test
    void validLinks()  {
        driver.get("http://youtube.com");
        waitUtil.staticWait(1000);
        List<WebElement> elements = driver.findElements(By.tagName("a"));
        List<String> badURLs = new ArrayList<>(Collections.emptyList());
        System.out.printf("found %d possible urls", elements.size());

        for (WebElement element : elements) {
            String url = element.getAttribute("href");
            String badUrlMsg;

            if (!Objects.isNull(url) && !url.isEmpty()) {
                try {
                    System.out.println("checking url - " + url);
                    HttpURLConnection httpUrlConnection = (HttpURLConnection) new URL(url).openConnection();
                    httpUrlConnection.setRequestMethod("HEAD");
                    httpUrlConnection.connect();

                    int responseCode = httpUrlConnection.getResponseCode();
                    boolean isValid = IntStream.of(validResponses).anyMatch(x -> x == responseCode);

                    if (!isValid) {
                        badUrlMsg = String.format("url %s returned response code %d \n", url, responseCode);
                        badURLs.add(badUrlMsg);
                    }
                } catch (Exception e) {
                    badUrlMsg = String.format("caught error while checking %s. Exception %s \n", url, e);
                    badURLs.add(badUrlMsg);
                }
            }
        }
        if (!badURLs.isEmpty()) {
            Assert.fail(String.valueOf(badURLs));
        }
    }

    @Test
    void basicLogin() {
        String expectedLandingURL = "https://www.phptravels.net/account/dashboard";

        By emailField = By.xpath("//*[@type='email' and @required='required']");
        By pwdField = By.xpath("//*[@type='password' and @required='required']");
        By loginBtn = By.xpath("//span[text()='Login']");

        driver.get("https://www.phptravels.net/login");

        driver.findElement(emailField).sendKeys(username);
        driver.findElement(pwdField).sendKeys(validCreds);
        driver.findElement(loginBtn).click();

        String landingURL = driver.getCurrentUrl();
        Assert.assertEquals(expectedLandingURL, landingURL, "did not land on expected url after logging in");
    }

}
