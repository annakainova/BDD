package ru.netology.web.test;

import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.LoginPageV1;
import ru.netology.web.page.LoginPageV2;
import ru.netology.web.page.LoginPageV3;

import static com.codeborne.selenide.Selenide.open;

class MoneyTransferTest {
    @Test
    void shouldTransferMoneyBetweenOwnCardsV1() {
        open("http://localhost:9999");
        var loginPage = new LoginPageV1();
//    var loginPage = open("http://localhost:9999", LoginPageV1.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
    }

    @Test
    void shouldTransferMoneyBetweenOwnCardsV2() {
        open("http://localhost:9999");
        var loginPage = new LoginPageV2();
//    var loginPage = open("http://localhost:9999", LoginPageV2.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
    }

    @Test
    void shouldTransferMoneyBetweenOwnCardsV3() {
        var loginPage = open("http://localhost:9999", LoginPageV3.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
    }

    @Test
        //check if we press cancel balances are not changed
    void shouldCancelTransfer() {
        var loginPage = open("http://localhost:9999", LoginPageV2.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);
        var transactionPage = dashboardPage.getTransactionPage(DataHelper.getFirstCardInfo().getCardID());
        transactionPage.cancelTransaction();
        dashboardPage.checkBalance(DataHelper.getFirstCardInfo().getCardID(), DataHelper.getBaseBalance().getBalanceForFirstCard());
        dashboardPage.checkBalance(DataHelper.getSecondCardInfo().getCardID(), DataHelper.getBaseBalance().getBalanceForSecondCard());
    }

    @Test
        //check if we try to make transaction from the first card to the first card balances are not changed
    void shouldNotTransferTheSameCard() {
        var loginPage = open("http://localhost:9999", LoginPageV2.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);
        var transactionPage = dashboardPage.getTransactionPage(DataHelper.getFirstCardInfo().getCardID());
        var enoughAmount = DataHelper.getLessMoneyThanBalance();
        transactionPage.doTransaction(DataHelper.getFirstCardInfo().getCardNumber(), enoughAmount.getAmount());
        dashboardPage.checkBalance(DataHelper.getFirstCardInfo().getCardID(), DataHelper.getBaseBalance().getBalanceForFirstCard());
        dashboardPage.checkBalance(DataHelper.getSecondCardInfo().getCardID(), DataHelper.getBaseBalance().getBalanceForSecondCard());
    }

    @Test
        //check if we make transaction and we have enough money transaction will happen
    void shouldTransferMoneyLessThanBalance() {
        var loginPage = open("http://localhost:9999", LoginPageV2.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);
        var transactionPage = dashboardPage.getTransactionPage(DataHelper.getFirstCardInfo().getCardID());
        var enoughAmount = DataHelper.getLessMoneyThanBalance();
        transactionPage.doTransaction(DataHelper.getSecondCardInfo().getCardNumber(), enoughAmount.getAmount());
        dashboardPage.checkBalance(DataHelper.getFirstCardInfo().getCardID(), DataHelper.getBalanceAfterSuccessfulTransaction().getBalanceForFirstCard());
        dashboardPage.checkBalance(DataHelper.getSecondCardInfo().getCardID(), DataHelper.getBalanceAfterSuccessfulTransaction().getBalanceForSecondCard());
        //return everything to original state
        dashboardPage.getTransactionPage(DataHelper.getSecondCardInfo().getCardID());
        transactionPage.doTransaction(DataHelper.getFirstCardInfo().getCardNumber(), enoughAmount.getAmount());
    }

  @Test
    //check if we make transaction and we have enough money transaction will happen
  void shouldTransferAllBalance() {
    var loginPage = open("http://localhost:9999", LoginPageV2.class);
    var authInfo = DataHelper.getAuthInfo();
    var verificationPage = loginPage.validLogin(authInfo);
    var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
    var dashboardPage = verificationPage.validVerify(verificationCode);
    var transactionPage = dashboardPage.getTransactionPage(DataHelper.getFirstCardInfo().getCardID());
    var amount = DataHelper.getAllBalance();
    transactionPage.doTransaction(DataHelper.getSecondCardInfo().getCardNumber(), amount.getAmount());
    dashboardPage.checkBalance(DataHelper.getFirstCardInfo().getCardID(), DataHelper.getBalanceAfterSuccessfulTransactionAllBalance().getBalanceForFirstCard());
    dashboardPage.checkBalance(DataHelper.getSecondCardInfo().getCardID(), DataHelper.getBalanceAfterSuccessfulTransactionAllBalance().getBalanceForSecondCard());
    //return everything to original state
    dashboardPage.getTransactionPage(DataHelper.getSecondCardInfo().getCardID());
    transactionPage.doTransaction(DataHelper.getFirstCardInfo().getCardNumber(), amount.getAmount());
  }

    @Test
      //check if we make transaction and we do not have enough money transaction will not happen
    void shouldNotTransferMoneyMoreThanBalance() {
        var loginPage = open("http://localhost:9999", LoginPageV2.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);
        var transactionPage = dashboardPage.getTransactionPage(DataHelper.getSecondCardInfo().getCardID());
        var notEnoughAmount = DataHelper.getMoreMoneyThanBalance();
        transactionPage.doTransaction(DataHelper.getFirstCardInfo().getCardNumber(), notEnoughAmount.getAmount());
        dashboardPage.checkBalance(DataHelper.getFirstCardInfo().getCardID(), DataHelper.getBaseBalance().getBalanceForFirstCard());
        dashboardPage.checkBalance(DataHelper.getSecondCardInfo().getCardID(), DataHelper.getBaseBalance().getBalanceForSecondCard());
    }
}

