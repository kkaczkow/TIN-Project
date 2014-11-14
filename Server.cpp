#include "Server.h"

void Server::run() {
	createSocket();
	listen();
}

/*
	Creates and initializes server socket
	@throws bla if socket could not be created
*/
void Server::createSocket() {
	serverSocket = socket(AF_INET); // FIXME
	if (serverSocket == -1)
		throw ; // FIXME
	// TODO find where to bind
	if (bind() < 0) // FIXME
		throw ; // FIXME
}


void Server::listen() {
	if (::listen(serverSocket, 5) < 0)
		throw ; // FIXME
	while ((int cSock = accept(serverSocket)) > 0) {
		// create new agent & handle him
		agents.emplace_back(*this, cSock)->run();
	}
}

