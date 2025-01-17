package com.koyta.enums;

public enum TodoStatus {

	NOT_STARTED(1, "Not Started"), IN_PROGRESS(2, "In Progress"), COMPLETED(3, "Completed");

	private Integer id;

	private String name;

	TodoStatus(int id, String name) {

		this.id = id;
		this.name = name;

	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}


}
