#include "readers.hpp"

using boost::asio::async_read;
using boost::system::error_code;

namespace AgentRepo3000 {

void ip_address_list_reader::do_read_ip_v4() {
  auto self(shared_from_this());
  async_read(socket, boost::asio::buffer(ipv4, ipv4.size()),
    [this, self](error_code ec, std::size_t) {
      if (!ec) {
	boost::asio::ip::address_v4::bytes_type end_of_list;
	end_of_list.fill(0);
	if (ipv4 != end_of_list) {
	  ip_addresses.emplace_back(boost::asio::ip::address_v4(ipv4));
	  do_read_ip_v4();
	}
	else {
	  do_read_ip_v6();
	}
      } else {
	after(ec, ip_addresses);
      }
    }
  );
}

void ip_address_list_reader::do_read_ip_v6() {
  auto self(shared_from_this());
  async_read(socket, boost::asio::buffer(ipv6, ipv6.size()),
    [this, self](error_code ec, std::size_t) {
      if (!ec) {
	boost::asio::ip::address_v6::bytes_type end_of_list;
	end_of_list.fill(0);
	if (ipv6 != end_of_list) {
	  ip_addresses.push_back(boost::asio::ip::address_v6(ipv6));
	  do_read_ip_v6();
	} else {
	  after(ec, ip_addresses);
	}
      } else {
	after(ec, ip_addresses);
      }
    }
  );
}

void services_reader::do_read_flags() {
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
  
void services_reader::do_read_data() {
  auto self(shared_from_this());
  async_read(socket, service.data_to_buffers(),
    [this, self](error_code ec, std::size_t) {
      if (!ec) {
	service.id = ntohs(service.id);
	service.port = ntohs(service.port);
	services.insert(service);
	do_read_flags();
      }
      else { 
	after(ec, services);
      }
    }
  );
}

void agent_ids_reader::do_read() {
  auto self(shared_from_this());
  async_read(socket, boost::asio::buffer(&id, 4),
    [this, self](error_code ec, std::size_t) {
      if (!ec) {
	id = ntohl(id);
	if (id != 0) {
	  ids.push_back(id);
	  do_read();
	} else {
	  after(ec, ids);
	} 
      } else {
	after(ec, ids);
      }
  });
}

void service_ids_reader::do_read() {
  auto self(shared_from_this());
  async_read(socket, boost::asio::buffer(&id, 2),
    [this, self](error_code ec, std::size_t) {
      if (!ec) {
	id = ntohs(id);
	if (id != 0) {
	  ids.push_back(id);
	  do_read();
	} else {
	  after(ec, ids);
	}
      } else {
	after(ec, ids);
      }
  });
}

} /* namespace AgentRepo3000 */