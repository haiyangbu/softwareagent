import java.util.Vector;

enum ROBOSTATE {
	READY,
	PLAYING,
	PUNISHING,
	STATE_MAX
}

public abstract class State {
	
	public State() {
		m_punish_flag = false;
	}

	abstract void see(Memory mem, SendCommand krislet);
	abstract void next(State state);
	abstract void next();
	
	void hear(String msg)
	{
		if (msg.contains("goal_kick") ||
				msg.contains("free_kick") ||
				msg.contains("kick_in") ||
				msg.contains("corner_kick")) {
			
			if (m_state == ROBOSTATE.PLAYING)
			{
				m_sm.next();
			}
			m_punish_flag = true;
			m_sm.setState(ROBOSTATE.PUNISHING);
			m_sm.hear(msg);
		}
	}
	
	void push_action(String act)
	{
		m_action_history.addElement(act);
		if (m_action_history.size() > 3)
		{
			pop_action();
		}
	}
	
	void pop_action()
	{
		m_action_history.removeElementAt(0);
	}

	void clean_action_histroy()
	{
		m_action_history.removeAllElements();
	}

	void set_last_state(ROBOSTATE state)
	{
		m_laststate = state;
	}
	
	ROBOSTATE get_last_state()
	{
		return m_laststate;
	}

	ROBOSTATE get_current_state()
	{
		return m_state;
	}

	String get_last_state_name()
	{
		switch (m_laststate)
		{
		case READY:
			return "READY";
		case PLAYING:
			return "PLAYING";
		case PUNISHING:
			return "PUNISHING";
		default:;
		}
		return "NUL";
	}

	String get_current_state_name()
	{
		switch (m_state)
		{
		case READY:
			return "READY";
		case PLAYING:
			return "PLAYING";
		case PUNISHING:
			return "PUNISHING";
		default:;
		}
		return "NUL";
	}

	ROBOSTATE m_state = ROBOSTATE.READY; //current state
	ROBOSTATE m_laststate = ROBOSTATE.READY; //previous state
	StateMachine m_sm;
	boolean m_punish_flag;
	Vector<String> m_action_history;

}
