/*
 * agent_session.hpp
 *
 *  Created on: Dec 17, 2014
 *      Author: konrad
 */

#ifndef AGENTREPO3000_AGENT_SESSION_HPP_
#define AGENTREPO3000_AGENT_SESSION_HPP_

#include <boost/asio.hpp>

#include <memory>

typedef unsigned char uchar;

namespace AgentRepo3000 {

class agent_session : public std::enable_shared_from_this<agent_session> {
	boost::asio::ip::tcp::socket socket;
	enum message_t : uchar {
		REGISTER = 'r'
	};

	static const char* registered;

	message_t message_type;
	void do_read_requests();
	void handle_vim_request();

public:
	agent_session(boost::asio::ip::tcp::socket socket)
		: socket(std::move(socket)) {}
	void run();
};

} /* namespace AgentRepo3000 */

#endif /* AGENTREPO3000_AGENT_SESSION_HPP_ */
