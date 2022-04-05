package name.trifon.example.ssm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

@Component
public class MyCommandLineRunner implements CommandLineRunner {

	@Autowired
	private StateMachine<States, Events> stateMachine;


	public void run(String... args) throws Exception {
	    stateMachine.sendEvent(Events.E1);
	    stateMachine.sendEvent(Events.E1);
	    stateMachine.sendEvent(Events.E1);
	    
	    stateMachine.sendEvent(Events.E2);
	    stateMachine.sendEvent(Events.E2);

	    stateMachine.sendEvent(Events.E3);
	    stateMachine.sendEvent(Events.E3);

	}

}
