#ifndef AGENTREPO3000_AGENT_SESSION_HPP
#define AGENTREPO3000_AGENT_SESSION_HPP

#include <boost/asio.hpp>

#include <memory>
#include <iostream>

#include "logging/logging.hpp"
#include "proto/messages.hpp"

typedef unsigned char uchar;

namespace AgentRepo3000 {
  
class agent_storage;
class agent_data;

/** Represents communication session with an agent
 */
class agent_session : public std::enable_shared_from_this<agent_session> {
  boost::asio::ip::tcp::socket socket;
  boost::asio::ip::address m_address;
  uint16_t m_port;
  agent_storage& storage;
  uint32_t id;
  uint32_t networkId;
  std::shared_ptr<agent_data> data;

  proto::message message_type;
  void do_read_requests();
  void handle_register();
  void handle_services();
  void handle_list_agents();
  void handle_list_services();
  
  std::ostream& log(logging::level lvl) const;

public:
  agent_session(boost::asio::ip::tcp::socket socket, agent_storage& storage)
    : socket(std::move(socket)), m_address(this->socket.remote_endpoint().address()),
    m_port(this->socket.remote_endpoint().port()), storage(storage) {}
  ~agent_session();
  void run();
};

} /* namespace AgentRepo3000 */

#endif /* AGENTREPO3000_AGENT_SESSION_HPP */
