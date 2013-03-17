package com.derpg.dnet.event;

public class Command {
	
	public static final int VARIABLE_QUEST = 0;
	public static final int FIELD_QUEST_POS = 0;
	public static final int FIELD_QUEST_NEXT = 1;
	public static final int FIELD_QUEST_COMPLETE = 2;
	
	public static final int VARIABLE_CONSTANT = 1;
	
	public static final int CONDITION_IS = 0;
	public static final int CONDITION_LESS_THAN = 1;
	public static final int CONDITION_GREATER_THAN = 2;
	public static final int CONDITION_LOQT = 3;
	public static final int CONDITION_GOQT = 4;
	
	public static final int COMMAND_IF = 0;
	public static final int COMMAND_ENDIF = 1;
	public static final int COMMAND_ENDSCRIPT = 2;
	public static final int COMMAND_GIVE = 3;
	public static final int COMMAND_TAKE = 4;
	public static final int COMMAND_START_EVENT = 5;
	public static final int COMMAND_START_BATTLE = 6;
	public static final int COMMAND_SAY = 7;
	
	private int command;
	private String[] params;
	
	public Command(int i, String[] params) {
		this.command = i;
		this.params = params;
	}
	
	public int getCommand() { 
		return command;
	}

	public String[] getParameters() {
		return params;
	}
	
}

