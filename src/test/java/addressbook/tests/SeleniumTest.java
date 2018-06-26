package addressbook.tests;

import java.net.MalformedURLException;
import java.util.regex.*;

import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.remote.RemoteWebDriver;

public class SeleniumTest {
	WebDriver driver;
	WebDriverWait wait;
	WebElement signinButton;
	WebElement newAddressLink;
	WebElement createNewAddressButton;
	WebElement updateAddressButton;
	WebElement countryRadioButton;
	WebElement dobBox;
	WebElement ageBox;
	WebElement uploadPicture;
	WebElement successfulAddressUpdateMsg;
	WebElement successfulAddressDeletionMsg;
	
	  public static final String USERNAME = "cproint";
	  public static final String ACCESS_KEY = "7988198b-9ffe-4cc9-acf7-dbbebb99e12d";
	  public static final String URL = "http://" + USERNAME + ":" + ACCESS_KEY + "@ondemand.saucelabs.com:80/wd/hub";


    @Before
    public void setup() throws MalformedURLException {
    System.out.println(System.getProperty("user.dir"));    
    	System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"/lib/drivers/chromedriver");
        //ChromeOptions chromeOptions = new ChromeOptions();

        DesiredCapabilities caps = DesiredCapabilities.chrome();
	    caps.setCapability("platform", "Mac");
	    //caps.setCapability("version", "43.0");

	    driver = new RemoteWebDriver(new java.net.URL(URL), caps);
        
        //driver = new ChromeDriver(chromeOptions);
        wait = new WebDriverWait(driver, 20);

    }
    
    @Test
    public void journey() throws InterruptedException  {
        driver.get("http://a.testaddressbook.com");
        // Add your first name and last name to the strings below

        // Use these variables to populate the fields below

        String username = "user@example.com";
        String password = "password";
        String firstName = "Murali";
        String lastName = "Tulugu";
        String streetAddress = "123 Main";
        String secondaryAddress = "Apt 1";
        String city = "San Francisco";
        //String state = "CA";
        String zipCode = "94107";
        String fileLocation = System.getProperty("user.dir")+"/lib/smiley.gif";
        String birthday = "01/01/1980";
        String age = "38";
        String website = "http://example.com";
        String phone = "5128675309";
        String note = "This person is nice.";


        // Navigate to Sign In Page
        
        driver.findElement(By.id("sign-in")).click();
        signinButton= wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@value='Sign in']")));

        // Submit Sign In Form
        driver.findElement(By.id("session_email")).sendKeys(username);
        driver.findElement(By.id("session_password")).sendKeys(password);
        signinButton.click();

        // Navigate to Address List Page
        driver.findElement(By.linkText("Addresses")).click();
        newAddressLink= wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("New Address")));

        // Navigate to New Address Page
        driver.findElement(By.linkText("New Address")).click();
        createNewAddressButton= wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@value='Create Address']")));
        
        // Fill Out New Address Form & Submit

            // First Name, Last Name, Street Address, Secondary Address & City
        	driver.findElement(By.id("address_first_name")).sendKeys(firstName);
        	driver.findElement(By.id("address_last_name")).sendKeys(lastName);
        	driver.findElement(By.id("address_street_address")).sendKeys(streetAddress);
        	driver.findElement(By.id("address_secondary_address")).sendKeys(secondaryAddress);
        	driver.findElement(By.id("address_city")).sendKeys(city);

            // State (use Select class)

        	Select drpState = new Select (driver.findElement(By.id("address_state")));
        	drpState.selectByVisibleText("California");
        	
            // Zip Code, Country, Birthday, Age, Website
        	driver.findElement(By.id("address_zip_code")).sendKeys(zipCode);
        	countryRadioButton = driver.findElement(By.id("address_country_true"));
        	countryRadioButton.click();
        	dobBox = driver.findElement(By.id("address_birthday"));

        	dobBox.sendKeys(birthday.substring(0,2));
        	dobBox.sendKeys(birthday.substring(3,5));
        	dobBox.sendKeys(birthday.substring(6,10));
        
        	ageBox = driver.findElement(By.id("address_age"));
        	ageBox.sendKeys(age);
        	driver.findElement(By.id("address_website")).sendKeys(website);

                 

        // Upload File
            uploadPicture = driver.findElement(By.id("address_picture"));
            uploadPicture.sendKeys(fileLocation);
            
        // Phone, Interest, Note
        	driver.findElement(By.id("address_phone")).sendKeys(phone);
        	driver.findElement(By.id("address_interest_read")).click();
        	driver.findElement(By.id("address_note")).sendKeys(note);

        	// Submit Form
        	createNewAddressButton.click();

        	// This gets the ID from URL to be used later

       String id = getID();

        // Navigate to Edit Address Page
        driver.findElement(By.linkText("Addresses")).click();
        driver.findElement(By.xpath("//a[contains(@href, '/addresses/"+id+"/edit')]")).click();
        updateAddressButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@value='Update Address']")));


        // Edit the First Name with newName and Submit

        String newName = "Kris";
    		driver.findElement(By.id("address_first_name")).clear();
        driver.findElement(By.id("address_first_name")).sendKeys(newName);
        updateAddressButton.click();

       successfulAddressUpdateMsg = driver.findElement(By.cssSelector((".alert")));
       Assert.assertEquals("Verify successful Address Update Msg", "Address was successfully updated.", successfulAddressUpdateMsg.getText().trim());
 
       // Navigate to Address List Page
       // Get list of Destroy Link elements

        driver.findElement(By.linkText("Addresses")).click();
        newAddressLink= wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("New Address")));
 
        // Iterate over the list to click the one matching the id we found above
        driver.findElement(By.xpath("//a[contains(@data-test, 'destroy-"+id+"')]")).click();
        wait.until(ExpectedConditions.alertIsPresent());

        // Accept Alert
        driver.switchTo().alert().accept();

        // Wait for link to go away
        successfulAddressDeletionMsg = driver.findElement(By.cssSelector((".alert")));
        Assert.assertEquals("Verify successful Address Deletion Msg", "Address was successfully destroyed.", successfulAddressDeletionMsg.getText().trim());

    }

    @After
    public void teardown() {
        driver.quit();
    }

    private String getID() {
        String url = driver.getCurrentUrl();
        String pattern = "\\d+$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(url);
        m.find();
        try {
            return m.group(0);
        } catch(Exception e) {
            return null;
        }
    }

}