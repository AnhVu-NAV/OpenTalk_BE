package sba301.java.opentalk.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.List;
import java.util.Locale;

@Configuration
public class I18nConfig {

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public AcceptHeaderLocaleResolver localeResolver() {
        return new CustomLocaleResolver();
    }

    static class CustomLocaleResolver extends AcceptHeaderLocaleResolver {
        private static final List<Locale> SUPPORTED_LOCALES = List.of(new Locale("vi"));

        @Override
        public Locale resolveLocale(HttpServletRequest request) {
            String headerLang = request.getHeader("Accept-Language");
            if (headerLang == null || headerLang.isEmpty()) {
                return Locale.getDefault();
            }
            return SUPPORTED_LOCALES.stream()
                    .filter(locale -> locale.getLanguage().equals(headerLang))
                    .findFirst()
                    .orElse(Locale.getDefault());
        }
    }
}

