
class Status {
	
	public Status() {
	}
	
}

public class StateMachine {

	public StateMachine(Brain brain, SendCommand krislet) {

		System.out.println("StateMachine created.");
		m_stateReady = new State_Ready(this);
		m_statePlay = new State_Play(this);
		m_statePunish = new State_Punish(this);
		m_brain = brain;
		m_krislet = krislet;
		setState(m_stateReady);
	}
	
	private State m_state;
	private State m_stateReady;
	private State m_statePlay;
	private State m_statePunish;
	private Brain m_brain;
	protected SendCommand m_krislet;


	public void setState(State state) {
		ROBOSTATE st = ROBOSTATE.STATE_MAX;
		
		if (m_state == state)
			return;

		if (m_state != null)
			st = m_state.get_current_state();
		else
			st = ROBOSTATE.READY;
		m_state = state;
		m_state.set_last_state(st);
		m_state.next();
		System.out.format("Last State: %s, Current State: %s\n\r",
				m_state.get_last_state_name(), m_state.get_current_state_name());
	}
	
	public void setState(ROBOSTATE state) {
		
		switch (state) {
		
		case READY:
			setState(m_stateReady);
			break;
		case PLAYING:
			setState(m_statePlay);
			break;
		case PUNISHING:
			setState(m_statePunish);
			break;
		default:;
		}
	}
	
	public void see(Memory mem) {
		//System.out.println("StateMachine see:" + m_state.get_current_state_name());

		m_state.see(mem, m_krislet);

	}
	
	public void hear(String msg) {
		
		if (msg.contains("play_on") ||
				msg.contains("kick_off_")) {
			setState(ROBOSTATE.PLAYING);
		}
		else if (msg.contains("goal_")) {
			setState(ROBOSTATE.READY);
		}
		
		m_state.hear(msg);
	}
	
	public void body_sense(String msg)
	{

	}
	
	public void next() {
		m_state.next();
	}

	public void next(State state) {
		m_state.next(state);
	}
	
	public void moveback() {
		m_brain.moveback();
	}
	
	public char getside() {
		return m_brain.m_global_param.m_side;
	}
}
