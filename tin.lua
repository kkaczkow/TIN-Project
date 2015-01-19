-- najprosciej dodac ten skrypt wywolujac wireszarka w nastepujacy sposob:
-- sudo wireshark -X lua_script:'/<home_dir>/tin.lua'

p_repoagent3000= Proto("repoagent3000", "RepoAgent3000 Protocol")
local data_data = Field.new("data.data")
local td_type = ProtoField.string("repoagent3000.type", "Packet type", ftypes.STRING)
local td_data = ProtoField.bytes("repoagent3000.data", "Packet data")
p_repoagent3000.fields._type = td_type
p_repoagent3000.fields._data = td_data

local ssl_dissector = Dissector.get("ssl")

function p_repoagent3000.dissector(buffer, pinfo, tree)
	pinfo.cols.protocol = "TIN"
	local subtree = tree:add(p_repoagent3000, buffer(), "RepoAgent3000 Protocol Data")
	if data_data() then
		local data_arr = { data_data() }
		local data = ByteArray.new()
		for k, v in ipairs(data_arr) do
			data:append(v())
		end
		local data_tvb = ByteArray.tvb(data, "RepoAgent3000 Raw Data")
		local packet_type = "UNDEFINED"
		if data_tvb[1] == 0x01 then
			packet_type = "REGISTER"
		elseif data_tvb[1] == 0x81 then
			packet_type = "REGISTERED_ID"
		elseif data_tvb[1] == 0x02 then
			packet_type = "SERVICES"
		elseif data_tvb[1] == 0x03 then
			packet_type = "LIST_AGENTS"
		elseif data_tvb[1] == 0x83 then
			packet_type = "AGENT_LIST"
		elseif data_tvb[1] == 0x04 then
			packet_type = "LIST_SERVICES"
		elseif data_tvb[1] == 0x84 then
			packet_type = "SERVICES_LIST"
		elseif data_tvb[1] == 0x05 then
			packet_type = "CONN_REQUEST"
		elseif data_tvb[1] == 0x85 then
			packet_type = "CONNECT"
		end
		subtree:add(td_type, packet_type)
		subtree:add(td_data, data_tvb())
	end
end

DissectorTable.get("tcp.port"):add(9000, p_repoagent3000)
