#ifndef AGENTREPO3000_AGENT_SESSION_HPP
#define AGENTREPO3000_AGENT_SESSION_HPP

#include <boost/asio.hpp>

#include <memory>

#include "proto/messages.hpp"

typedef unsigned char uchar;

namespace AgentRepo3000 {
  
class agent_storage;
class agent_data;

/** Represents communication session with an agent
 */
class agent_session : public std::enable_shared_from_this<agent_session> {
  boost::asio::ip::tcp::socket socket;
  agent_storage& storage;
  uint32_t id;
  agent_data* data;

  proto::message message_type;
  void do_read_requests();
  void handle_register();
  void handle_services();
  void handle_list_agents();
  void handle_list_services();

public:
  agent_session(boost::asio::ip::tcp::socket socket, agent_storage& storage)
    : socket(std::move(socket)), storage(storage) {}
  void run();
};

} /* namespace AgentRepo3000 */

#endif /* AGENTREPO3000_AGENT_SESSION_HPP */
