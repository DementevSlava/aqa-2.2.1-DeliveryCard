import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;

public class DeliveryCardTest {
    LocalDate today = LocalDate.now();
    LocalDate newDate = today.plusDays(3);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    SelenideElement form = $("form[class='form form_size_m form_theme_alfa-on-white']");

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @Test
    void shouldSubmitRequest() {

        form.$("[placeholder='Город']").setValue("Санкт-Петербург");
        form.$("[placeholder='Дата встречи']").doubleClick().sendKeys(formatter.format(newDate));
        form.$("[data-test-id=name] input").setValue("Имя Фамилия");
        form.$("[data-test-id=phone]  input").setValue("+79101234567");
        form.$(".checkbox__box").click();
        $$(".button__content").find(exactText("Забронировать")).click();
        $(withText("Успешно")).waitUntil(visible, 15000);
    }

    @Test
    void shouldSubmitRequestWithDropDownList() {
        $("[placeholder='Город']").setValue("Санкт-Петербург");
        $(".menu-item__control").click();
        $("[placeholder='Дата встречи']").click();
        $(".calendar__day_state_current").click();
        $("[name=name]").setValue("Имя Фамилия");
        $("[name=phone]").setValue("+79111234567");
        $(".checkbox__box").click();
        $$(".button__content").find(exactText("Забронировать")).click();
        $(withText("Успешно")).waitUntil(visible, 15000);
    }

    @Test
    void shouldSubmitWithIncorrectCity() {
        form.$("[placeholder='Город']").setValue("Вашингтон");
        form.$("[placeholder='Дата встречи']").doubleClick().sendKeys(formatter.format(newDate));
        form.$("[name=name]").setValue("Имя Фамилия");
        form.$("[name=phone]").setValue("+79101234567");
        form.$(".checkbox__box").click();
        $$(".button__content").find(exactText("Забронировать")).click();
        form.$(".input_theme_alfa-on-white.input_invalid .input__sub")
                .shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldSubmitWithIncorrectDate() {
        LocalDate newDate = today.plusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        form.$("[placeholder='Город']").setValue("Рязань");
        form.$("[placeholder='Дата встречи']").doubleClick().sendKeys(formatter.format(newDate));
        form.$("[name=name]").setValue("Имя Фамилия");
        form.$("[name=phone]").setValue("+79119874123");
        form.$(".checkbox__box").click();
        $$(".button__content").find(exactText("Забронировать")).click();
        form.$(".input_theme_alfa-on-white.input_invalid .input__sub")
                .shouldHave(exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    void shouldSubmitWithIncorrectName() {
        form.$("[placeholder='Город']").setValue("Москва");
        form.$("[placeholder='Дата встречи']").doubleClick().sendKeys(formatter.format(newDate));
        form.$("[name=name]").setValue("Name Surname");
        form.$("[name=phone]").setValue("+79815463321");
        form.$(".checkbox__box").click();
        $$(".button__content").find(exactText("Забронировать")).click();
        form.$(".input_theme_alfa-on-white.input_invalid .input__sub")
                .shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldSubmitWithIncorrectNumber() {
        form.$("[placeholder='Город']").setValue("Санкт-Петербург");
        form.$("[placeholder='Дата встречи']").doubleClick().sendKeys(formatter.format(newDate));
        form.$("[name=name]").setValue("Имя Фамилия");
        form.$("[name=phone]").setValue("89217896541");
        form.$(".checkbox__box").click();
        $$(".button__content").find(exactText("Забронировать")).click();
        form.$(".input_theme_alfa-on-white.input_invalid .input__sub")
                .shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldSubmitUseCalendarTool() {
        LocalDate week = today.plusDays(7);
        String dayOfMonth = String.valueOf(week.getDayOfMonth());
        form.$("[placeholder='Город']").setValue("Санкт-Петербург");
        form.$("[data-test-id=date]").click();
        $$("[role='gridcell']")
                .find(exactText(dayOfMonth)).click();
        form.$("[data-test-id=name] input").setValue("Имя Фамилия");
        form.$("[data-test-id=phone] input").setValue("+79101234567");
        form.$(".checkbox__box").click();
        $$(".button__content").find(exactText("Забронировать")).click();
        $(withText("Успешно")).waitUntil(visible, 15000);
    }
}
