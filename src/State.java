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

	ROBOSTATE m_state;
	StateMachine m_sm;
	boolean m_punish_flag;
	Vector<String> m_action_history;

}
