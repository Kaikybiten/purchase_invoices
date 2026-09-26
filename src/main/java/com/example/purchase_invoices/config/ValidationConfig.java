
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class ValidationConfig {

    private static ReloadableResourceBundleMessageSource messageSource() {

        // Objeto fonte das mensagens
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();

        // Codificação das mensagens
        messageSource.setDefaultEncoding("UTF-8");
        // Arquivo com as variaveis referenciando as mensagens de validação
        messageSource.setBasename("classpath:ValidationMessages");

        return messageSource; // Retornando fonte das mensagens já configurada
    }

    @Bean
    public LocalValidatorFactoryBean localValidatorFactoryBean() {

        // Objeto que lê as anotações de validação (@NotBlank, @Max, @NotNull, @Positive)
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();

        // Definindo fonte das mensagens de retorno do validador
        localValidatorFactoryBean.setValidationMessageSource(messageSource());

        return localValidatorFactoryBean;
    }
}
