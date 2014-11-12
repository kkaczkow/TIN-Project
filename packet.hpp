#include <ctype>
#include <sys/types.h>

struct packet {

enum type {

	REGISTER = 1,
	READY,
	NOT_READY,
	SERVICE_LIST,
	LIST_AGENTS,
	LIST_SERVICES,
	REQUEST_CONNECTION
};

uint8_t type;

};

struct ip_list {
 uint8_t countv4;
 ipv4_t ipsv4[1];
 uint8_t& countv6() {
	return *reinterpret_cast<uint8_t*>(&ipsv4[countv4]);
 }
 ipv4_t& ipsv6(uint8_t i) {
 	return reinterpret_cast<ipv4_t*>(&countv6()+1)[i];
 }
}

// usage
/*
bufor b[100];
packet& p = reintepret_cast<packet&>(bufor[0]);
switch (p.type) {
case IP_LIST:
 ip_list& ipl = reinterpret_cast<ip_list&>(p);
 ipl.countv4;
 for (int i = 0; i < ipl.countv6(); ++i)
	ipl.ipsv6(i);
}
*/

struct register {

 uint8_t type;
 ip_list ips;

}

struct service {
 enum {
  TCP,
  UDP
 };
 const int NAME_LEN = 10;
 char name[NAME_LEN];
 uint16_t port;
 uint8_t flags;
}

struct service_list {
 uint8_t type;
 uint32_t count;
 service services[1];
}

struct agent {
 uint32_t id;
 uint8_t flags;
 ip_list ips;
}

struct agent_list {
 uint8_t type;
 uint32_t count;
 agent agents[1];
}



