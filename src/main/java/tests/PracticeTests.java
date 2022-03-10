package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.stream.IntStream;

public class PracticeTests extends BaseTest {

    static final int[] validResponses = {200, 201};
    static final String username = "user@phptravels.com";
    static final String validCreds = "demouser";
    static final String youtube = "http://youtube.com";

    @Test
    void validLinks() throws InterruptedException {
        driver.get(youtube);
        List<WebElement> elements = driver.findElements(By.tagName("a"));
        List<Thread> threads = new ArrayList<>(Collections.emptyList());
        List<String> badURLs = new ArrayList<>(Collections.emptyList());
        System.out.printf("found %d possible urls", elements.size());

        for (WebElement element : elements) {
            Thread t = new Thread(() -> {
                String url = element.getAttribute("href");
                String badUrlMsg;
                if (!Objects.isNull(url) && url.startsWith("http")) {
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
            });
            threads.add(t);
            t.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        if (!badURLs.isEmpty()) {
            Assert.fail(String.valueOf(badURLs));
        }
    }

    @Test
    void basicLogin() {
        String expectedLandingURL = "https://www.phptravels.net/account/dashboard";

        By emailField = By.name("email");
        By pwdField = By.name("password");
        By loginBtn = By.xpath("//span[text()='Login']");

        driver.get("https://www.phptravels.net/login");

        driver.findElement(emailField).sendKeys(username);
        driver.findElement(pwdField).sendKeys(validCreds);
        driver.findElement(loginBtn).click();

        String landingURL = driver.getCurrentUrl();
        Assert.assertEquals(expectedLandingURL, landingURL, "did not land on expected url after logging in");
    }

    /**
     * WIP
     * still get same url as previous from time to time
     */
    @Test
    void monkeyTestLinks() throws InterruptedException {
        int clicks = 25;

        driver.get(youtube);
        String previousURL = "";

        while (clicks > 0) {
            System.out.printf("%d clicks remaining \n", clicks);
            clicks--;

            Thread.sleep(150);

            ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
            driver.switchTo().window(tabs.get(0));

            List<WebElement> elements = driver.findElements(By.tagName("a"));
            List<WebElement> validElements = new ArrayList<>(Collections.emptyList());

            elements.forEach((element) -> {
                try {
                    String link = element.getAttribute("href");
                    if (!Objects.isNull(link) && link.startsWith("http")) {
                        validElements.add(element);
                    }
                } catch (StaleElementReferenceException ignored) {

                }
            });

            String link;
            int index;

            do {
                index = new Random().nextInt(validElements.size() - 1);
                link = validElements.get(index).getAttribute("href");
            } while (link.equalsIgnoreCase(previousURL) || link.equalsIgnoreCase(driver.getCurrentUrl()));

            System.out.printf("clicking %s\n", link);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click()", validElements.get(index));
            tabs = new ArrayList<>(driver.getWindowHandles());


            if (tabs.size() > 1) {
                driver.switchTo().window(tabs.get(1));
            }
            String landingURL = driver.getCurrentUrl();

            if (!link.equals(landingURL)) {
                Assert.assertNotEquals(landingURL, previousURL);
            }

            previousURL = link;

            if (tabs.size() > 1) {
                for (int i = 0; i < tabs.size(); i++) {
                    if (i != 0) {
                        driver.switchTo().window(tabs.get(i));
                        driver.close();
                    }
                }
            }
        }
    }

}
