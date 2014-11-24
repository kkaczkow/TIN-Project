#ifndef AGENTREPO3000_AGENT_STORAGE_HPP
#define AGENTREPO3000_AGENT_STORAGE_HPP

#include <utility>
#include "agent_data.hpp"
#include "list.hpp"

namespace AgentRepo3000 {

class agent_storage {
  list<std::pair<boost::asio::ip::tcp::endpoint, agent_data>> m_agents;
public:
  agent_storage() = default;
  agent_data& register_agent(boost::asio::ip::tcp::endpoint endpoint);
  list<std::pair<boost::asio::ip::tcp::endpoint, agent_data>>& agents();
};

}

#endif /* AGENTREPO3000_AGENT_STORAGE_HPP */