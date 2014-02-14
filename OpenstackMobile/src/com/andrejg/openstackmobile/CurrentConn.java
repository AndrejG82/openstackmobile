package com.andrejg.openstackmobile;

public class CurrentConn {
	private String ConnToken;
	private String NovaURL;
	private String CinderURL;
	
	public CurrentConn(String asConnToken,String asNovaURL, String asCinderURL) {
		setConnToken(asConnToken);
		setNovaURL(asNovaURL);
		setCinderURL(asCinderURL);
	}

	public String getConnToken() {
		return ConnToken;
	}

	private void setConnToken(String connToken) {
		ConnToken = connToken;
	}

	public String getNovaURL() {
		return NovaURL;
	}

	private void setNovaURL(String novaURL) {
		NovaURL = novaURL;
	}

	public String getCinderURL() {
		return CinderURL;
	}

	private void setCinderURL(String cinderURL) {
		CinderURL = cinderURL;
	}
	
}
