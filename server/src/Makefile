OBJECTS = server.o agent_storage.o agent_session.o
CXXFLAGS = --std=c++11

server: ${OBJECTS}
	$(CXX) ${CXXFLAGS} ${OBJECTS} main.cpp -o server -lboost_system
