/*
Client class
*/


class Agent : public Runnable {
	int socket;
	long id;
	bool available;
	vector<Service> services;
	vector<ipv4> ipsv4;
	vector<ipv6> ipsv6;
	queue<ConnectionRequest> conRequestsQueue;

	void registerServices();
	void handleConnectionRequests();
	void waitForQuery();

	static int max_id;
public:
	Agent(int socket) : socket(socket), id(++max_id) {}
	bool isConnected();
	void run();
}
