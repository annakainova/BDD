package ru.netology.web.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebElement;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private SelenideElement heading = $("[data-test-id=dashboard]");

    private ElementsCollection cards = $$(".list__item div");
    private SelenideElement reloadButton = $("[data-test-id=action-reload]");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";

    public DashboardPage() {
        heading.shouldBe(visible);
    }

    public TransactionPage getTransactionPage(String id) {

        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).getAttribute("data-test-id").equals(id)) {
                cards.get(i).find(".button__text").click();
                break;
            }
        }
        return new TransactionPage();
    }

    public int getCardBalance(String id) {
        int balance = 0;
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).getAttribute("data-test-id").equals(id)) {
                balance = extractBalance(cards.get(i).text());
                break;
            }
        }
        return balance;
    }

    public void checkBalance(String id, int expectedBalance) {
        int actualBalance = getCardBalance(id);
        Assertions.assertEquals(expectedBalance, actualBalance);
    }

    private int extractBalance(String text) {
        var start = text.indexOf(balanceStart);
        var finish = text.indexOf(balanceFinish);
        var value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }
}
