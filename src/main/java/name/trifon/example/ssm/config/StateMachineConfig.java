package name.trifon.example.ssm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.action.SpelExpressionAction;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import name.trifon.example.ssm.Events;
import name.trifon.example.ssm.States;

@Configuration
@EnableStateMachine
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<States, Events> {

	@Override
	public void configure(StateMachineConfigurationConfigurer<States, Events> config) throws Exception {
		config.withConfiguration().autoStartup(true).listener(listener());
	}

	@Override
	public void configure(StateMachineStateConfigurer<States, Events> states) throws Exception {
		states.withStates()
			.initial(States.SI)
//			.states(EnumSet.allOf(States.class))
			.state(States.S1, entryAction1(), exitAction1())
			.state(States.S2, entryAction2(), exitAction2())
			.state(States.S3, entryAction3(), exitAction3())
		;
	}
	@Override
	public void configure(StateMachineTransitionConfigurer<States, Events> transitions) throws Exception {
		transitions
		.withExternal()
			.source(States.SI).target(States.S1)
			.event(Events.E1)
			.guard(guard1())
			.and()
		.withExternal()
			.source(States.S1).target(States.S2)
			.event(Events.E2)
			.guard(guard2())
			.and()
		.withExternal()
			.source(States.S2).target(States.S3)
			.event(Events.E3)
//			.guardExpression("extendedState.variables.get('myvar')")
		;
	}

	@Bean
	public StateMachineListener<States, Events> listener() {
		return new StateMachineListenerAdapter<States, Events>() {
			@Override
			public void stateChanged(State<States, Events> from, State<States, Events> to) {
				System.out.println("State change to " + to.getId());
			}
		};
	}

	//========
	//   Guards
	// - https://docs.spring.io/spring-statemachine/docs/current/reference/#sm-guards
	@Bean
	public Guard<States, Events> guard1() {
		return new Guard<States, Events>() {

			public boolean evaluate(StateContext<States, Events> context) {
				System.out.println("Trifon - Guard 1");
				return true;
			}
		};
	}

	@Bean
	public BaseGuard guard2() {
		return new BaseGuard();
	}

	public class BaseGuard implements Guard<States, Events> {

		public boolean evaluate(StateContext<States, Events> context) {
			System.out.println("Trifon - Guard 2");
			return true;
		}
	}

	//===========
	//   Actions
	// - https://docs.spring.io/spring-statemachine/docs/current/reference/#sm-actions
	@Bean
	public Action<States, Events> entryAction1() {
		return new Action<States, Events>() {

			public void execute(StateContext<States, Events> context) {
				System.out.println("TRIFON - Entry action1()");
			}
		};
	}

	@Bean
	public Action<States, Events> entryAction2() {
		return entryAction("2");
	}
	@Bean
	public Action<States, Events> entryAction3() {
		return entryAction("3");
	}
	public Action<States, Events> entryAction(final String name) {
		return new Action<States, Events>() {

			public void execute(StateContext<States, Events> context) {
				System.out.println("TRIFON - Entry action"+name+"()");
			}
		};
	}

	public Action<States, Events> exitAction(final String name) {
		return new Action<States, Events>() {

			public void execute(StateContext<States, Events> context) {
				System.out.println("TRIFON - Exit action"+name+"()");
			}
		};
	}
	@Bean
	public Action<States, Events> exitAction1() {
		return exitAction("1");
	}
	@Bean
	public Action<States, Events> exitAction2() {
		return exitAction("2");
	}

	@Bean
	public BaseAction exitAction3() {
		return new BaseAction();
	}

	@Bean
	public SpelAction action3() {
		ExpressionParser parser = new SpelExpressionParser();
		return new SpelAction(
			parser.parseExpression("stateMachine.sendEvent(T(name.trifon.example.ssm.Events).E2)"));
	}

	public class BaseAction implements Action<States, Events> {

		public void execute(StateContext<States, Events> context) {
			System.out.println("TRIFON - baseAction()");
		}
	}

	public class SpelAction extends SpelExpressionAction<States, Events> {

		public SpelAction(Expression expression) {
			super(expression);
		}
	}
}