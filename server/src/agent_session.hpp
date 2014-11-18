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

namespace AgentRepo3000 {

class agent_session : public std::enable_shared_from_this<agent_session> {
	boost::asio::ip::tcp::socket socket;
	static const char* vim;
public:
	agent_session(boost::asio::ip::tcp::socket socket)
		: socket(std::move(socket)) {}
	void run();
};

} /* namespace AgentRepo3000 */

#endif /* AGENTREPO3000_AGENT_SESSION_HPP_ */
