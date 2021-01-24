package com.assignment.spring;

import com.fasterxml.classmate.TypeResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.data.domain.Pageable;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.AlternateTypeBuilder;
import springfox.documentation.builders.AlternateTypePropertyBuilder;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.AlternateTypeRuleConvention;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static springfox.documentation.schema.AlternateTypeRules.newRule;


@Configuration
@EnableSwagger2
public class SwaggerConfig implements WebMvcConfigurer {

   @Bean
   public Docket api() {
      return new Docket(DocumentationType.SWAGGER_2)

         .apiInfo(apiInfo())
         .select()
         .apis(RequestHandlerSelectors.basePackage(WeatherController.class.getPackage().getName()))
         .build();
   }

   @Override
   public void addResourceHandlers(ResourceHandlerRegistry registry) {
      registry.addResourceHandler("swagger-ui.html")
         .addResourceLocations("classpath:/META-INF/resources/");

      registry.addResourceHandler("/webjars/**")
         .addResourceLocations("classpath:/META-INF/resources/webjars/");
   }

   private ApiInfo apiInfo() {
      return new ApiInfoBuilder().title("Weather API")
         .description("LeasePlan coding-test solution by Bao Vu.")
         .version("1.0.0")
         .build();
   }

   @Bean
   public AlternateTypeRuleConvention pageableConvention(
      final TypeResolver resolver) {
      return new AlternateTypeRuleConvention() {

         @Override
         public int getOrder() {
            return Ordered.HIGHEST_PRECEDENCE;
         }

         @Override
         public List<AlternateTypeRule> rules() {
            return Collections.singletonList(
               newRule(resolver.resolve(Pageable.class), resolver.resolve(pageableMixin()))
            );
         }
      };
   }

   @Override
   public void addViewControllers(ViewControllerRegistry registry) {
      registry.addRedirectViewController("/", "/swagger-ui.html");
   }

   private Type pageableMixin() {
      return new AlternateTypeBuilder()
         .fullyQualifiedClassName(
            String.format("%s.generated.%s",
               Pageable.class.getPackage().getName(),
               Pageable.class.getSimpleName()))
         .withProperties(Arrays.asList(
            property(Integer.class, "page"),
            property(Integer.class, "size"),
            property(String.class, "sort")
         ))
         .build();
   }

   private AlternateTypePropertyBuilder property(Class<?> type, String name) {
      return new AlternateTypePropertyBuilder()
         .withName(name)
         .withType(type)
         .withCanRead(true)
         .withCanWrite(true);
   }
}
