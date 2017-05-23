import java.util.Vector;

public class State_Play extends State {
	
	
	public State_Play(StateMachine sm) {
		m_sm = sm;
		m_action_history = new Vector<String>(1);
	}
	
	
	//to avoid action sequences such as: kick -> dash -> kick
	//the purpose is to avoid commit fault.
	boolean is_new_action_league(String act)
	{
		if (m_action_history.size() < 3)
			return true;
		
		for (int i = m_action_history.size() - 1; i >= 0; i --)
		{
			if (m_action_history.get(1).equals("kick") &&
					m_action_history.get(2).equals("dash") &&
					act.equals("kick"))
			{
				return false;
			}
		}
		return true;
	}
	
	//For a state machine agent, it will not just find the ball and kick it.
	//Before kicking or dashing, an league checking will be done to avoid committing fault.
	@Override
	void see(Memory mem, SendCommand krislet)
	{

		ObjectInfo ball;
		ball = mem.getObject("ball");
		PlayerInfo player;
		player = (PlayerInfo)mem.getObject("player");
		GoalInfo goal;


		if( ball == null )
	    {
			// If you don't know where is ball then find it
			push_action("turn");
			krislet.turn(40);
			mem.waitForNewInfo();
	    }
		else if( ball.m_distance > 1.0 )
	    {
			// If ball is too far then
			// turn to ball or 
			// if we have correct direction then go to ball
			if( ball.m_direction != 0 )
			{
				push_action("turn");
			    krislet.turn(ball.m_direction);
			}
			else 
			{
				if (is_new_action_league("dash"))
				{
					push_action("dash");
				    krislet.dash(20*ball.m_distance);
				}
			}
	    }
		else 
	    {
			// We know where is ball and we can kick it
			// so look for goal
			if( m_sm.getside() == 'l' )
				goal = (GoalInfo)mem.getObject("goal r");
			else
				goal = (GoalInfo)mem.getObject("goal l");
	
			if( ball == null )
		    {
				krislet.turn(40);
				push_action("turn");
				mem.waitForNewInfo();
		    }
			else
			{
				if (is_new_action_league("kick"))
				{
					push_action("kick");
					double dir_off = 0;
					
					if(player != null)
						System.out.println("player:" + player.m_direction);
					if(goal != null)
						System.out.println("goal:" + goal.m_direction);
				    //m_krislet.kick(m_brain.m_global_param.m_kick_power, object.m_direction);
				    if (player != null && goal != null)
						if ((player.m_direction - goal.m_direction) < 5)
					    {
					    	dir_off = 35;
					    	System.out.println("get off direction......");
					    	
					    }
				    
				    if (goal != null)
				    	krislet.kick(40, goal.m_direction + dir_off);
				    else 
				    	krislet.turn(40);
				}
			}
	    }
		
		//
		
		/*if (player != null)
		{
			System.out.println("player info:" + player.m_type +
					",team:" + ((PlayerInfo)player).m_teamName +
					", uniform:" + ((PlayerInfo)player).m_uniformName);
		}*/
	}

	@Override
	void hear(String msg) {
		// TODO Auto-generated method stub
		
		if (msg.contains("kick_in") ||
				msg.contains("free_kick_") ||
				msg.contains("corner_kick_") ||
				msg.contains("goal_kick_") ||
				msg.contains("offside_")) {
			
			m_sm.setState(ROBOSTATE.PUNISHING);
		}

	}

	@Override
	void next() {
		clean_action_histroy();
	}

	@Override
	void next(State state) {
		// TODO Auto-generated method stub
		clean_action_histroy();
	}

}
