#ifndef AGENTREPO3000_PROTO_MESSAGES_HPP
#define AGENTREPO3000_PROTO_MESSAGES_HPP

#include <boost/asio.hpp>

namespace AgentRepo3000 {
namespace proto {

enum class message : unsigned char {
  CLIENT_FLAG = 0x00,
  REGISTER = 0x01,
  SERVICES = 0x02,
  LIST_AGENTS = 0x03,
  LIST_SERVICES = 0x04,
  CONN_REQUEST = 0x05,
  
  SERVER_FLAG = 0x80,
  REGISTERED_ID = 0x81,
  AGENT_LIST = 0x83,
  SERVICE_LIST = 0x84,
  CONNECT = 0x85
};
  
} /* namespace proto */
} /* namespace AgentRepo3000 */

#endif /* AGENTREPO3000_PROTO_MESSAGES_HPP */

