/*
 * agent_session.cpp
 *
 *  Created on: Dec 17, 2014
 *      Author: konrad
 */

#include <iostream>
#include "agent_session.hpp"

namespace AgentRepo3000 {

const char* agent_session::vim = "VIM!!!!!1111";

void agent_session::run() {
	auto self(shared_from_this());
	boost::asio::async_write(socket, boost::asio::buffer(vim, strlen(vim)),
		[this, self](boost::system::error_code ec, std::size_t /*length*/) {
	});
}

} /* namespace AgentRepo3000 */
