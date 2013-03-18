package com.derpg.dnet.event;

import java.util.ArrayList;
import java.util.List;

import com.derpg.dnet.DialogEngine;
import com.derpg.dnet.Game;

// this class handles everything
public class EventRunner {
	
	public static EventRunner eventRunner;
	
	private List<Command> commands;
	private Game game;
	private DialogEngine dialog;
	private QuestController qcontroller;
	
	private Command command;
	private int current;
	private boolean running;
	private boolean drun = false;
	
	public EventRunner(Game game) {
		this.current = 0;
		this.command = null;
		this.running = false;
		commands = new ArrayList<Command>();
		this.game = game;
		this.dialog = game.getDialogEngine();
		this.qcontroller = game.getQuestController();
	}
	
	public void update() {
		if(command != null) {
			int cm = command.getCommand();
			if(cm == Command.COMMAND_GIVE) {
				System.out.println("giving");
				this.next();
			} else if(cm == Command.COMMAND_TAKE) {
				this.next();
			} else if(cm == Command.COMMAND_SAY) {
				if(!dialog.isRunning() && !drun) {
					drun = true;
					dialog.say(command.getParameters()[0]);
				}
				if(dialog.isComplete() && drun) {
					drun = false;
					this.next();
				}
			} else if(cm == Command.VARIABLE_QUEST) {
				int qid = Integer.valueOf(command.getParameters()[0]);
				Quest q = qcontroller.getQuestByID(qid);
				if(command.getParameters()[1].equals("next")) {
					q.next();
				}
				this.next();
			}
		}
	}
	
	public void next() {
		if(current < commands.size() - 1) {
			current++;
			command = commands.get(current);
		} else {
			current = 0;
			command = null;
			commands.clear();
		}
	}

	public void runEvent(List<Command> event) {
		commands.addAll(event);
		command = commands.get(current);
	}
	
	// checks if an if statement is true
	public boolean validate(Command command) {
		
		return false;
	}
	
	public static void init(Game game) {
		eventRunner = new EventRunner(game);
	}

	public static void updateRunner() {
		eventRunner.update();
	}

}
