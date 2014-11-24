#include "agent_storage.hpp"

namespace AgentRepo3000 {

agent_data& agent_storage::register_agent(boost::asio::ip::tcp::endpoint endpoint) {
  m_agents.push_back(std::make_pair(endpoint, agent_data()));
  return m_agents.back().second;
}

list<std::pair<boost::asio::ip::tcp::endpoint, agent_data>>& agent_storage::agents() {
  return m_agents;
}


} /* namespace AgentRepo3000 */