
int Agent::max_id = 0;


void Agent::run() {
	// we run the loop until the client closes its connection
	for (; isConnected(); ) {
		// if there are connection requests from another client handle them first
		if (!conRequestsQueue.empty())
			handleConnectionRequests();
		// then wait for a request from the agent
		else
			waitForQuery();
	}
}

void Agent::waitForQuery() {
//TODO
}

void Agent::registerServices() {
	services_list& list = packet_cast<services_list&>(buffer);
	services.clear();
	for (int i  = 0; i < list.count; ++i)
		services.emplace_back(list.services[i]);
	// FIXME do we really care that much about memory usage?
	services.shrink_to_fit();
}

