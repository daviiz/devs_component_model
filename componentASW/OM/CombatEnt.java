package componentASW.om;

import GenCol.ExternalRepresentation;

/**
 * 战斗实体基类，用于交战实体之间的消息传输
 * */
public class CombatEnt extends GenCol.entity {
	//位置信息
	protected int x;
	
	protected int y;
	
	//"live" "destroyed"
	protected String status;
	
	protected int belong; //1 红方,0  中立方,-1 蓝方
	
	protected int _type;  //0:platform, 1:weapon,-1 未定义
	
	protected String sendorder;//发送的指令
	
	

	public CombatEnt() {
		this.name = "combatEntBase";
		this.status = "live";
		this.x = 0;
		this.y = 0;
		this.belong = 0;
		this._type = -1;
		this.sendorder = "";
	}
	
	public CombatEnt(CombatEnt ent) {
		this.name = ent.name;
		this.status = ent.status;
		this.x = ent.x;
		this.y = ent.y;
		this.belong = ent.belong;
		this._type = ent._type;
		this.sendorder = ent.sendorder;
	}
	public CombatEnt(String name,int x,int y,String status,int belong,int _type,String sendorder) {
		this.name = name;
		this.status = status;
		this.x = x;
		this.y = y;
		this.belong = belong;
		this._type = _type;
		this.sendorder = sendorder;
	}
	
	public boolean eq(String nm) {
		return getName().equals(nm);
	}

	public Object equalName(String nm) {
		if (eq(nm))
			return this;
		else
			return null;
	}
	
	public boolean equals(Object o) { // overrides pointer equality of Object
		if (!(o instanceof CombatEnt))
			return false;
		else
			return eq(((CombatEnt) o).getName());
	}

	public ExternalRepresentation getExtRep() {
		return new ExternalRepresentation.ByteArray();
	}
	
	public String toString() {

		return ("name  " + name +"X  " + x + " Y  " + y + " status  " + status+"Belong  " + belong+"_type  " + _type);
	}
	public void print() {
		System.out.println(name);
	}
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public int getBelong() {
		return belong;
	}

	public void setBelong(int belong) {
		this.belong = belong;
	}

	public int get_type() {
		return _type;
	}

	public void set_type(int _type) {
		this._type = _type;
	}
	
	public String getSendorder() {
		return sendorder;
	}

	public void setSendorder(String sendorder) {
		this.sendorder = sendorder;
	}

}
