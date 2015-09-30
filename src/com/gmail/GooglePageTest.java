package com.gmail;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import atu.testrecorder.ATUTestRecorder;
import atu.testrecorder.exceptions.ATUTestRecorderException;

import com.gmail.utility.Read_XLS;
import com.gmail.utility.SuiteUtility;

//RSPTestOne Class Inherits From SuiteRSPBase Class.
//So, RSPTestOne Class Is Child Class Of SuiteRSPBase Class And SuiteBase Class.
public class GooglePageTest extends SuiteGoogleBase{
	Read_XLS FilePath = null;
	String SheetName = null;	
	String TestCaseName = null;	
	String ToRunColumnNameTestCase = null;
	String ToRunColumnNameTestData = null;
	String TestDataToRun[]=null;
	static boolean TestCasePass=true;
	static int DataSet=-1;	
	static boolean Testskip=false;
	static boolean Testfail=false;
	SoftAssert s_assert =null;	
	ATUTestRecorder recorder;
	
	@BeforeTest
	public void checkCaseToRun() throws IOException{
		//Called init() function from SuiteBase class to Initialize .xls Files
		init();			
		//To set SuiteOne.xls file's path In FilePath Variable.
		FilePath = TestCaseListExcelOne;		
		TestCaseName = this.getClass().getSimpleName();	
		//SheetName to check CaseToRun flag against test case.
		SheetName = "TestCasesList";
		//Name of column In TestCasesList Excel sheet.
		ToRunColumnNameTestCase = "CaseToRun";
		//Name of column In Test Case Data sheets.
		ToRunColumnNameTestData = "DataToRun";
		//Bellow given syntax will Insert log In applog.log file.
		Add_Log.info(TestCaseName+" : Execution started.");
		
		//To check test case's CaseToRun = Y or N In related excel sheet.
		//If CaseToRun = N or blank, Test case will skip execution. Else It will be executed.
		if(!SuiteUtility.checkToRunUtility(FilePath, SheetName,ToRunColumnNameTestCase,TestCaseName)){
			Add_Log.info(TestCaseName+" : CaseToRun = N for So Skipping Execution.");
			//To report result as skip for test cases In TestCasesList sheet.
			SuiteUtility.WriteResultUtility(FilePath, SheetName, "Pass/Fail/Skip", TestCaseName, "SKIP");
			//To throw skip exception for this test case.
			throw new SkipException(TestCaseName+"'s CaseToRun Flag Is 'N' Or Blank. So Skipping Execution Of "+TestCaseName);
		}	
		//To retrieve DataToRun flags of all data set lines from related test data sheet.
		TestDataToRun = SuiteUtility.checkToRunUtilityOfData(FilePath, TestCaseName, ToRunColumnNameTestData);
		
		  DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH-mm-ss");
		  Date date = new Date();
		  //Created object of ATUTestRecorder
		  //Provide path to store videos and file name format.
		  try {
			recorder = new ATUTestRecorder("E:\\ScriptVideos\\","TestVideo-"+dateFormat.format(date),true);
		
		  //To start video recording.
		  recorder.start(); 
		  Add_Log.info("ATUTestRecorder is started");
		  loadWebBrowser();
		  driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		  driver.manage().window().maximize();
		  } catch (ATUTestRecorderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  
		 }

	
	
	//Accepts 4 column's String data In every Iteration.
	@Test(enabled = false,dataProvider="SuiteOneCaseOneData")
	public void SuiteOneCaseOneTest(String DataCol1,String DataCol2,String DataCol3,String ExpectedResult){
		
		DataSet++;
		
		//Created object of testng SoftAssert class.
		s_assert = new SoftAssert();
		
		//If found DataToRun = "N" for data set then execution will be skipped for that data set.
		if(!TestDataToRun[DataSet].equalsIgnoreCase("Y")){	
			Add_Log.info(TestCaseName+" : DataToRun = N for data set line "+(DataSet+1)+" So skipping Its execution.");
			//If DataToRun = "N", Set Testskip=true.
			Testskip=true;
			throw new SkipException("DataToRun for row number "+DataSet+" Is No Or Blank. So Skipping Its Execution.");
		}
		
		//If found DataToRun = "Y" for data set then bellow given lines will be executed.
		//To Convert data from String to Integer
		int ValueOne = Integer.parseInt(DataCol1);
		int ValueTwo = Integer.parseInt(DataCol2);
		int ValueThree = Integer.parseInt(DataCol3);
		int ExpectedResultInt =  Integer.parseInt(ExpectedResult);
		
		//To Initialize  browser.
		loadWebBrowser();		
		
		//To navigate to URL. It will read site URL from PropertyUtil.properties file
		driver.get(PropertyUtil.getProperty("siteURL")+"/2014/04/calc.html");		
		
		//Simple calc test.
		
		//Locate Element by Name Locator example.
		getElementByName("txt_Result").clear();
		
		//Locate element by dynamic xPath example.
		getElementByXPath("btn_Calc_PrePart",ValueOne,"btn_Calc_PostPart").click();
		
		//Locate Element by ID Locator example.
		getElementByID("btn_Plus").click();
		
		getElementByXPath("btn_Calc_PrePart",ValueTwo,"btn_Calc_PostPart").click();
		
		getElementByID("btn_Plus").click();
				
		getElementByXPath("btn_Calc_PrePart",ValueThree,"btn_Calc_PostPart").click();
		
		//Locate Element by cssSelector Locator example.
		getElementByCSS("btn_Equals").click();
		
		String Result = getElementByName("txt_Result").getAttribute("value");
		
		//To Convert data from String to Integer
		int ActualResultInt =  Integer.parseInt(Result);
		
		//Compare actual and expected values.
		if(!(ActualResultInt==ExpectedResultInt)){
			//If expected and actual results not match, Set flag Testfail=true.
			Testfail=true;	
			//If result Is fail then test failure will be captured Inside s_assert object reference.
			//This soft assertion will not stop your test execution.
			s_assert.assertEquals(ActualResultInt, ExpectedResultInt, "ActualResult Value "+ActualResultInt+" And ExpectedResult Value "+ExpectedResultInt+" Not Match");
		}
		
		if(Testfail){
			//At last, test data assertion failure will be reported In testNG reports and It will mark your test data, test case and test suite as fail.
			s_assert.assertAll();
			loadWebBrowser();	
		}
	}
	

	  
	 @Test (enabled = true,   description = "LaunchPortal")
	  public void LaunchPortal()throws Exception{
		 //To navigate to URL. It will read site URL from PropertyUtil.properties file
		  driver.get(PropertyUtil.getProperty("SITE_URL"));		
		  driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
		  driver.manage().window().maximize();
		  String Title = "Google";
		  Assert.assertEquals(Title, driver.getTitle());
		  
		   }
	  
	  @Test (enabled = true,   dependsOnMethods = {"LaunchPortal"})
	  public void testCompanyProfile() {
		 
		  driver.findElement(By.xpath(ObjectRep.getProperty("Company_Profile"))).click();
		  driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
		  JavascriptExecutor js = (JavascriptExecutor) driver;
		  js.executeScript("window.scrollBy(1600,0)", "");
		  driver.findElement(By.cssSelector(ObjectRep.getProperty("FY1"))).click();
		  WebElement cleaarbut1 = driver.findElement(By.cssSelector(ObjectRep.getProperty("clearbtn1")));//.click();
		  Actions bar = new Actions(driver);
		  bar.click(cleaarbut1).perform();
		  driver.findElement(By.cssSelector(ObjectRep.getProperty("FYI2"))).click();
		  WebElement cleaarbut2 = driver.findElement(By.cssSelector(ObjectRep.getProperty("clearbtn1")));//.click();
		  Actions bar1 = new Actions(driver);
		  bar1.click(cleaarbut2).perform();
		  driver.findElement(By.xpath(ObjectRep.getProperty("FYI3"))).click();
		  WebElement cleaarbut3 =driver.findElement(By.cssSelector(ObjectRep.getProperty("clearbtn1")));//.click();
		  Actions bar11 = new Actions(driver);
		  bar11.click(cleaarbut3).perform();
		  System.out.println("profile page is clicked");
		 }

	  @Test (enabled = false,   dependsOnMethods = {"LaunchPortal"}, description = "login")
		public void companyprofile() throws Exception {
			driver.findElement(By.linkText(ObjectRep.getProperty("companyprofile"))).click();
			driver.findElement(By.xpath(ObjectRep.getProperty("fy2014"))).click();
			driver.findElement(By.xpath(ObjectRep.getProperty("clear2014"))).click();
			driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
			driver.findElement(By.xpath(ObjectRep.getProperty("fy2013"))).click();
			driver.findElement(By.xpath(ObjectRep.getProperty("clear2013"))).click();
			driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
			driver.findElement(By.xpath(ObjectRep.getProperty("fy2012"))).click();
			driver.findElement(By.xpath(ObjectRep.getProperty("clear2012"))).click();
			driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
			
			Add_Log.info("Company profile page reached");
			//Add_Log1.log(LogStatus.INFO, "Company profile page reached");
			

			driver.manage().timeouts().implicitlyWait(10000, TimeUnit.SECONDS);

		}

		
	

	  
	  
	  
	 @Test (enabled = false,   dependsOnMethods = {"LanchPortal"}, description = "Verify Dashboard")
	  public void Verifydashboard()throws Exception{
		 Assert.assertEquals("Dashboard - Inmarsat", driver.getTitle());
		 driver.getPageSource().contains("Welcome" +"resellerisat237@mailinator.com");
		 driver.getPageSource().contains("Dashboard");
		 driver.getPageSource().contains("Resellers");
		 driver.getPageSource().contains("Operators");
		 driver.getPageSource().contains("Access Network Status");
		 driver.getPageSource().contains("System Status");
		 driver.getPageSource().contains("Purchases");
		 driver.getPageSource().contains("Catalogue");
		 driver.getPageSource().contains("Subscriptions");
		 driver.getPageSource().contains("Assigned Activities");
		 driver.getPageSource().contains("Messages");
		 driver.getPageSource().contains("Sales");
		 List<WebElement> allElements = driver.findElements(By.xpath("//div/nav/ul/li/a"));
			
			for (WebElement element : allElements) {
				if (element.getText().equals("Dashboard")) {
					System.out.println("Dashboard is there");
					Actions action2 = new Actions(driver);
					action2.click(element);
					System.out.println(driver.getTitle());

					driver.manage().timeouts()
							.implicitlyWait(60, TimeUnit.SECONDS);
					
						WebElement reseller = driver
								.findElement(By
										.xpath(PropertyUtil.getProperty("resellerblock")));

						System.out.println(reseller.toString());
						if (reseller.isEnabled()) {
							reseller.click();
							System.out.println("Reseller block is enabled");

						} else {
							System.out.println(driver.findElement(By.xpath(PropertyUtil.getProperty("error"))));
						}
						WebElement Operator = driver
								.findElement(By
										.xpath(PropertyUtil.getProperty("operator")));
						if (Operator.isEnabled()) {
							System.out.println("Operator block is displayed");

						}

						WebElement Acess = driver
								.findElement(By
										.xpath(PropertyUtil.getProperty("access")));
						if (Acess.isEnabled()) {
							System.out.println("Acess block is displayed");

						}
						WebElement system = driver
								.findElement(By
										.xpath(PropertyUtil.getProperty("system")));
						if (system.isEnabled()) {
							System.out.println("system block is displayed");

						}
						/*WebElement pendingorder = driver
								.findElement(By
										.xpath(PropertyUtil.getProperty("pendingorder")));
						if (pendingorder.isEnabled()) {
							System.out
									.println("pending order  block is displayed");

						}*/
						WebElement message = driver
								.findElement(By
										.xpath(PropertyUtil.getProperty("message")));
						if (message.isEnabled()) {
							System.out.println("Message   block is displayed");

						}

						WebElement purchasecatlogue = driver
								.findElement(By
										.xpath(PropertyUtil.getProperty("purchase")));
						if (purchasecatlogue.isEnabled()) {
							System.out
									.println("purchasecatlogue   block is displayed");

						}

						WebElement salescatlogue = driver
								.findElement(By
										.xpath(PropertyUtil.getProperty("sales")));
						if (salescatlogue.isEnabled()) {
							System.out
									.println("salescatlogue   block is displayed");

						}
					} 

				}
		 }
	  

	 
		@Test(enabled = false, dependsOnMethods = { "LanchPortal","testlogin"}, description = "Reseller tab functinality")
		public void testResellerTab() throws Exception {
			List<WebElement> allElements = driver.findElements(By
					.xpath(PropertyUtil.getProperty("allelements")));
			System.out.println(allElements);
			for (WebElement element : allElements) {
				if (element.getText().equals("Resellers")) {
					System.out.println("Resellers is there");
					JavascriptExecutor js = (JavascriptExecutor) driver;
					js.executeScript("arguments[0].click();", element);

					System.out.println("Resellers");

					driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
					break;
				}
			}
			WebElement resellertree1 =getElementByXPath(PropertyUtil.getProperty("resellertree"));
			System.out.println("resellertree"+PropertyUtil.getProperty("resellertree"));
			WebElement resellertree = driver.findElement(By.xpath(PropertyUtil.getProperty("resellertree")));
			if (resellertree.isEnabled()) {
				System.out.println("Reseller tree exist");
			} else {
				System.out.println("Reseller tree not found");
			}
			WebElement profiletab = driver.findElement(By
					.xpath(PropertyUtil.getProperty("profiletab")));
			if (profiletab.isDisplayed()) {
				System.out.println("profile tab is displayed");
			}
			WebElement resellertab = driver.findElement(By.xpath(PropertyUtil.getProperty("resellertab")));
			if (resellertab.isEnabled()) {
				System.out.println("reseller tab exists and enabled");
			}

			WebElement searchcriteria = driver.findElement(By.id(PropertyUtil.getProperty("searchcriteria")));
			WebElement searchData = driver.findElement(By.name(PropertyUtil.getProperty("searchdata")));
			WebElement onboard = driver.findElement(By.xpath(PropertyUtil.getProperty("onboard")));
			WebElement resellertbgrid = driver.findElement(By.className(PropertyUtil.getProperty("resellertbgrid")));

			if (searchcriteria.isDisplayed()) {
				System.out.println("search  criteria is displayed");
			}
			if (searchData.isDisplayed()) {
				System.out.println("search data is displayed");
			}
			if (onboard.isDisplayed()) {
				System.out.println("onbaord  button is displayed");
			}
			if (resellertbgrid.isDisplayed()) {
				System.out.println("resellergrid  is displayed");
			}
		}
		
		//@Test
		  public void testOnboardReseller(String customername, String displayname,
					String minimumbilling, String credit, String billing,
					String taxcategory, String family, String givenname,
					String Addresss, String zipcode, String phone, String Emailid1,
					String Family2, String givename2, String Address2, String zipcode1,
					String phone2, String Emailid2) {
				WebElement onboard = driver.findElement(By.xpath(PropertyUtil
						.getProperty("onboard1")));
				onboard.click();
				System.out.println(driver.getTitle());
				WebElement navigate = driver.findElement(By
						.xpath(PropertyUtil.getProperty("navigation")));
				if (navigate.isDisplayed()) {

					System.out.println("navigation bar exist");

				}
				// WebElement custname
				// =driver.findElement(By.xpath("//div[@id='aui_3_4_0_1_407']/span/span/label/span"));
				// if(custname.isEnabled()){

				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				//
				// driver.findElement(By.linkText("Details")).click();// details

				System.out.println("Filling details form");}
	
	//@AfterMethod method will be executed after execution of @Test method every time.
	@AfterMethod
	public void reporterDataResults(){		
		if(Testskip){
			Add_Log.info(TestCaseName+" : Reporting test data set line "+(DataSet+1)+" as SKIP In excel.");
			//If found Testskip = true, Result will be reported as SKIP against data set line In excel sheet.
			SuiteUtility.WriteResultUtility(FilePath, TestCaseName, "Pass/Fail/Skip", DataSet+1, "SKIP");
		}
		else if(Testfail){
			Add_Log.info(TestCaseName+" : Reporting test data set line "+(DataSet+1)+" as FAIL In excel.");
			//To make object reference null after reporting In report.
			s_assert = null;
			//Set TestCasePass = false to report test case as fail In excel sheet.
			TestCasePass=false;	
			//If found Testfail = true, Result will be reported as FAIL against data set line In excel sheet.
			SuiteUtility.WriteResultUtility(FilePath, TestCaseName, "Pass/Fail/Skip", DataSet+1, "FAIL");			
		}else{
			Add_Log.info(TestCaseName+" : Reporting test data set line "+(DataSet+1)+" as PASS In excel.");
			//If found Testskip = false and Testfail = false, Result will be reported as PASS against data set line In excel sheet.
			SuiteUtility.WriteResultUtility(FilePath, TestCaseName, "Pass/Fail/Skip", DataSet+1, "PASS");
		}
		//At last make both flags as false for next data set.
		Testskip=false;
		Testfail=false;
	}
	
	//This data provider method will return 4 column's data one by one In every Iteration.
	@DataProvider
	public Object[][] SuiteOneCaseOneData(){
		//To retrieve data from Data 1 Column,Data 2 Column,Data 3 Column and Expected Result column of SuiteOneCaseOne data Sheet.
		//Last two columns (DataToRun and Pass/Fail/Skip) are Ignored programatically when reading test data.
		return SuiteUtility.GetTestDataUtility(FilePath, TestCaseName);
	}	
	
	//To report result as pass or fail for test cases In TestCasesList sheet.
	@AfterTest
	public void closeBrowser() throws ATUTestRecorderException{
		//To Close the web browser at the end of test.
		
			closeWebBrowser();
		
		if(TestCasePass){
			Add_Log.info(TestCaseName+" : Reporting test case as PASS In excel.");
			SuiteUtility.WriteResultUtility(FilePath, SheetName, "Pass/Fail/Skip", TestCaseName, "PASS");
		}
		else{
			Add_Log.info(TestCaseName+" : Reporting test case as FAIL In excel.");
			SuiteUtility.WriteResultUtility(FilePath, SheetName, "Pass/Fail/Skip", TestCaseName, "FAIL");			
		}
	}
}