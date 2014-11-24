/*
 * agent_session.cpp
 *
 *  Created on: Dec 17, 2014
 *      Author: konrad
 */

#include <iostream>
#include <functional>
#include <algorithm>
#include "agent_session.hpp"
#include "agent_storage.hpp"
#include "list.hpp"

using boost::asio::async_read;
using boost::asio::async_write;
using boost::system::error_code;

namespace AgentRepo3000 {

const char* agent_session::registered = "Registered\n";

void agent_session::do_read_requests() {
	auto self(shared_from_this());
	std::clog << "Accepted agent" << std::endl;
	async_read(socket, boost::asio::buffer(&message_type, 1),
		[this, self](error_code ec, std::size_t) {
		switch (message_type) {
		case REGISTER:
			handle_register();
			break;
		}
	});
}

class ip_address_list_reader :
  public std::enable_shared_from_this<ip_address_list_reader> {
  boost::asio::ip::tcp::socket& socket;
  boost::asio::ip::address_v4::bytes_type ipv4;
  boost::asio::ip::address_v6::bytes_type ipv6;
  typedef std::function<void(boost::system::error_code ec, list<boost::asio::ip::address>)> after_type;
  after_type after;
  
  list<boost::asio::ip::address> ip_addresses;
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

void agent_session::handle_register() {
	auto self(shared_from_this());
	data = &storage.register_agent(socket.remote_endpoint());
	ip_address_list_reader reader(socket);
	reader.read([this,self](boost::system::error_code ec, list<boost::asio::ip::address> ip_addresses) {
	  if (!ec) {
	    data->ip_addresses = ip_addresses;
	    do_read_requests();
	  }
	});
}

void agent_session::run() {
	do_read_requests();
}

} /* namespace AgentRepo3000 */
