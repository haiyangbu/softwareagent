//
//	File:			Brain.java
//	Author:		Krzysztof Langner
//	Date:			1997/04/28
//
//    Modified by:	Paul Marlow

//    Modified by:      Edgar Acosta
//    Date:             March 4, 2008

import java.lang.Math;
import java.util.regex.*;

class Brain extends Thread implements SensorInput
{
	protected Agent m_global_param;
	protected ReactiveComp m_reactive;
	protected Environment m_env;
	protected GloablePosition m_pos;
	public StateMachine m_statemachine;
	
    //---------------------------------------------------------------------------
    // This constructor:
    // - stores connection to krislet
    // - starts thread for this object
    public Brain(SendCommand krislet, 
		 String team, 
		 char side, 
		 int number, 
		 String playMode)
    {
    	System.out.println("Brain created.");
		m_timeOver = false;
		m_krislet = krislet;
		m_memory = new Memory();
		//m_team = team;
		// m_number = number;
		m_playMode = playMode;

		m_global_param = new Agent();
		m_global_param.m_side = side;
    	m_global_param.g_debug_on = false;
		m_env = new Environment();
    	if (m_env.m_xml.m_runningmode.equals("reactive"))
    	{
    		m_global_param.setRunmode(RUNNINGMODE.MODE_REACTIVE);
    		m_reactive = new ReactiveComp(this);
    	}
    	else {
    		m_global_param.setRunmode(RUNNINGMODE.MODE_STATE_MACHINE);
    		m_statemachine = new StateMachine(this, m_krislet);
    	}
		m_pos = new GloablePosition();
		//
		m_pos.x = (float) ((float)-Math.random()*52.5);
	    m_pos.y = (float) ((float)34 - Math.random()*68.0);
		//m_pos.x = -30;
		//m_pos.y = 10;
		start();
    }


    //---------------------------------------------------------------------------
    // This is main brain function used to make decision
    // In each cycle we decide which command to issue based on
    // current situation. the rules are:
    //
    //	1. If you don't know where is ball then turn right and wait for new info
    //
    //	2. If ball is too far to kick it then
    //		2.1. If we are directed towards the ball then go to the ball
    //		2.2. else turn to the ball
    //
    //	3. If we dont know where is opponent goal then turn wait 
    //				and wait for new info
    //
    //	4. Kick ball
    //
    //	To ensure that we don't send commands to often after each cycle
    //	we waits one simulator steps. (This of course should be done better)

    // ***************  Improvements ******************
    // Allways know where the goal is.
    // Move to a place on my side on a kick_off
    // ************************************************
    public void run()
    {
		ObjectInfo object;
	
		// first put it somewhere on my side
		if(Pattern.matches("^before_kick_off.*",m_playMode)) {
	
			m_krislet.move(m_pos.x, m_pos.y);
			m_krislet.changeView("wide", "high");
			System.out.println(" x:" + m_pos.x  + ",y:" + m_pos.y);
		}
	
		System.out.println("^^^^^^^^^^^^^^^before_kick_off");
		m_global_param.m_kick_power = 100;
		m_global_param.m_run_power = 80;
		m_global_param.m_kick_off = false;
		if (m_global_param.getRunmode() == RUNNINGMODE.MODE_STATE_MACHINE) 
		{
			m_statemachine.setState(ROBOSTATE.READY);
		}

		while( !m_timeOver )
		{
				
			if (m_global_param.getRunmode() == RUNNINGMODE.MODE_REACTIVE) 
			{
				m_reactive.see(m_memory, m_krislet);
			}
			else if (m_global_param.getRunmode() == RUNNINGMODE.MODE_STATE_MACHINE) 
			{
				m_statemachine.see(m_memory);
			}

			// sleep one step to ensure that we will not send
			// two commands in one cycle.
			try{
			    Thread.sleep(2*SoccerParams.simulator_step);
			}catch(Exception e){}
		}
		m_krislet.bye();
    }


    //===========================================================================
    // Here are suporting functions for implement logic


    //===========================================================================
    // Implementation of SensorInput Interface

    //---------------------------------------------------------------------------
    // This function sends see information
    public void see(VisualInfo info)
    {
	m_memory.store(info);
    }


    //---------------------------------------------------------------------------
    // This function receives hear information from player
    public void hear(int time, int direction, String message)
    {
    	System.out.println("Heard:" + message);
    }

    //---------------------------------------------------------------------------
    // This function receives hear information from referee
    public void hear(int time, String message)
    {
	    System.out.println("Heard:" + message);
	
    	if (m_global_param.getRunmode() == RUNNINGMODE.MODE_STATE_MACHINE) 
    	{
    		m_statemachine.hear(message);
    	}
    	else if (m_global_param.getRunmode() == RUNNINGMODE.MODE_REACTIVE) 
    	{
    		m_reactive.hear(message);
    	}
		else 
		{
			try {
				throw new Exception("Failed to handle an hear!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	    
    	if(message.compareTo("time_over") == 0) 
    	{
		    m_timeOver = true;
	    }
	}
    
    public void body_sense(String msg)
    {
    	//if (m_global_param.getRunmode() == RUNNINGMODE.MODE_REACTIVE)
    	//System.out.println("------:" + msg);
    	m_statemachine.body_sense(msg);
    }
    
    public void printbody()
    {
    	m_statemachine.printbody();
    }
    
    public void moveback()
    {
	    //int x = -Math.random()*52.5;
	    //int y = 34 - Math.random()*68.0;
    	m_krislet.move(-Math.random()*52.5, 34 - Math.random()*68.0);
    }

    //===========================================================================
    // Private members
    private SendCommand m_krislet;			// robot which is controled by this brain
    private Memory m_memory;				// place where all information is stored
    volatile private boolean m_timeOver;
    private String m_playMode;
    
}
