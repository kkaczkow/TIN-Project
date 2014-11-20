/*
 * agent_session.cpp
 *
 *  Created on: Dec 17, 2014
 *      Author: konrad
 */

#include <iostream>
#include "agent_session.hpp"

namespace AgentRepo3000 {

const char* agent_session::registered = "Registered\n";

void agent_session::do_read_requests() {
	auto self(shared_from_this());
	std::clog << "Accepted agent" << std::endl;
	boost::asio::async_read(socket, boost::asio::buffer(&message_type, 1),
		[this, self](boost::system::error_code ec, std::size_t) {
		switch (message_type) {
		case REGISTER:
			handle_vim_request();
			break;
		}
	});
}

void agent_session::handle_vim_request() {
	auto self(shared_from_this());
	std::clog << "Sending vim" << std::endl;
	boost::asio::async_write(socket, boost::asio::buffer(registered, strlen(registered)),
		[this, self](boost::system::error_code ec, std::size_t) {
	});
}

void agent_session::run() {
	do_read_requests();
}

} /* namespace AgentRepo3000 */
