OBJECTS = server.o agent_session.o
CXXFLAGS = --std=c++11

server: ${OBJECTS}
	$(CXX) -lboost_system ${CXXFLAGS} ${OBJECTS} main.cpp -o server
