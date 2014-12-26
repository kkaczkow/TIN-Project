#include "writers.hpp"

#include <array>

using boost::asio::async_write;
using boost::system::error_code;

namespace AgentRepo3000 {

const proto::message agent_list_writer::message_type = proto::message::AGENT_LIST;

const proto::message service_list_writer::message_type = proto::message::SERVICE_LIST;
const std::array<uint8_t, 1> service_list_writer::end_of_services = {{0}};
const std::array<uint8_t, 4> service_list_writer::end_of_agents = {{0, 0, 0, 0}};

void agent_list_writer::do_write_next() {
  buffers.clear();
  auto it = storage.agents().upper_bound(last_id);
  if (it != storage.agents().end()) {
    last_id = it->first;
    do_write_agent(it->second);
  } else  {
    do_write_end_of_list();
  }
}

void agent_list_writer::do_write_agent(std::shared_ptr<agent_data> data) {
  auto self(shared_from_this());
  ipv4.clear();
  ipv6.clear();
  for (auto&& addr : data->ip_addresses) {
    if (addr.is_v4())
      ipv4.push_back(addr.to_v4().to_bytes());
    else if (addr.is_v6())
      ipv6.push_back(addr.to_v6().to_bytes());
  }
  boost::asio::ip::address_v4::bytes_type end_of_ipv4;
  end_of_ipv4.fill(0);
  ipv4.push_back(end_of_ipv4);
  boost::asio::ip::address_v6::bytes_type end_of_ipv6;
  end_of_ipv6.fill(0);
  ipv6.push_back(end_of_ipv6);
  
  id = htonl(last_id);
  buffers.push_back(boost::asio::buffer(&id, 4));
  for (auto& data : ipv4)
    buffers.push_back(boost::asio::buffer(data, data.size()));
  for (auto& data : ipv6)
    buffers.push_back(boost::asio::buffer(data, data.size()));
  async_write(socket, buffers, [this,self](boost::system::error_code ec, std::size_t) {
    buffers.clear();
    if (!ec)
      do_write_next();
    else
      after(ec);
  });
}

void agent_list_writer::do_write_end_of_list() {
  auto self(shared_from_this());
  last_id = 0;
  buffers.push_back(boost::asio::buffer(&last_id, 4));
  async_write(socket, buffers, [this,self](boost::system::error_code ec, std::size_t) {
    buffers.clear();
    after(ec);
  });
}

void service_list_writer::do_write_next_agent() {
  auto it = storage.agents().upper_bound(last_id);
  while (it != storage.agents().end() && 
    !agent_id_filter.empty() &&
    agent_id_filter.find(it->first) == agent_id_filter.end())
    ++it;
  if (it != storage.agents().end()) {
    last_id = it->first;
    agent = it->second;
    do_write_first_service();
  } else {
    do_write_end_of_agents();
  }
}  

void service_list_writer::do_write_first_service() {
  auto it = agent->services.begin();
  while (it != agent->services.end() && 
    !service_id_filter.empty() &&
    service_id_filter.find(it->id) == service_id_filter.end())
    ++it;
  if (it != agent->services.end()) {
    id = htonl(last_id);
    buffers.push_back(boost::asio::buffer(&id, 4));
    last_service = *it;
    do_write_service();
  } else {
    do_write_next_agent();
  }
}

void service_list_writer::do_write_next_service() {
  auto it = agent->services.upper_bound(last_service);
  while (it != agent->services.end() && 
    !service_id_filter.empty() &&
    service_id_filter.find(it->id) == service_id_filter.end())
    ++it;
  if (it != agent->services.end()) {
    last_service = *it;
    do_write_service();
  } else {
    do_write_end_of_services();
  }
}

void service_list_writer::do_write_service() {
  auto self(shared_from_this());
  service = last_service;
  service.id = htons(service.id);
  service.port = htons(service.port);
  auto sbuf = service.to_buffers();
  buffers.insert(buffers.end(), sbuf.begin(), sbuf.end());
  async_write(socket, buffers,
    [this, self](error_code ec, std::size_t) {
    buffers.clear();
    
    if (ec) {
      after(ec);
      return;
    }
    
    do_write_next_service();
  });
}

void service_list_writer::do_write_end_of_services() {
  auto self(shared_from_this());
  buffers.push_back(boost::asio::buffer(end_of_services, end_of_services.size()));
  async_write(socket, buffers,
    [this, self](error_code ec, std::size_t) {
    buffers.clear();
    
    if (ec) {
      after(ec);
      return;
    }
    
    do_write_next_agent();
  });
}
  
void service_list_writer::do_write_end_of_agents() {
  auto self(shared_from_this());
  buffers.push_back(boost::asio::buffer(end_of_agents, end_of_agents.size()));
  async_write(socket, buffers,
    [this, self](error_code ec, std::size_t) {
    buffers.clear();
    after(ec);
  });
}

} /* namespace AgentRepo3000 */