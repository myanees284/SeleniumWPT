import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.logging.Logs;
import org.openqa.selenium.remote.CapabilityType;
 
public class TestChrome {
	
	static void printLog(String type,WebDriver driver) {
	    List<LogEntry> entries = driver.manage().logs().get(type).getAll();
	    System.out.println(entries.size() + " " + type + " log entries found");
	    for (LogEntry entry : entries) {
	      System.out.println(
	          new Date(entry.getTimestamp()) + " " + entry.getLevel() + " " + entry.getMessage());
	    }
	  }
	
	static void submitPerformanceResult(String name, List<LogEntry> perfLogEntries,WebDriver driver)
		      throws IOException, JSONException {
		    JSONArray devToolsLog = new JSONArray();
		    System.out.println(perfLogEntries.size() + " performance log entries found");
		    for (LogEntry entry : perfLogEntries) {
		      JSONObject message = new JSONObject(entry.getMessage());
		      JSONObject devToolsMessage = message.getJSONObject("message");
		      // System.out.println(
		      //     devToolsMessage.getString("method") + " " + message.getString("webview"));
		      devToolsLog.put(devToolsMessage);
		    }
		    byte[] screenshot = null;
		    //if (null == androidPackage) {  // Chrome on Android does not yet support screenshots
		      screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
		  //  }
		    String resultUrl = new WebPageTest(new URL("http://192.168.1.108:4000/"), "Test", name)
		        .submitResult(devToolsLog, screenshot);
		    System.out.println("Result page: " + resultUrl);
		  }
 
public static void main(String[] args) {
 
System.setProperty("webdriver.chrome.driver", "C:\\tets\\chromedriver.exe");
 
// Initialize browser
ChromeOptions options = new ChromeOptions();
options.addArguments("no-sandbox");
options.addArguments("disable-extensions");

LoggingPreferences log= new LoggingPreferences();
log.enable(LogType.BROWSER, Level.ALL);

log.enable(LogType.PERFORMANCE, Level.ALL);
options.setCapability(CapabilityType.LOGGING_PREFS, log);
options.setAcceptInsecureCerts(true);
WebDriver driver=new ChromeDriver(options);
 

/*driver.get("http://www.ndtv.com");
driver.get("http://www.emirates.com/ae/english/experience/cabin-features/first-class/");*/
driver.get("http://www.google.com");
WebElement element = driver.findElement(By.name("q"));
element.sendKeys("Selenium Conference 2013");
element.submit();

// Maximize browser
 
driver.manage().window().maximize();
Logs logs = driver.manage().logs();
System.out.println("Log types: " + logs.getAvailableLogTypes());

printLog(LogType.BROWSER,driver);

try {
	submitPerformanceResult("http://www.emirates.com", logs.get(LogType.PERFORMANCE).getAll(),driver);
	driver.quit();
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
} catch (JSONException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
 
}
 
}