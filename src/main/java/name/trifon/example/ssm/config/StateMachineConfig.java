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

import name.trifon.example.ssm.ExampleEvent;
import name.trifon.example.ssm.ExampleState;

@Configuration
@EnableStateMachine
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<ExampleState, ExampleEvent> {

	@Override
	public void configure(StateMachineConfigurationConfigurer<ExampleState, ExampleEvent> config) throws Exception {
		config.withConfiguration().autoStartup(true).listener(listener());
	}

	@Override
	public void configure(StateMachineStateConfigurer<ExampleState, ExampleEvent> states) throws Exception {
		states.withStates()
			.initial(ExampleState.SI)
//			.states(EnumSet.allOf(ExampleState.class))
			.state(ExampleState.S1, entryAction1(), exitAction1())
			.state(ExampleState.S2, entryAction2(), exitAction2())
			.state(ExampleState.S3, entryAction3(), exitAction3())
		;
	}
	@Override
	public void configure(StateMachineTransitionConfigurer<ExampleState, ExampleEvent> transitions) throws Exception {
		transitions
		.withExternal()
			.source(ExampleState.SI).target(ExampleState.S1)
			.event(ExampleEvent.E1)
			.guard(guard1())
			.and()
		.withExternal()
			.source(ExampleState.S1).target(ExampleState.S2)
			.event(ExampleEvent.E2)
			.guard(guard2())
			.and()
		.withExternal()
			.source(ExampleState.S2).target(ExampleState.S3)
			.event(ExampleEvent.E3)
//			.guardExpression("extendedState.variables.get('myvar')")
		;
	}

	@Bean
	public StateMachineListener<ExampleState, ExampleEvent> listener() {
		return new StateMachineListenerAdapter<ExampleState, ExampleEvent>() {
			@Override
			public void stateChanged(State<ExampleState, ExampleEvent> from, State<ExampleState, ExampleEvent> to) {
				System.out.println("State change to " + to.getId());
			}
		};
	}

	//========
	//   Guards
	// - https://docs.spring.io/spring-statemachine/docs/current/reference/#sm-guards
	@Bean
	public Guard<ExampleState, ExampleEvent> guard1() {
		return new Guard<ExampleState, ExampleEvent>() {

			public boolean evaluate(StateContext<ExampleState, ExampleEvent> context) {
				System.out.println("Trifon - Guard 1");
				return true;
			}
		};
	}

	@Bean
	public BaseGuard guard2() {
		return new BaseGuard();
	}

	public class BaseGuard implements Guard<ExampleState, ExampleEvent> {

		public boolean evaluate(StateContext<ExampleState, ExampleEvent> context) {
			System.out.println("Trifon - Guard 2");
			return true;
		}
	}

	//===========
	//   Actions
	// - https://docs.spring.io/spring-statemachine/docs/current/reference/#sm-actions
	@Bean
	public Action<ExampleState, ExampleEvent> entryAction1() {
		return new Action<ExampleState, ExampleEvent>() {

			public void execute(StateContext<ExampleState, ExampleEvent> context) {
				System.out.println("TRIFON - Entry action1()");
			}
		};
	}

	@Bean
	public Action<ExampleState, ExampleEvent> entryAction2() {
		return entryAction("2");
	}
	@Bean
	public Action<ExampleState, ExampleEvent> entryAction3() {
		return entryAction("3");
	}
	public Action<ExampleState, ExampleEvent> entryAction(final String name) {
		return new Action<ExampleState, ExampleEvent>() {

			public void execute(StateContext<ExampleState, ExampleEvent> context) {
				System.out.println("TRIFON - Entry action"+name+"()");
			}
		};
	}

	public Action<ExampleState, ExampleEvent> exitAction(final String name) {
		return new Action<ExampleState, ExampleEvent>() {

			public void execute(StateContext<ExampleState, ExampleEvent> context) {
				System.out.println("TRIFON - Exit action"+name+"()");
			}
		};
	}
	@Bean
	public Action<ExampleState, ExampleEvent> exitAction1() {
		return exitAction("1");
	}
	@Bean
	public Action<ExampleState, ExampleEvent> exitAction2() {
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

	public class BaseAction implements Action<ExampleState, ExampleEvent> {

		public void execute(StateContext<ExampleState, ExampleEvent> context) {
			System.out.println("TRIFON - baseAction()");
		}
	}

	public class SpelAction extends SpelExpressionAction<ExampleState, ExampleEvent> {

		public SpelAction(Expression expression) {
			super(expression);
		}
	}
}