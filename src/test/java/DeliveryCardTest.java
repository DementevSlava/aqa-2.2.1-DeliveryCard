import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;

public class DeliveryCardTest {
    LocalDate today = LocalDate.now();
    LocalDate newDate = today.plusDays(3);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    SelenideElement form = $("form");

    @BeforeEach
    void setUp(){
        open("http://localhost:9999");
    }

    @Test
    void shouldSubmitRequest() {
        SelenideElement form = $("form[class='form form_size_m form_theme_alfa-on-white']");
        form.$("[placeholder='Город']").setValue("Санкт-Петербург");
        form.$("[placeholder='Дата встречи']").doubleClick().sendKeys(formatter.format(newDate));
        form.$("[data-test-id=name] input").setValue("Имя Фамилия");
        form.$("[data-test-id=phone]").setValue("+79815463321");
        form.$(".checkbox__box").click();
        $$(".button__content").find(exactText("Забронировать")).click();
        $(withText("Успешно")).waitUntil(visible, 15000);
    }

}
