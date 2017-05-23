
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
	String m_bodysense;

	
	public void setState(State state) {
		m_state = state;
		m_state.next();
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
		//System.out.println("StateMachine see");

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
		//debug
		if (true) {
			String tmp = msg;
			m_bodysense = tmp.substring(0, tmp.lastIndexOf(')') + 2);
			//debug end
		}

	}
	
	public void printbody()
	{
		System.out.println("-- body:" + m_bodysense);
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
