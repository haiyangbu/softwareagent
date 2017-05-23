
public class State_Ready extends State {
	
	public State_Ready(StateMachine sm) {
		m_sm = sm;
	}

	@Override
	void see(Memory mem, SendCommand krislet) {

		ObjectInfo object;
		object = mem.getObject("ball");

		if( object == null )
	    {
			// If you don't know where is ball then find it
			krislet.turn(40);
			mem.waitForNewInfo();
	    }
		else
	    {
			if( object.m_direction != 0 ) 
			{
			    krislet.turn(object.m_direction);
			}

	    }
	}

	@Override
	void hear(String msg) {
		
		if (msg.contains("goal_")) {
			m_sm.moveback();
		}
		else if (msg.contains("play_on") ||
				msg.contains("kick_off_")) {
			
			m_sm.setState(ROBOSTATE.PLAYING);
		}

	}

	@Override
	void next() {

	}

	@Override
	void next(State state) {

	}
}
