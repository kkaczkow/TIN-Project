#include "server.hpp"

#include <boost/asio.hpp>

#include <iostream>

using boost::asio::ip::tcp;

namespace AgentRepo3000 {

const int server::PORT = 12345;

server::server(boost::asio::io_service& io_service) :
	acceptor(io_service, tcp::endpoint(tcp::v4(), PORT)),
	socket(io_service) {
	do_accept();
}

void server::do_accept() {
	acceptor.async_accept(socket, [this](boost::system::error_code ec){
		if (!ec) {
			std::make_shared<agent_session>(std::move(socket), storage)->run();
		}
		do_accept();
	});
}

}

