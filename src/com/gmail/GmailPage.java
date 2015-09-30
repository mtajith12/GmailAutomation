package com.gmail;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
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

//GmailPage Class Inherits From SuiteGoogleBase Class.
//So, GmailPage Class Is Child Class Of SuiteGoogleBase Class And SuiteBase Class.
public class GmailPage extends SuiteGoogleBase{
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

	
	

	  
	 @Test (enabled = true,   description = "LaunchPortal")
	  public void LaunchPortal()throws Exception{
		 //To navigate to URL. It will read site URL from PropertyUtil.properties file
		  driver.get(PropertyUtil.getProperty("SITE_URL"));		
		  driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
		  driver.manage().window().maximize();
		  String Title = "Google";
		  Assert.assertEquals(Title, driver.getTitle());
		  driver.findElement(By.xpath(ObjectRep.getProperty("gmail"))).click();
		  driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
		  driver.getPageSource().contains("Already use Gmail?");
		
		  driver.findElement(By.xpath(ObjectRep.getProperty("gmail-create-account"))).click();
		  driver.getPageSource().contains("One account is all you need");
		  
		  driver.findElement(By.id(ObjectRep.getProperty("FN"))).sendKeys("Ajith");
		  driver.findElement(By.id(ObjectRep.getProperty("LN"))).sendKeys("kumar");
		
		  driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
		  driver.findElement(By.id(ObjectRep.getProperty("Passwd"))).sendKeys("azmuyt@123");
		  driver.findElement(By.id(ObjectRep.getProperty("ConfirmPasswd"))).sendKeys("azmuyt@123");
		
		  //Click on the Arrow mark
	        driver.findElement(By.xpath("//label[@id='month-label']/span/div/div")).click();
	        //Select value from the list
	        driver.findElement(By.xpath("//label[@id='month-label']/span/div[2]/div[@id=':5']")).click();
	        driver.findElement(By.id("BirthDay")).sendKeys("26");
	        driver.findElement(By.id("BirthYear")).sendKeys("1984");
	        driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
	        
	       // driver.findElement(By.xpath(".//*[@id='Gender']/div")).click();
	       // driver.findElement(By.xpath(".//*[@id='Gender']/div[3]/div[@id=':d']")).click();
	       /* WebElement genderselect = driver.findElement(By.xpath("//div[@class='goog-menu goog-menu-vertical']/div[@id=':e']"));
            Actions action1=new Actions(driver);
            action1.click(genderselect);
            action1.perform();
	        driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);*/
	        driver.findElement(By.xpath(".//*[@id='Gender']/div")).click();
	       // driver.findElement(By.xpath(".//*[@id='Gender']/div[2]/div[@id=':d']")).click();
	     //   driver.findElement(By.xpath("//*[@id='Gender']/div[1]/div[2]")).sendKeys("Male");
	      //  driver.findElement(By.xpath("Gender")).sendKeys("Male");
	       
	        
	  //   getElementByXPath(ObjectRep.getProperty("Gender")).click();
	       // Select  select1 =new Select(gender);
	      //  select1.selectByValue("Male");

//	        WebElement gender = driver.findElement(By.xpath("//div[@id='Gender']/div[@class='goog-menu goog-menu-vertical']/div"));
//        Actions action = new Actions(driver);
//        action.click(gender);
	    /*    WebElement gender = driver.findElement(By.xpath("//div[@id='Gender']"));
            Actions action = new Actions(driver);
            action.click(gender);
          //  action.keyDown(Keys.ARROW_DOWN).click();
           
            action.sendKeys("Male");*/
	        
//	        WebElement genderselect = driver.findElement(By.xpath("//div[@id='Gender']"));
//            Actions action1=new Actions(driver);
//            action1.click(genderselect);
//            action1.sendKeys("Male");
//            action1.perform();
            

/*WebElement genderselect = driver.findElement(By.xpath("//div[@role='listbox']/div[@id=':e']"));
            Actions action1=new Actions(driver);
            action1.click(genderselect);
            action1.perform();*/
            
          //  action.perform();
          //  action.keyDown(Keys.ARROW_DOWN).click();
	        
	        
	        
	        
	        driver.findElement(By.name("RecoveryPhoneNumber")).sendKeys("9900563321");
		  
	        driver.findElement(By.name("RecoveryEmailAddress")).sendKeys("mt.ajith@gmail.com");   
	        
	        /// Enter the capcha manually 
	       
	        System.out.println("Enter the capcha manually and wait for 20 seconnds");
	        driver.findElement(By.id("SkipCaptcha")).click();
	       
	        
	        driver.findElement(By.id("termsofservice-label")).click();
	        
	       
	       
	        driver.findElement(By.id("submitbutton")).click();
	        System.out.println("Enter the verification code sent on ur registered mobile number");
	     
	       
	        System.out.println("Gmail Registration is not sucessfuly ");
	        
	        Assert.assertEquals(ObjectRep.getProperty("expectedErrorMsg"),"You can't leave this empty.", "Please enter the valid userName");
		   }
	  
	 
		
	
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
	
}