package com.jzb.family.domain;

import java.util.List;

public class SysMenu extends SysResource {

	private static final long serialVersionUID = -4304156452469296410L;
	
	private List<SysMenu> subMenuList;
	
	private boolean hasChild;

	public List<SysMenu> getSubMenuList() {
		return subMenuList;
	}

	public void setSubMenuList(List<SysMenu> subMenuList) {
		this.subMenuList = subMenuList;
	}

	public boolean isHasChild() {
		return hasChild;
	}

	public void setHasChild(boolean hasChild) {
		this.hasChild = hasChild;
	}
	
	

}
