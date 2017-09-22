package com.mock.model;

public class MappingInfo {
	private String matchStartAndEndCharacter;
	private Request request;
	private Response response;

	public String getMatchStartAndEndCharacter() {
		return matchStartAndEndCharacter;
	}

	public void setMatchStartAndEndCharacter(String matchStartAndEndCharacter) {
		this.matchStartAndEndCharacter = matchStartAndEndCharacter;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public Request getRequest() {
		return request;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	public Response getResponse() {
		return response;
	}
}
