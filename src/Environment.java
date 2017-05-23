import java.util.Vector;

public class Environment {
	private boolean m_debug = false;

	World m_world;
	XMLParser m_xml;

	public Environment() {
		System.out.println("Env created");
		m_world = new World();
   	 	m_xml = new XMLParser();
   	 	m_xml.LoadCfgReactive();
   	 	//m_xml.dump();
	}
	
	public GloablePosition CurrentAngle(Memory mem) {
		return m_world.CurrentAngle(mem);
	}
	
	public FlagInfo NearestFlag(Memory mem) 
	{
		return m_world.NearestFlag(mem);
	}
	
	public RelativePosition Whereami(FlagInfo flag, float distance, float angle)
	{
		try {
			return m_world.Whereami(flag, distance, angle);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}

class GloablePosition {
	char line;
	float x;
	float y;
	float angle;
	
}

class RelativePosition {
	String flag;
	float x;
	float y;
	float angle;
}

class World {
	boolean m_debug = false;
	
	Vector<FlagInfo> m_flag_list; 
	
	public World() 
	{
		m_flag_list = new Vector<FlagInfo>(1);
		InitFlags();
	}
	
	public int flagSize()
	{
		return m_flag_list.size();
	}
	
	public String getFlagAt(int i)
	{
		return String.format("%s %d", m_flag_list.get(i));
	}
	
	public FlagInfo getFlagNodeAt(int i)
	{
		return m_flag_list.get(i);
	}
	
	public void InitFlags() 
	{
		
		//Flags top
		m_flag_list.add(new FlagInfo("flag t l", ' ', 't', 'l', 50, true));
		m_flag_list.add(new FlagInfo("flag t l", ' ', 't', 'l', 30, true));
		m_flag_list.add(new FlagInfo("flag t l", ' ', 't', 'l', 20, true));
		m_flag_list.add(new FlagInfo("flag t l", ' ', 't', 'l', 10, true));
		m_flag_list.add(new FlagInfo("flag t", ' ', 't', ' ', 0, true));
		m_flag_list.add(new FlagInfo("flag t r", ' ', 't', 'l', 10, true));
		m_flag_list.add(new FlagInfo("flag t r", ' ', 't', 'l', 20, true));
		m_flag_list.add(new FlagInfo("flag t r", ' ', 't', 'l', 30, true));
		m_flag_list.add(new FlagInfo("flag t r", ' ', 't', 'l', 50, true));
		
		//Flags left
		m_flag_list.add(new FlagInfo("flag l t", ' ', 't', 'l', 30, true));
		m_flag_list.add(new FlagInfo("flag l t", ' ', 't', 'l', 20, true));
		m_flag_list.add(new FlagInfo("flag l t", ' ', 't', 'l', 10, true));
		m_flag_list.add(new FlagInfo("flag l", ' ', 'l', ' ', 0, true));
		m_flag_list.add(new FlagInfo("flag l b", ' ', 'l', 'b', 10, true));
		m_flag_list.add(new FlagInfo("flag l b", ' ', 'l', 'b', 20, true));
		m_flag_list.add(new FlagInfo("flag l b", ' ', 'l', 'b', 30, true));
		
		//Flags bottom
		m_flag_list.add(new FlagInfo("flag b l", ' ', 'b', 'l', 50, true));
		m_flag_list.add(new FlagInfo("flag b l", ' ', 'b', 'l', 30, true));
		m_flag_list.add(new FlagInfo("flag b l", ' ', 'b', 'l', 20, true));
		m_flag_list.add(new FlagInfo("flag b l", ' ', 'b', 'l', 10, true));
		m_flag_list.add(new FlagInfo("flag b", ' ', 'b', ' ', 0, true));
		m_flag_list.add(new FlagInfo("flag b r", ' ', 'b', 'r', 10, true));
		m_flag_list.add(new FlagInfo("flag b r", ' ', 'b', 'r', 20, true));
		m_flag_list.add(new FlagInfo("flag b r", ' ', 'b', 'r', 30, true));
		m_flag_list.add(new FlagInfo("flag b r", ' ', 'b', 'r', 50, true));
		
		//Flags right
		m_flag_list.add(new FlagInfo("flag r t", ' ', 'r', 't', 30, true));
		m_flag_list.add(new FlagInfo("flag r t", ' ', 'r', 't', 20, true));
		m_flag_list.add(new FlagInfo("flag r t", ' ', 'r', 't', 10, true));
		m_flag_list.add(new FlagInfo("flag r", ' ', 'r', ' ', 0, true));
		m_flag_list.add(new FlagInfo("flag l b", ' ', 'r', 'b', 10, true));
		m_flag_list.add(new FlagInfo("flag l b", ' ', 'r', 'b', 20, true));
		m_flag_list.add(new FlagInfo("flag l b", ' ', 'r', 'b', 30, true));
	}
	
	public boolean Exists(String flagType, char type, char pos1, char pos2, int num, boolean out)
	{
		for (int i = 0; i < m_flag_list.size(); i ++) {
			FlagInfo obj;
			
			obj = m_flag_list.elementAt(i);
			if (obj.equals(new FlagInfo(flagType, type, pos1, pos2, num, out))) {
				return true;
			}
		}
		return false;
	}
	
	public FlagInfo NearestFlag(Memory mem)
	{
		int nearest = -1;
		float distance = 10000;
		float direction = 360;
		ObjectInfo obj = null;
		for (int i = 0; i < m_flag_list.size(); i ++) {
			obj = mem.getObject(m_flag_list.elementAt(i).getType(), 
								m_flag_list.elementAt(i).m_num);
			if (obj != null) {
				/*System.out.println("i:" + i + "," + m_flag_list.elementAt(i).getType() +
						",num:" + m_flag_list.elementAt(i).m_num +
						",Ftype:" + obj.getType() + 
						",obj.dis:" + obj.m_distance + ",dbg:dis:" + distance);*/
				if (obj.m_distance < distance) {
					nearest = i;
					distance = obj.m_distance;
					direction = obj.m_direction;
				}
			}
		}

		if (nearest > 0) {
			FlagInfo flg = m_flag_list.elementAt(nearest);
			
			if (flg != null) {
				flg.m_distance = distance;
				flg.m_direction = direction;
				
				//System.out.println("######NearestF:" + nearest + ",type:" + flg.getType() + ",num:" + flg.m_num +
				//		", pos1:" + flg.m_pos1 + ", pos2:" + flg.m_pos2 +
				//		",dis:" + flg.m_distance + ", dir:" + flg.m_direction);
				
				if ( nearest < 10000)
					return flg;
			}
		}		
	
		return null;
	}
	
	public GloablePosition CurrentAngle(Memory mem)
	{
		float distance = 10000;
		float angle = 360;
		ObjectInfo obj;
		char kind[] = {'t', 'r', 'b', 'l'};
		int nearest = -1;
		
		for (int i = 0; i < 4; i ++) {
			obj = mem.getObject("line", kind[i]);
			
			if (obj != null) {
				if (m_debug)
					System.out.println("Line:" + obj.m_type + " " + kind[i]);
				
				if (obj.m_distance < distance) {
					distance = obj.m_distance;
					angle = obj.m_direction;
					nearest = i;
				}
			}
		}
		
		GloablePosition pos = new GloablePosition();
		if (nearest > -1) {
			pos.line = kind[nearest];
			pos.angle = angle;			
		}
		return pos;
	}
	
	public RelativePosition Whereami(FlagInfo flag, float distance, float angle) throws Exception
	{
		double x, y;
		float ang = 0;
		RelativePosition pos = new RelativePosition();

		ang = 90 - (Math.abs(angle) + flag.m_direction);
		x = Math.abs(distance * Math.sin(Math.toRadians(ang)));
		y = Math.abs(distance * Math.cos(Math.toRadians(ang)));
		//System.out.println(" Dis:" + distance + ",Ang:" + angle + ",FAng:" + flag.m_direction + 
		//		"Flag:" + flag.getType() + " " + flag.m_num + 
		//		",ang:(" + ang + "), X:" + x + ", Y:" + y);

		pos.angle = ang;
		pos.x = (float)x;
		pos.y = (float)y;

		//Bond current horizontal position with a flag.
		if (flag.getType().startsWith("flag b") || flag.getType().startsWith("flag t")) {
			pos.flag = flag.getType() + " " + flag.m_num;			
		}
		else if (flag.getType().startsWith("flag l")) {
			if (y >= 0 && y < 15) {
				pos.flag = "flag b l 50";
			}
			else if (y >= 15 && y < 25) {
				pos.flag = "flag b l 40";
			}
			else if (y >= 25 && y < 35) {
				pos.flag = "flag b l 30";				
			}
			else if (y >= 35 && y < 45) {
				pos.flag = "flag b l 20";
			}
			else if (y >= 55 && y < 65) {
				pos.flag = "flag b l 10";
			}
			else if (y >= 65 && y < 75) {
				pos.flag = "flag b 0";
			}
			else {
				if (m_debug)
					System.out.println("X:" + x + ",Y:" + y);
				throw new Exception("Failed to find a flag!");

			}
		}
		else if (flag.getType().startsWith("flag r")) {
			if (y >= -10 && y < 15) {
				pos.flag = "flag b r 50";
			}
			else if (y >= 15 && y < 25) {
				pos.flag = "flag b r 40";
			}
			else if (y >= 25 && y < 35) {
				pos.flag = "flag b r 30";				
			}
			else if (y >= 35 && y < 45) {
				pos.flag = "flag b r 20";
			}
			else if (y >= 55 && y < 65) {
				pos.flag = "flag b r 10";
			}
			else if (y >= 65 && y < 75) {
				pos.flag = "flag b 0";
			}
			else {
				if (m_debug)
					System.out.println("X:" + x + ",Y:" + y);
				throw new Exception("Failed to find a flag!");
			}
		}
		else {
			if (m_debug)
				System.out.println("X:" + x + ",Y:" + y);
			throw new Exception("Failed to find a flag!");
		}

		if (m_debug) {
			System.out.println("Pos:" + pos.flag + ",f_dis:" + distance + 
					",f_ang:" + flag.m_direction + ",o_ang:" + angle +
					",ang:" + pos.angle + ",X:" + x + ",Y:" + y);
			System.out.println("-----------------------------------");
		}
		
		return pos;
	}
}
