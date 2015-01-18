#include "server.hpp"

#include <boost/asio.hpp>

#include <iostream>

using boost::asio::ip::tcp;

namespace AgentRepo3000 {

using logging::level;

const int server::PORT = 12345;

std::ostream& server::log(logging::level lvl) {
  auto endpoint = acceptor.local_endpoint();
  std::clog << "[@";
  if (endpoint.address() != boost::asio::ip::address_v4::any())
    std::clog << endpoint.address().to_string();
  return std::clog << ':' << endpoint.port() << "]: ";
}

server::server(boost::asio::io_service& io_service) :
  acceptor(io_service, tcp::endpoint(tcp::v4(), PORT)),
  socket(io_service) {
  log(level::INFO) << "Server started" << std::endl;
  do_accept();
}

void server::do_accept() {
  acceptor.async_accept(socket, [this](boost::system::error_code ec){
    if (!ec) {
      auto endpoint = socket.remote_endpoint();
      log(level::INFO) << "Accepted connection from "
	<< endpoint.address().to_string()
	<< ':' << endpoint.port()
	<< std::endl;
      std::make_shared<agent_session>(std::move(socket), storage)->run();
    }
    do_accept();
  });
}

}

