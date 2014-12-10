#include <netinet/in.h>
#include <iostream>
#include <functional>
#include <algorithm>
#include <vector>
#include "agent_session.hpp"
#include "agent_storage.hpp"

using boost::asio::async_read;
using boost::asio::async_write;
using boost::system::error_code;

namespace AgentRepo3000 {

void agent_session::do_read_requests() {
  
  auto self(shared_from_this());
  async_read(socket, boost::asio::buffer(&message_type, 1),
    [this, self](error_code ec, std::size_t) {
    using proto::message;
    if (!ec) {
      switch (message_type) {
      case message::REGISTER:
	handle_register();
	break;
      case message::SERVICES:
	handle_services();
	break;
      case message::LIST_AGENTS:
	handle_list_agents();
	break;
      case message::LIST_SERVICES:
	handle_list_services();
	break;
      default:
	; // FIXME wrong message type
      }
    }
  });
}

class ip_address_list_reader :
  public std::enable_shared_from_this<ip_address_list_reader> {
  typedef std::function<void(boost::system::error_code ec, std::vector<boost::asio::ip::address>)> after_type;
  
  boost::asio::ip::tcp::socket& socket;
  after_type after;
  
  boost::asio::ip::address_v4::bytes_type ipv4;
  boost::asio::ip::address_v6::bytes_type ipv6;
  
  
  std::vector<boost::asio::ip::address> ip_addresses;
  
  
  void do_read_ipv4() {
    auto self(shared_from_this());
    async_read(socket, boost::asio::buffer(ipv4, ipv4.size()),
      [this, self](error_code ec, std::size_t) {
	if (!ec) {
	  boost::asio::ip::address_v4::bytes_type end_of_list;
	  end_of_list.fill(0);
	  if (ipv4 != end_of_list) {
	    ip_addresses.emplace_back(boost::asio::ip::address_v4(ipv4));
	    do_read_ipv4();
	  }
	  else {
	    do_read_ipv6();
	  }
	} else {
	  after(ec, ip_addresses);
	}
      }
    );
  };
  void do_read_ipv6() {
    auto self(shared_from_this());
    async_read(socket, boost::asio::buffer(ipv6, ipv6.size()),
      [this, self](error_code ec, std::size_t) {
	if (!ec) {
	  boost::asio::ip::address_v6::bytes_type end_of_list;
	  end_of_list.fill(0);
	  if (ipv6 != end_of_list) {
	    ip_addresses.push_back(boost::asio::ip::address_v6(ipv6));
	    do_read_ipv6();
	  } else {
	    after(ec, ip_addresses);
	  }
	} else {
	  after(ec, ip_addresses);
	}
      }
    );
  }
  
public:
  ip_address_list_reader(boost::asio::ip::tcp::socket& socket) : socket(socket) {}
  void read(after_type after) {
    this->after = after;
    do_read_ipv4();
  }
};

class services_reader :
  public std::enable_shared_from_this<services_reader> {
  
  typedef std::function<void(boost::system::error_code ec, std::set<service_data>)> after_type;
  
  boost::asio::ip::tcp::socket& socket;
  after_type after;
  
  service_data service;
  
  std::set<service_data> services;
  
  void do_read_flags() {
    auto self(shared_from_this());
    async_read(socket, boost::asio::buffer(&service.flags, 1),
      [this, self](error_code ec, std::size_t) {
	if (!ec) {
	  if (service.flags == 0)
	    after(ec, services);
	  else
	    do_read_data();
	} else {
	  after(ec, services);
	}
      }
    );
  }
  
  void do_read_data() {
    auto self(shared_from_this());
    async_read(socket, service.data_to_buffers(),
      [this, self](error_code ec, std::size_t) {
	if (!ec) {
	  services.insert(service);
	  do_read_flags();
	}
	else { 
	  after(ec, services);
	}
      }
    );
  }
 
public:
  services_reader(boost::asio::ip::tcp::socket& socket) : socket(socket) {}
  void read(after_type after) {
    this->after = after;
    do_read_flags();
  }
};

void agent_session::handle_register() {
  auto self(shared_from_this());
  id = storage.register_agent();
  auto reader = std::make_shared<ip_address_list_reader>(socket);
  reader->read([this,self](boost::system::error_code ec, std::vector<boost::asio::ip::address> ip_addresses) {
    if (!ec) {
      data->ip_addresses = std::move(ip_addresses);
      async_write(socket, boost::asio::buffer(&id, 4), [this,self](boost::system::error_code ec, std::size_t) {
	if (!ec)
	  do_read_requests();
      });
    }
  });
  
}

void agent_session::handle_services() {
  auto self(shared_from_this());
  auto reader = std::make_shared<services_reader>(socket);
  reader->read([this,self](boost::system::error_code ec, std::set<service_data> services) {
    if (!ec) {
      data->services = std::move(services);
      do_read_requests();
    }
  });
}

class agent_list_writer :
  public std::enable_shared_from_this<agent_list_writer> {
  
  typedef std::function<void(boost::system::error_code ec)> after_type;
  
  boost::asio::ip::tcp::socket& socket;
  agent_storage& storage;
  after_type after;
  
  uint32_t last_id;
  std::vector<boost::asio::ip::address_v4::bytes_type> ipv4;
  std::vector<boost::asio::ip::address_v6::bytes_type> ipv6;
  
  void do_write_first() {
    storage.agents().begin();
  }
  
  void do_write_agent(agent_data& data) {
    auto self(shared_from_this());
  }
  
  void do_write_end_of_list() {
    auto self(shared_from_this());
    last_id = 0;
    async_write(socket, boost::asio::buffer(&last_id, 4), [this,self](boost::system::error_code ec, std::size_t) {
      after(ec);
    });
  }
  
  
public:
  agent_list_writer(boost::asio::ip::tcp::socket& socket, agent_storage& storage) :
    socket(socket), storage(storage) {}
  void write(after_type after) {
    do_write_first();
  }
};


void agent_session::handle_list_agents() {
  //FIXME
}

void agent_session::handle_list_services() {
  //FIXME
}

void agent_session::run() {
  do_read_requests();
}

} /* namespace AgentRepo3000 */
