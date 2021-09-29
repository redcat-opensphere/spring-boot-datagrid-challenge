package com.redhat.developergames;

import javax.servlet.http.HttpSession;

import org.infinispan.spring.remote.session.configuration.EnableInfinispanRemoteHttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.redhat.developergames.model.Weather;
import com.redhat.developergames.repository.WeatherRepository;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
@RestController
@EnableCaching
@EnableInfinispanRemoteHttpSession
public class WeatherApp {

	@Autowired
	WeatherRepository weatherRepository;

	@GetMapping("/")
	public String index() {
		return "Greetings from Spring Boot with Data Grid!";
	}

	@ApiOperation("getByLocation")
	@GetMapping("/weather/{location}")
	public Object getByLocation(@PathVariable String location, HttpSession session) {
		Weather weather = weatherRepository.getByLocation(location);
		if (weather == null) {
			return String.format("Weather for location %s not found", location);
		}

		session.setAttribute("latest", location);
		return weather;
	}

	@ApiOperation("latestLocation")
	@GetMapping("/latest")
	public String latestLocation(HttpSession session) {

		String latest = (String) session.getAttribute("latest");

		return StringUtils.isEmpty(latest)
				? "No latest location stored yet, plese use 'getByLocation' method at least once"
				: latest;
	}

	public static void main(String... args) {
		new SpringApplicationBuilder().sources(WeatherApp.class).run(args);
	}


}
