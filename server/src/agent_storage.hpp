#ifndef AGENTREPO3000_AGENT_STORAGE_HPP
#define AGENTREPO3000_AGENT_STORAGE_HPP

#include <map>
#include <memory>
#include "agent_data.hpp"

namespace AgentRepo3000 {

/** Holds agent data.
 */
class agent_storage {
  uint32_t last_id;
  std::map<uint32_t, std::shared_ptr<agent_data>> m_agents;
public:
  /** Creates new initially empty storage.
   */
  agent_storage() : last_id(0) {}
  /** Register a new agent
   * 
   * @returns id of the newly registered agent
   */
  uint32_t register_agent();
  /** Returns agent_data for the given id.
   * 
   * @param id id of the agent data to fetch
   * @returns agent_data for the id
   * @throws std::out_of_range if agent with the id is not registered
   */
  std::shared_ptr<agent_data> agent(uint32_t id);
  /** Returns the whole map of registered agents.
   */
  std::map<uint32_t, std::shared_ptr<agent_data>>& agents();
};

}

#endif /* AGENTREPO3000_AGENT_STORAGE_HPP */