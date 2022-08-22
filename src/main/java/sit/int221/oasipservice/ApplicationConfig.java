package sit.int221.oasipservice;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import sit.int221.oasipservice.utils.ListMapper;

@Configuration
@EnableJpaAuditing
public class ApplicationConfig {
    @Bean
    public ListMapper listMapper() {
        return ListMapper.getInstance();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
