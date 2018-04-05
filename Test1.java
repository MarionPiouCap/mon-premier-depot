//suppression ligne package de base :
//package com.example.tests;
import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

// Imports pour Log4j
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.log4j.*;

//Test Selenium Webdriver : Compte Client - Connexion, Mes Billets
public class Test1 {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();
  // Pour Log4j
  private static final org.apache.log4j.Logger LOGGER = LogManager.getLogger(Test1.class);

  @Before
  public void setUp() throws Exception {
    // Lancement geckodriver lib
    System.setProperty("webdriver.gecko.driver","/home/partageFichiersVM/lib/geckodriver");
    // Lancement Log4j / fichier de propriétés de Log
    Properties logProps=new Properties();
    InputStream in=new Test1().getClass().getResourceAsStream("Log4j.properties");
    logProps.load(in);
    in.close();
    PropertyConfigurator.configure(logProps);
    driver = new FirefoxDriver();
    baseUrl = "https://www.katalon.com/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void test1() throws Exception {
    driver.get("https://int.thalys.com/fr/fr");
    //Enregistrement des informations de connexion
    String email = "etrange@svoc.com";
    String password = "1234";
    //Se connecter
    driver.findElement(By.xpath("//nav[@id='navigation-secondary']/ul/li[3]/button")).click();
    assertTrue(driver.findElement(By.xpath("(//button[@class='btn-principal margin-t-10' and @type='submit'])")).isDisplayed());
    assertTrue(driver.findElement(By.id("connectemailcin")).isDisplayed());
    driver.findElement(By.id("connectemailcin")).clear();
    driver.findElement(By.id("connectemailcin")).sendKeys(email);
    assertTrue(driver.findElement(By.id("connectpassword")).isDisplayed());
    driver.findElement(By.id("connectpassword")).clear();
    driver.findElement(By.id("connectpassword")).sendKeys(password);
    assertTrue(isElementPresent(By.xpath("(//button[@class='show-password'])")));
    driver.findElement(By.xpath("(//button[@class='btn-principal margin-t-10' and @type='submit'])")).click();
    //vérification de la redirection vers la page d'accueil après connexion
    for (int second = 0;; second++) {
    	if (second >= 60) fail("timeout");
    	try { if (driver.findElement(By.id("home")).isDisplayed()) break; } catch (Exception e) {}
    	Thread.sleep(1000);
    }
    for (int second = 0;; second++) {
    	if (second >= 60) fail("timeout");
    	try { if (driver.findElement(By.id("pre-booking-principal")).isDisplayed()) break; } catch (Exception e) {}
    	Thread.sleep(1000);
    }
    // Appui sur Mon compte en vérifiant que le site a compris qu'on s'était connecté sinon rafraichissement de la page
    if (isElementPresent(By.xpath("(//a[@class='navigation-item user-picture hidden-xs'])"))){
      driver.findElement(By.xpath("(//a[@class='navigation-item user-picture hidden-xs'])")).click();
    } else {
      driver.navigate().refresh();
      driver.findElement(By.xpath("(//a[@class='navigation-item user-picture hidden-xs'])")).click();
    }
    //Vérification des éléments de fidélité de mon compte
    try {
      assertTrue(driver.findElement(By.xpath("(//div[@class='panel panel-white panel-miles grid-100'])")).isDisplayed());
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertTrue(driver.findElement(By.xpath("(//div[@class='panel panel-white panel-miles grid-100'])")).isDisplayed());
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertTrue(driver.findElement(By.xpath("(//div[@class='panel panel-white panel-miles grid-100'])")).isDisplayed());
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    //Accéder à ses réservations/billets
    driver.findElement(By.cssSelector("a[title=\"Vos réservations\"]")).click();
    for (int second = 0;; second++) {
    	if (second >= 60) fail("timeout");
    	try { if (driver.findElement(By.xpath("//div[@id='react-tabs-1']/div")).isDisplayed()) break; } catch (Exception e) {}
    	Thread.sleep(1000);
    }

    for (int second = 0;; second++) {
    	if (second >= 60) fail("timeout");
    	try { if (driver.findElement(By.xpath("//div[@id='react-tabs-1']/div/ul/li/div/div/div/p")).isDisplayed()) break; } catch (Exception e) {}
    	Thread.sleep(1000);
    }
    // Accéder à ses réservations passées
    driver.findElement(By.id("react-tabs-2")).click();
    // Accéder à la réservation 9920 - Affichage du QRCode
    driver.findElement(By.xpath("(//button[@type='button' and @data-booking-number='9920' ])")).click();
    try {
      assertTrue(driver.findElement(By.xpath("(//button[@type='button' and @data-popin='#popin-QTIPYP-247746380'])")).isDisplayed());
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    driver.findElement(By.xpath("(//button[@type='button' and @data-booking-number='9920' ])")).click();
    //Se déconnecter
    driver.findElement(By.xpath("(//li[@id='logout-menu-left-item']/button)")).click();
    try {
      assertTrue(driver.findElement(By.xpath("(//div[@id='home'])")).isDisplayed());
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    assertTrue(isElementPresent(By.xpath("//div[@id='logout-message']/div/p")));
    try {
      assertTrue(driver.findElement(By.xpath("//div[@id='logout-message']/div/p")).isDisplayed());
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    LOGGER.info("verificationErrorString : "+verificationErrorString);
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}
