package com.safecode.andcloud.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Spring Context configuration class.
 *
 * @author Sumy
 */
@Configuration
@EnableTransactionManagement
@PropertySource(value = {"classpath:application.properties"})
@ComponentScan(basePackages = "com.safecode.andcloud.*")
public class SpringAppContext {

    private final static Logger logger = LoggerFactory.getLogger(SpringAppContext.class);

}
