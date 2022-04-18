package name.trifon.example.ssm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;


@Component
public class MyCommandLineRunner implements CommandLineRunner {

	@Autowired
	private StateMachine<ExampleState, ExampleEvent> stateMachine;


	public void run(String... args) throws Exception {
		String orderId = "ORD_101";
		
		Message<ExampleEvent> msg = MessageBuilder.withPayload(ExampleEvent.E1)
				.setHeader("X-SM-HEADER", orderId)
				.build();
		stateMachine.sendEvent(msg);
//		stateMachine.sendEvent(ExampleEvent.E1);
//	    stateMachine.sendEvent(ExampleEvent.E1);
//	    stateMachine.sendEvent(ExampleEvent.E1);
	    
	    stateMachine.sendEvent(ExampleEvent.E2);
	    stateMachine.sendEvent(ExampleEvent.E2);

	    stateMachine.sendEvent(ExampleEvent.E3);
	    stateMachine.sendEvent(ExampleEvent.E3);

	}

}
