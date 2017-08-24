package com.asai24.golf.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Tee implements Serializable {
	private static final long serialVersionUID = 1L;

	private String oobId;
	private String name;
	private List<Hole> holes = new ArrayList<Hole>();
	private boolean containTempData = false;
	private String idServer;

	public String getOobId() {
		return oobId;
	}

	public void setOobId(String oobId) {
		this.oobId = oobId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Hole> getHoles() {
		return holes;
	}

	public void setHoles(List<Hole> holes) {
		this.holes = holes;
	}

	public void addHole(Hole hole) {
		holes.add(hole);
	}

	@Override
	public String toString() {
		return name;
	}

	public boolean hasTempData() {
		return containTempData;
	}

	public void setContainTempData(boolean hasTempData) {
		this.containTempData = hasTempData;
	}

	public void setIdServer(String idServer) {
		this.idServer = idServer;
	}

	public String getIdServer() {
		return idServer;
	}

}
