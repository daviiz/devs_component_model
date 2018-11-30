package ComponetAntiAirFleet.FleetAntiAirDefense;

import GenCol.entity;

public class RedEntity extends entity {
	protected int m_velocity;
	protected double m_x;
	protected double m_y;

	public RedEntity(RedEntity r) {
		name = r.name;
		m_velocity = r.m_velocity;
		m_x = r.m_x;
		m_y = r.m_y;
	}

	public RedEntity(String _name, int v, double x, double y) {
		name = _name;
		m_velocity = v;
		m_x = x;
		m_y = y;
	}

	public String getName() {
		return name;
	}

	public int getM_velocity() {
		return m_velocity;
	}

	public void setM_velocity(int m_velocity) {
		this.m_velocity = m_velocity;
	}

	public double getM_x() {
		return m_x;
	}

	public void setM_x(double m_x) {
		this.m_x = m_x;
	}

	public double getM_y() {
		return m_y;
	}

	public void setM_y(double m_y) {
		this.m_y = m_y;
	}

}
