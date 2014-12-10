#include "agent_storage.hpp"

namespace AgentRepo3000 {

uint32_t agent_storage::register_agent() {
  m_agents.insert(m_agents.end(), std::make_pair(++last_id, std::make_shared<agent_data>()));
  return last_id;
}

std::shared_ptr<agent_data> agent_storage::agent(uint32_t id) {
  return m_agents.at(id);
}

std::map<uint32_t, std::shared_ptr<agent_data>>& agent_storage::agents() {
  return m_agents;
}


} /* namespace AgentRepo3000 */