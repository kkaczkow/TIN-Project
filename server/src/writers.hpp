#ifndef AGENTREPO3000_WRITERS_HPP
#define AGENTREPO3000_WRITERS_HPP

#include <boost/asio.hpp>
#include <vector>
#include <set>
#include <functional>

#include "agent_data.hpp"
#include "agent_storage.hpp"
#include "proto/messages.hpp"

namespace AgentRepo3000 {
  
template<typename... Args>
class writer {
protected:
  typedef std::function<void(boost::system::error_code)> after_type;
 
  boost::asio::ip::tcp::socket& socket;
  after_type after;
  
  virtual void do_write() = 0;
  
public:
  writer(boost::asio::ip::tcp::socket& socket) : socket(socket) {}
  void write(Args... args, after_type after) {
    this->after = after;
    do_write(args...);
  }
};

class agent_list_writer :
  public writer<>,
  public std::enable_shared_from_this<agent_list_writer> {
  
  static const proto::message message_type;
  
  agent_storage& storage;
  
  std::vector<boost::asio::const_buffer> buffers;
  uint32_t id;
  std::vector<boost::asio::ip::address_v4::bytes_type> ipv4;
  std::vector<boost::asio::ip::address_v6::bytes_type> ipv6;
  
  uint32_t last_id;
  
  void do_write_next();
  void do_write_agent(std::shared_ptr<agent_data> data);
  void do_write_end_of_list();
  
  void do_write() {
    buffers.push_back(boost::asio::buffer(&message_type, 1));
    auto it = storage.agents().begin();
    if (it != storage.agents().end()) {
      last_id = it->first;
      do_write_agent(it->second);
    } else  {
      do_write_end_of_list();
    } 
  }
  
public:
  agent_list_writer(boost::asio::ip::tcp::socket& socket, agent_storage& storage) :
    writer(socket), storage(storage) {}
};

class service_list_writer :
  public writer<>,
  public std::enable_shared_from_this<service_list_writer> {
    
  static const proto::message message_type;
  static const std::array<uint8_t, 1> end_of_services;
  static const std::array<uint8_t, 4> end_of_agents;
  
  agent_storage& storage;
  
  uint32_t id;
  service_data service;
  std::vector<boost::asio::const_buffer> buffers;
  
  std::shared_ptr<agent_data> agent;
  
  uint32_t last_id;
  service_data last_service;
  
  std::set<uint16_t> agent_id_filter;
  std::set<uint16_t> service_id_filter;
  
  void do_write_next_agent();
  void do_write_first_service();
  void do_write_next_service();
  void do_write_service();
  void do_write_end_of_services();
  void do_write_end_of_agents();
  
  void do_write() {
    last_id = 0;
    buffers.push_back(boost::asio::buffer(&message_type, 1));
    do_write_next_agent();
  }
  
public:
  service_list_writer(boost::asio::ip::tcp::socket& socket, agent_storage& storage,
    std::shared_ptr<std::vector<uint32_t>> agent_id_filter, std::shared_ptr<std::vector<uint16_t>> service_id_filter) :
    writer(socket), storage(storage), agent_id_filter(agent_id_filter->begin(), agent_id_filter->end()),
    service_id_filter(service_id_filter->begin(), service_id_filter->end()) {}
};

} /* namespace AgentRepo3000 */

#endif /* AGENTREPO3000_WRITERS_HPP */

