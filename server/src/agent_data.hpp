#ifndef AGENTREPO3000_AGENT_DATA_HPP
#define AGENTREPO3000_AGENT_DATA_HPP

#include <boost/asio.hpp>
#include <vector>
#include <set>

namespace AgentRepo3000 {

struct service_data {
  uint8_t flags;
  uint16_t id;
  uint16_t port;
  
  std::vector<boost::asio::mutable_buffer> to_buffers() {
    return {
      boost::asio::buffer(&flags, 1),
      boost::asio::buffer(&id, 2),
      boost::asio::buffer(&port, 2)
    };
  }
  std::vector<boost::asio::mutable_buffer> data_to_buffers() {
    return {
      boost::asio::buffer(&id, 2),
      boost::asio::buffer(&port, 2)
    };
  }
  
  bool operator<(const service_data& o) const {
    return flags < o.flags ||
      (flags == o.flags && (id < o.id || (id == o.id && port < o.port)));
  }
};

struct agent_data {
  std::vector<boost::asio::ip::address> ip_addresses;
  std::set<service_data> services;
};

}


#endif /* AGENTREPO3000_AGENT_DATA_HPP */