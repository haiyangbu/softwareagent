import java.util.Random;

public class State_Punish extends State {
	
	final float offside_kick_margin = (float) 9.15;
	Memory m_memory;
	float m_direction;
	float m_runpower;
	
	public State_Punish(StateMachine sm) {
		m_sm = sm;
		m_state = ROBOSTATE.PUNISHING;
	}

	@Override
	void see(Memory mem, SendCommand krislet) {
		// TODO Auto-generated method stub
		m_memory = mem;
		
		//use the pause time to adjust position.
		m_sm.m_krislet.dash(m_runpower);
	}

	@Override
	void hear(String msg) {
		// TODO Auto-generated method stub
		if (msg.contains("play_on")) {
			
			m_punish_flag = false;
			m_sm.setState(ROBOSTATE.PLAYING);
		}
		else {
			if (msg.contains("free_kick_") ||
				msg.contains("corner_kick_") ||
				msg.contains("goal_kick_") ||
				msg.contains("offside_")) {

				if (msg.charAt(msg.length() - 1) == 
						m_sm.m_krislet.getMyside()) {
					System.out.println("My kick please.........");
					m_punish_flag = false;
					m_sm.setState(ROBOSTATE.PLAYING);
				}
				else
					System.out.println("You kick please.........");

			}
		}
	}

	@Override
	void next() {
		
		int max=40;
        int min=10;
		Random random = new Random();
		
		m_sm.m_krislet.turn(random.nextInt(max)%(max-min+1) + min);
		m_runpower = (float) (20*Math.random());

	}

	@Override
	void next(State state) {
		// TODO Auto-generated method stub

	}

}
