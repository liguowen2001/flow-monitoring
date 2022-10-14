package plus.liguowen.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import plus.liguowen.api.controller.Flow;

@SpringBootApplication
public class ApiApplication {

	public static void main(String[] args) {
		Flow.main();
		//SpringApplication.run(ApiApplication.class, args);
	}

}
