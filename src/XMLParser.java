import java.io.*;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



public class XMLParser {
	
	static boolean debug_xmlparser = false;
	Zones m_zones;
	DealObstacle m_dealObs;
	String m_goal = "Random";
	String m_runningmode = "reactive";
	
	public XMLParser () {
		System.out.println("XML Parser created.");
		m_dealObs = new DealObstacle();
		m_zones = new Zones();
		
	}

	//public Vector<ZoneInfo> m_zone_list;

	void dump() {
		
		int zonesize = m_zones.size();
		
		System.out.println("----------------dump XML-------------------");
		System.out.println("zonesize:" + zonesize);
		for ( int i = 0; i < zonesize; i ++) {
			int areasize = m_zones.get(i).m_area_list.size();
			System.out.println("name " + m_zones.get(i).m_name);
			System.out.println("areas size:" + areasize);

			for ( int j = 0; j < areasize; j ++) {
				System.out.println("            " + m_zones.get(i).m_area_list.get(j));
			}
			System.out.println("kick:" + m_zones.get(i).m_kick_power);
			System.out.println("run:" + m_zones.get(i).m_run_power);
		}
		System.out.println("----------------dump XML-------------------");
	}

	public void LoadCfgReactive() {

		Element element = null;
		File f = new File("krislet.xml");
		DocumentBuilder db = null;
		DocumentBuilderFactory dbf = null;

		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			Document dt = db.parse(f);
			element = dt.getDocumentElement();
			if (debug_xmlparser) {
				System.out.println("Root:" + element.getNodeName());
			}
			NodeList childNodes0 = element.getChildNodes();
			for (int i = 0; i < childNodes0.getLength(); i++) {
				Node node1 = childNodes0.item(i);
				
				if(node1 instanceof Element) {
					if (debug_xmlparser) {
						System.out.println("<" + node1.getNodeName() + ">");
					}
					if ("ReactiveZones".equals(node1.getNodeName())) {
						NodeList childNodes1 = node1.getChildNodes();
						for (int j = 0; j < childNodes1.getLength(); j++) {
							Node node2 = childNodes1.item(j);
							
							if(node2 instanceof Element) {
								if (debug_xmlparser) {
									System.out.println("	<" + node2.getNodeName() + ">");
								}
								if ("zone".equals(node2.getNodeName())) {
									NodeList childNodes2 = node2.getChildNodes();
									ZoneInfo zone = new ZoneInfo();
									
									for (int k = 0; k < childNodes2.getLength(); k++) {
										Node node3 = childNodes2.item(k);
										
										if(node3 instanceof Element) {
											if (debug_xmlparser) {
												System.out.println("		<" + node3.getNodeName() + ">");
											}
											if ("name".equals(node3.getNodeName())) {
												zone.m_name = node3.getTextContent();
												if (debug_xmlparser) {
													System.out.println("		 " + zone.m_name);
												}
											}
											else if ("area_groups".equals(node3.getNodeName())) {
												NodeList childNodes3 = node3.getChildNodes();
												
												if (debug_xmlparser) {
													System.out.println("		size:" + childNodes3.getLength());
												}
												for (int m = 0; m < childNodes3.getLength(); m ++) {
													Node node4 = childNodes3.item(m);
													
													if(node4 instanceof Element) {
														zone.add(node4.getTextContent());
														if (debug_xmlparser) {
															System.out.println("		<" + node4.getNodeName() + 
																">" + node4.getTextContent());
														}
													}
												}
											}
											else if ("run_power".equals(node3.getNodeName())) {
												Integer integer = 0;
												zone.m_run_power = integer.parseInt(node3.getTextContent());
												if (debug_xmlparser) {
													System.out.println("		run power:" + zone.m_run_power);
												}

												
											}
											else if ("kick_power".equals(node3.getNodeName())) {
												Integer integer = 0;
												zone.m_kick_power = integer.parseInt(node3.getTextContent());
												if (debug_xmlparser) {
													System.out.println("		kick power:" + zone.m_kick_power);
												}

											}
										}
									}
									m_zones.addZone(zone);
								}
							}
						}
					}
					else if ("Runningmode".equals(node1.getNodeName())) {
						if(node1 instanceof Element) {
							System.out.println("run mode:" + node1.getTextContent());
							m_runningmode = node1.getTextContent();
						}
					}

				}
				
			} //end for
			
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
//System.out.println("\r\nFound one rule: " + node1.getAttributes().getNamedItem("type").getNodeValue() + ". ");
