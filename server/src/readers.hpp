#ifndef AGENTREPO3000_READERS_HPP
#define AGENTREPO3000_READERS_HPP

#include <boost/asio.hpp>
#include <vector>
#include <set>
#include <functional>

#include "agent_data.hpp"

namespace AgentRepo3000 {
  
template<typename... Args>
class reader {
protected:
  typedef std::function<void(boost::system::error_code, Args...)> after_type;
 
  boost::asio::ip::tcp::socket& socket;
  after_type after;
  
  virtual void do_read() = 0;
  
public:
  reader(boost::asio::ip::tcp::socket& socket) : socket(socket) {}
  void read(after_type after) {
    this->after = after;
    do_read();
  }
};

class ip_address_list_reader :
  public reader<std::vector<boost::asio::ip::address>>,
  public std::enable_shared_from_this<ip_address_list_reader> {

  boost::asio::ip::address_v4::bytes_type ipv4;
  boost::asio::ip::address_v6::bytes_type ipv6;  
  
  std::vector<boost::asio::ip::address> ip_addresses;
  
  void do_read_ip_v4();
  void do_read_ip_v6();

  void do_read() {
    do_read_ip_v4();
  }
  
public:
  ip_address_list_reader(boost::asio::ip::tcp::socket& socket) : reader(socket) {}
};

class services_reader :
  public reader<std::set<service_data>>,
  public std::enable_shared_from_this<services_reader> {
  
  service_data service;
  
  std::set<service_data> services;
  
  void do_read_flags();
  void do_read_data();
  
  void do_read() {
    do_read_flags();
  }
 
public:
  services_reader(boost::asio::ip::tcp::socket& socket) : reader(socket) {}
};

class agent_ids_reader :
  public reader<std::vector<uint32_t>>,
  public std::enable_shared_from_this<agent_ids_reader> {

  uint32_t id;
  
  std::vector<uint32_t> ids;
  
  void do_read();
  
public:
  agent_ids_reader(boost::asio::ip::tcp::socket& socket) : reader(socket) {}
};

class service_ids_reader :
  public reader<std::vector<uint16_t>>,
  public std::enable_shared_from_this<service_ids_reader> {

  uint16_t id;
  
  std::vector<uint16_t> ids;
  
  void do_read();
  
public:
  service_ids_reader(boost::asio::ip::tcp::socket& socket) : reader(socket) {}
};
 
} /* namespace AgentRepo3000 */

#endif /* AGENTREPO3000_READERS_HPP */

