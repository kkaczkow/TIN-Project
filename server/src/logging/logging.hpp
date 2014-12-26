 
#ifndef AGENTREPO3000_LOGGING_LOGGING_HPP
#define AGENTREPO3000_LOGGING_LOGGING_HPP

#include <boost/asio.hpp>

namespace AgentRepo3000 {
namespace logging {

enum class level {
  NONE =  0x00,
  ERROR = 0x01,
  INFO =  0x02,
  DEBUG = 0x04,
  TRACE = 0x08
};
  
} /* namespace logging */
} /* namespace AgentRepo3000 */

#endif /* AGENTREPO3000_LOGGING_LOGGING_HPP */

