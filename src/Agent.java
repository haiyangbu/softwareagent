import java.util.ArrayList;
import java.util.Vector;

class ZoneInfo {
	public ZoneInfo() {
		m_area_list = new ArrayList<String>();
	}
	String m_name;
	ArrayList<String> m_area_list;
	int m_run_power;
	int m_kick_power;
	String m_avoid_direction;
	int m_collision_avoid;
	
	public void add(String area)
	{
		m_area_list.add(area);
	}
	
	public boolean findKey(String key)
	{
		for (int i = 0; i < m_area_list.size(); i ++)
		{
			if (m_area_list.get(i).equals(key)) 
			{
				return true;
			}
		}
		return false;
	}
}

class Zones {
	public Zones() {
		m_zone_list = new Vector<ZoneInfo>(1);
	}
	public Vector<ZoneInfo> m_zone_list;
	
	public void addZone(ZoneInfo obj) {
		m_zone_list.add(obj);
	}
	
	public int size() {
		return m_zone_list.size();
	}
	
	public ZoneInfo get(int i) {
		return m_zone_list.get(i);
	}
	
	public ZoneInfo getZone(String key) {
		
		for (int i = 0; i < m_zone_list.size(); i ++) {
			if (m_zone_list.get(i).findKey(key)) {
				return m_zone_list.get(i);
			}
		}
		return null;
	}
}

class DealObstacle {
	public DealObstacle() {
		
	}
	int m_igore;
	String m_Stepdirection;
}

enum RUNNINGMODE{
	MODE_REACTIVE,
	MODE_STATE_MACHINE,
	RUNMODEMAX
}

public class Agent {

	boolean g_debug_on;
	String m_runningmode;
	
	int m_run_power;
	int m_kick_power;
	char m_side;
	String m_goal_policy;
	String m_ball_policy;

	int m_moving_mode; //0: straight, 1:wave
	boolean m_kick_off;
	
	RUNNINGMODE m_running_mode;
	
	RUNNINGMODE getRunmode() {
		return m_running_mode;
	}
	
	void setRunmode(RUNNINGMODE mode) {
		m_running_mode = mode;
	}
	
	void setRunPower(int power) {
		m_run_power = power;
	}
	
	void setKickPower(int power) {
		m_kick_power = power;
	}
	
	int getRunPower(int power) {
		return m_run_power;
	}
	
	int getKickPower(int power) {
		return m_kick_power;
	}
	
}
