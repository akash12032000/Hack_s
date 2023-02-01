import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


class AutoLogin {
	
	
	static HashMap<String, String> map = new HashMap<String, String>();
	
	static {
		File file = new File("Email.txt");
		
		  BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
      String st;
      try {
		while ((st = br.readLine()) != null){
		      String str[] = st.split("=");
		      try {
		      map.put(str[0],str[1]);
		      }catch (Exception e) {
			}
		}
	} catch (IOException e) {
		e.printStackTrace();
	}
	}
	
	
	public static void Subscribe(String email,String pass,String URL) {
		System.out.println("email "+email+" Pass "+pass);
		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("useAutomationExtension", true);
		options.addArguments("--disable-notifications");
		options.setExperimentalOption("excludeSwitches",Arrays.asList("disable-popup-blocking","enable-automation"));
		options.addArguments("disable-infobars");
		WebDriver driver = new ChromeDriver(options);

		driver.navigate().to("https://accounts.google.com");
		driver.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
		driver.findElement(By.id("identifierId")).sendKeys(email.trim()); 
		driver.findElement(By.xpath("//*[@id=\'identifierNext\']/div/button/span")).click();
		driver.findElement(By.name("Passwd")).sendKeys(pass.trim());
		driver.findElement(By.xpath("//*[@id=\'passwordNext\']/div/button/span")).click();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		driver.navigate().to("https://www.google.com"); 
		driver.navigate().to(URL);  
		
		// like button 
		// driver1.findElement(By.xpath("//*[@id='segmented-like-button']/ytd-toggle-button-renderer/yt-button-shape/button/yt-touch-feedback-shape/div/div[2]")).click();
		
		// subscribe button
		try {
		if(!driver.findElement(By.xpath("//*[@id=\'notification-preference-button\']/ytd-subscription-notification-toggle-button-renderer-next/yt-button-shape/button/div[2]/span")).getText().equalsIgnoreCase("Subscribed"))
			driver.findElement(By.xpath("//*[@id=\'subscribe-button\']/ytd-subscribe-button-renderer/yt-button-shape/button/yt-touch-feedback-shape/div/div[2]")).click();
		}catch (Exception e) {
		}
		driver.close();
	}
	

	public static void main(String[] args){
		System.setProperty("webdriver.chrome.driver" , "C:\\Users\\Acutec\\Documents\\chromedriver_win32\\chromedriver.exe");
//		System.setProperty("webdriver.chrome.verboseLogging", "true");
	
		 for (Map.Entry<String,String> entry : map.entrySet()) {
	            Subscribe(entry.getKey(), entry.getValue(), "https://www.youtube.com/channel/UCWDKO4G46pUPdoiWLHgDjUA");
	            
	    }
		System.out.println("Closer End");
	}

}
