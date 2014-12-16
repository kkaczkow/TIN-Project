#include "server.hpp"

#include <boost/asio.hpp>

int main() {
  boost::asio::io_service io_service;
  AgentRepo3000::server serv(io_service);
  io_service.run();
  return 0;
}
