package controller;

import common.events.ClientEvent;

public interface EventHandler {
	public void handle(ClientEvent event);
}
