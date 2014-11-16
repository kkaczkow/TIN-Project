/*
server class
*/


class Server : public Runnable {
	list<Agent> agents;

public:
	void run();

private:
	void listen();
}

