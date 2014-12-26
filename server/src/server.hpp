#ifndef AGENTREPO3000_SERVER_HPP
#define AGENTREPO3000_SERVER_HPP

#include <boost/asio.hpp>
#include <iostream>

#include "agent_session.hpp"
#include "agent_storage.hpp"

#include "logging/logging.hpp"

namespace AgentRepo3000 {

class server {
  static const int PORT;

  boost::asio::ip::tcp::socket socket;
  boost::asio::ip::tcp::acceptor acceptor;
  agent_storage storage;
  
  std::ostream& log(logging::level lvl);
  
  void do_accept();
public:
  server(boost::asio::io_service& io_service);
};

}

#endif /* AGENTREPO3000_SERVER_HPP */
