package com.adonis;

import com.adonis.data.persons.Person;
import com.adonis.utils.DatabaseUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Locale;
import java.util.TimeZone;

import static com.adonis.install.InstallConstants.DEFAULT_TIMEZONE;
import static com.adonis.install.InstallConstants.INSTALL;
import static com.adonis.install.VehicleManagerInstaller.createShortcat;

@Configuration
@ComponentScan(value = "com.adonis")
@EnableAutoConfiguration
@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = Person.class)
public class VehiclyManagerApplication {

	public static void main(String[] args) {

		TimeZone.setDefault(TimeZone.getTimeZone(DEFAULT_TIMEZONE));
		Locale.setDefault(new Locale("lv", "LV", "Latvia"));

		if(args!=null && args.length>0 && args[0].equals("install") ){
			INSTALL = true;
		}else{
			INSTALL = false;
		}
		if( INSTALL ) createShortcat();
		try {
			if( INSTALL ) DatabaseUtils.createDatabase();
		} catch (Exception e) {
			e.printStackTrace();
		}

		SpringApplication.run(VehiclyManagerApplication.class, args);
	}
}
