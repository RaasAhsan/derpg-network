package com.derpg.dnet.event;

import java.util.ArrayList;
import java.util.List;

public class QuestController {

	private List<Quest> quests;
	
	public QuestController() {
		quests = new ArrayList<Quest>();
		quests.add(new Quest(0, "Successful Theft"));
	}
	
	public Quest getQuestByID(int id) {
		return quests.get(id);
	}
	
}
