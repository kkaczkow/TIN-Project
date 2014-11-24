#ifndef AGENTREPO3000_AGENT_DATA_HPP
#define AGENTREPO3000_AGENT_DATA_HPP

#include <boost/asio.hpp>
#include "list.hpp"

namespace AgentRepo3000 {

struct service_data {
  unsigned short port;
  unsigned short name;
};

struct agent_data {
  list<boost::asio::ip::address> ip_addresses;
  list<service_data> services;
};

}


#endif /* AGENTREPO3000_AGENT_DATA_HPP */