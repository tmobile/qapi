package com.tmobile.ct.tep.qapi.tvaultclient.domain;

import java.util.HashMap;

public class Secrets {

	private HashMap<String, String> data = new HashMap<String, String>();


	public HashMap<String, String> getData() {
		return data;
	}

	public void setData(HashMap<String, String> data) {
		this.data = data;
	}

}
