package name.trifon.example.ssm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExampleSpringStatemachineApplication {

	private static Logger log = LoggerFactory.getLogger(ExampleSpringStatemachineApplication.class);

	public static void main(String[] args) {
		log.info("STARTING THE APPLICATION");
		SpringApplication.run(ExampleSpringStatemachineApplication.class, args);
		log.info("APPLICATION FINISHED");
	}

}
