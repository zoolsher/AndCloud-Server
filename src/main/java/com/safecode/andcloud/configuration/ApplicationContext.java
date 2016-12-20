package com.safecode.andcloud.configuration;

import org.libvirt.Connect;
import org.libvirt.LibvirtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
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
public class ApplicationContext {

    private final static Logger logger = LoggerFactory.getLogger(ApplicationContext.class);

    @Autowired
    private Environment environment;

    @Bean
    public Connect libvirtConnect() throws LibvirtException {
        Connect conn = new Connect(environment.getProperty("libvirt.endpoint"));
        return conn;
    }

}
