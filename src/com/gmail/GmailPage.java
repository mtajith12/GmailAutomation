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

	
	

	  
	 @Test (enabled = true,   /*dataProvider = "GoooleRegistrationDataProvider",*/ priority= 0,description = "LaunchPortal")
	  public void GoooleRegistration()throws Exception{
		 //To navigate to URL. It will read site URL from PropertyUtil.properties file
		  driver.get(PropertyUtil.getProperty("SITE_URL"));		
		  driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
		  driver.manage().window().maximize();
		  String Title = "Google";
		  Assert.assertEquals(Title, driver.getTitle());
		  //Clicking on Gmail link
		  getElementByXPath("gmail").click();
		 
		  driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
		  //Verifying the page content
		  driver.getPageSource().contains("Already use Gmail?");
		//Clicking on Google signup
		  getElementByXPath("gmail-create-account").click();
		  driver.getPageSource().contains("One account is all you need");
		  //Entering the personal details
		  getElementByID("FN").sendKeys("Ajith");
		  getElementByID("LN").sendKeys("kumar");
		
		  driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
		  getElementByID("Passwd").sendKeys("azmuyt@123");
		  getElementByID("ConfirmPasswd").sendKeys("azmuyt@123");
		
		  //Click on the Month Arrow mark		  
		   getElementByXPath("Month").click();
	        //Select value from the list
		   getElementByXPath("month-label").click();
		   getElementByID("BirthDay").sendKeys("26");
		   getElementByID("BirthYear").sendKeys("1984");
	        driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
	        //Seleing the Gender
	        getElementByXPath("Gender").click();
	        
	        getElementByName("RecoveryPhoneNumber").sendKeys("9900563321");
		  
	        getElementByName("RecoveryEmailAddress").sendKeys("mt.ajith@gmail.com");         
	     
	        getElementByID("SkipCaptcha").click();	       
	        
	        getElementByID("termsofservice-label").click();  
	        getElementByID("submitbutton").click();
	        driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
	       
	        Add_Log.info("Gmail Registration is not sucessfuly, Please enter the mandatary fields ");
	        
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
	public Object[][] GoooleRegistrationDataProvider(){
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