
public class ReactiveComp {
	private boolean m_debug = false;
	private Brain m_brain;

	public ReactiveComp(Brain brain) 
	{
		System.out.println("ReactiveComp Created.");
		
   	 	m_brain = brain;
	}
	
	//For reactive vision handling, an agent just find the bal and kick it to goal.
	//And in each predefined area, the running power and kick power are different.
	public void see(Memory mem, SendCommand krislet)
	{
		if (m_debug)
			System.out.println("ReactiveComp see.");
		
		//Find current location and adjust kick power and running speed
		//according to xml config.
		FlagInfo flg = m_brain.m_env.NearestFlag(mem);
		if (flg != null) {
			GloablePosition gpos = m_brain.m_env.CurrentAngle(mem);
			RelativePosition rpos = m_brain.m_env.Whereami(flg, flg.m_distance, gpos.angle);
			ZoneInfo zi;

			//adjust agent param according to current position.
			zi = m_brain.m_env.m_xml.m_zones.getZone(rpos.flag);
			if (zi != null) {
				m_brain.m_global_param.m_kick_power = zi.m_kick_power;
				m_brain.m_global_param.m_run_power = zi.m_run_power;
			}
			
			if (m_debug) {
				System.out.println("kick power: " + m_brain.m_global_param.m_kick_power);
				System.out.println("run power: " + m_brain.m_global_param.m_run_power);
			}
		}
		
		//Find ball and kick it.
		ObjectInfo object;
		object = mem.getObject("ball");

		if( object == null )
	    {
			// If you don't know where is ball then find it
			krislet.turn(40);
			mem.waitForNewInfo();
	    }
		else if( object.m_distance > 1.0 )
	    {
			// If ball is too far then
			// turn to ball or 
			// if we have correct direction then go to ball
			if( object.m_direction != 0 )
			    krislet.turn(object.m_direction);
			else
			    krislet.dash(10*object.m_distance);
	    }
		else 
		{
			// We know where is ball and we can kick it
			// so look for goal
			if( m_brain.m_global_param.m_side == 'l' )
			    object = mem.getObject("goal r");
			else
			    object = mem.getObject("goal l");

			if( object == null )
		    {
				krislet.turn(40);
				mem.waitForNewInfo();
		    }
			else
			{
			   	krislet.kick(m_brain.m_global_param.m_kick_power, object.m_direction);
			}
		}
	}
	
	public void hear(String msg)
	{
		
	}
	
}
