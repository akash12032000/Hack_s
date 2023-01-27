package Login;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.http.ClientConfig;

class AutoLogin {

	public static void main(String[] args) {
		System.setProperty("webdriver.chrome.driver" , "C:\\Users\\Acutec\\Documents\\chromedriver_win32\\chromedriver.exe");
//		System.setProperty("webdriver.chrome.verboseLogging", "true");
		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("useAutomationExtension", true);
		options.addArguments("--disable-notifications");
		options.setExperimentalOption("excludeSwitches",Arrays.asList("disable-popup-blocking","enable-automation"));
		options.addArguments("disable-infobars");
		WebDriver driver1 = new ChromeDriver(options);

		
		driver1.navigate().to("https://accounts.google.com");
		driver1.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
		driver1.findElement(By.id("identifierId")).sendKeys("skk119004@gmail.com");
		driver1.findElement(By.xpath("//*[@id=\'identifierNext\']/div/button/span")).click();
		driver1.findElement(By.name("Passwd")).sendKeys("Asdf@123");
		driver1.findElement(By.xpath("//*[@id=\'passwordNext\']/div/button/span")).click();
		
		driver1.navigate().to("https://www.youtube.com/@CarryMinati");  
		driver1.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
		driver1.findElement(By.xpath("//*[@id=\'subscribe-button\']/ytd-subscribe-button-renderer/yt-button-shape/button/yt-touch-feedback-shape/div")).click();
//		driver1.close();
	}

}
