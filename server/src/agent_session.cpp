#include <netinet/in.h>
#include <iostream>
#include <vector>
#include "readers.hpp"
#include "writers.hpp"
#include "agent_session.hpp"
#include "agent_storage.hpp"

using boost::asio::async_read;
using boost::asio::async_write;
using boost::system::error_code;

namespace AgentRepo3000 {
  
using logging::level;

std::ostream& agent_session::log(logging::level lvl) const {
  return std::clog << "["
    << m_address.to_string()
    << ':' << m_port
    << "]: ";
}

agent_session::~agent_session() {
  if (data) {
    storage.unregister_agent(id);
    log(level::INFO) << "Unregistered." << std::endl;
  }
  log(level::INFO) << "Disconnected." << std::endl;
}

void agent_session::do_read_requests() {
  auto self(shared_from_this());
  async_read(socket, boost::asio::buffer(&message_type, 1),
    [this, self](error_code ec, std::size_t) {
    using proto::message;
    if (!ec) {
      log(level::TRACE) << "Got message, type=" << ((int)message_type) << std::endl;
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
	 // FIXME wrong message type
	log(level::ERROR) << "Read wrong message type." << std::endl;
      }
    }
  });
}

static proto::message registered_id = proto::message::REGISTERED_ID;

void agent_session::handle_register() {
  auto self(shared_from_this());
  id = storage.register_agent();
  networkId = htonl(id);
  data = storage.agent(id);
  auto reader = std::make_shared<ip_address_list_reader>(socket);
  reader->read([this,self](error_code ec, std::vector<boost::asio::ip::address> ip_addresses) {
    if (!ec) {
      data->ip_addresses = std::move(ip_addresses);
      std::vector<boost::asio::const_buffer> buffers{
	  boost::asio::buffer(&registered_id, 1),
	  boost::asio::buffer(&networkId, 4)
      };
      async_write(socket, buffers, [this,self](error_code ec, std::size_t) {
	log(level::INFO) << "Registered with id: " << id << std::endl;
	if (!ec)
	  do_read_requests();
      });
    }
  });
  
}

void agent_session::handle_services() {
  auto self(shared_from_this());
  auto reader = std::make_shared<services_reader>(socket);
  reader->read([this,self](error_code ec, std::set<service_data> services) {
    if (!ec) {
      log(level::INFO) << "Registered services: " << std::endl;
      for (auto&& service : services) {
	log(level::INFO) << '\t' << service.id << '@' << service.port << std::endl;
      }
      if (data)
	data->services = std::move(services);
      do_read_requests();
    }
  });
}

void agent_session::handle_list_agents() {
  auto self(shared_from_this());
  auto writer = std::make_shared<agent_list_writer>(socket, storage);
  writer->write([this,self](error_code ec) {
    log(level::DEBUG) << "Listing agents. " << std::endl;
    if(!ec)
      do_read_requests();
  });
}

void agent_session::handle_list_services() {
  auto self(shared_from_this());
  auto agent_reader = std::make_shared<agent_ids_reader>(socket);
  agent_reader->read([this,self](error_code ec, std::vector<uint32_t> ids) {
    if (ec)
      return;
    
    auto agent_ids_ptr = std::make_shared<std::vector<uint32_t>>(std::move(ids));
    auto service_reader = std::make_shared<service_ids_reader>(socket);
    service_reader->read([this,self,agent_ids_ptr](error_code ec, std::vector<uint16_t> services) {
      if (ec)
	return;
      
      auto service_ids_ptr = std::make_shared<std::vector<uint16_t>>(std::move(services));
      
      if (agent_ids_ptr->empty()) {
	if (service_ids_ptr->empty()) {
	  log(level::DEBUG) << "Listing all services." << std::endl;
	} else {
	  log(level::DEBUG) << "Listing services of all agents with ids:" << std::endl;
	  for (uint16_t id : *service_ids_ptr)
	    log(level::DEBUG) << '\t' << id << std::endl;
	}
      } else {
	if (service_ids_ptr->empty()) {
	  log(level::DEBUG) << "Listing all services of:" << std::endl;
	  for (uint32_t id : *agent_ids_ptr)
	    log(level::DEBUG) << '\t' << id << std::endl;
	} else {
	  log(level::DEBUG) << "Listing services" << std::endl;
	  log(level::DEBUG) << "\tof" << std::endl;
	  for (uint32_t id : *agent_ids_ptr)
	    log(level::DEBUG) << "\t\t" << id << std::endl;
	  log(level::DEBUG) << "\twith ids:" << std::endl;
	  for (uint16_t id : *service_ids_ptr)
	    log(level::DEBUG) << "\t\t" << id << std::endl;
	}
      }
      
      auto writer = std::make_shared<service_list_writer>(socket, storage, agent_ids_ptr, service_ids_ptr);
      writer->write([this, self](error_code ec) {
	if (!ec)
	  do_read_requests();
      });
    });
  });
}

void agent_session::run() {
  log(level::TRACE) << "Started." << std::endl;
  do_read_requests();
}

} /* namespace AgentRepo3000 */
